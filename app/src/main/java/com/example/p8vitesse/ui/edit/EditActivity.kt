package com.example.p8vitesse.ui.edit

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.p8vitesse.MainActivity
import com.example.p8vitesse.R
import com.example.p8vitesse.domain.model.Candidat
import com.example.p8vitesse.ui.detail.CandidatDetailActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class EditActivity : AppCompatActivity() {
    private val REQUEST_CODE_PICK_IMAGE = 1001
    private lateinit var userProfilePicture: ImageView
    private val viewModel: EditViewModel by viewModels()
    private var candidatId: Long? = null  // Nullable type for candidatId
    private var yourBitmap: Bitmap? = null // Make this nullable to handle the null case
    private var isFav: Boolean = false  // Add this variable to store the isFav value


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)  // Replace with your correct layout name

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val editText = findViewById<EditText>(R.id.editTextDateOfBirth)
        userProfilePicture = findViewById<ImageView>(R.id.userProfilePicture)
        val editTextEmail = findViewById<EditText>(R.id.emailTextName)
        val saveFloatingActionButton = findViewById<ExtendedFloatingActionButton>(R.id.fabSave)

        // Retrieve the Candidat ID from the Intent
        candidatId = intent.getLongExtra("CANDIDAT_ID", -1)

        Log.e("AppDatabase", "id in edit : $candidatId")

        // Inside your Activity or Fragment
        if (candidatId != -1L) {
            // Fetch Candidat by ID
            viewModel.getCandidatById(candidatId!!.toInt())

            // Collect the flow from the ViewModel
            lifecycleScope.launch{
                viewModel.candidat.collect { candidat ->
                    candidat?.let {
                        setPrefilledData(it)  // Set the values in the UI
                    }
                }
            }
        }

        // Set the toolbar as the app's action bar
        setSupportActionBar(toolbar)

        // Enable the "up" button (back button) on the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set the title of the activity
        supportActionBar?.title = "Modifier un candidat"

        showDatePicker(editText)
        userProfilePicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }

        // Call the function to add text watcher for email validation
        validateEmailWhileTyping(editTextEmail)

        saveFloatingActionButton.setOnClickListener {
            saveCandidat()
            setResult(RESULT_OK) // Inform the previous activity that the save was successful
            finish()
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // Trigger back navigation
        return true
    }

    // Function to add a TextWatcher to EditText and validate email
    private fun validateEmailWhileTyping(editTextEmail: EditText) {
        val textInputLayout = findViewById<TextInputLayout>(R.id.textInputLayoutEmail)

        editTextEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val email = editable.toString()
                if (isValidEmail(email)) {
                    textInputLayout.error = null
                } else {
                    textInputLayout.error = "Format email invalide"
                }
            }
        })
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            // Get the URI of the selected image
            val imageUri: Uri = data.data!!

            // Convert URI to Bitmap
            val bitmap = getBitmapFromUri(imageUri)

            if (bitmap != null) {
                yourBitmap = bitmap
            }

            // Set the image to the ImageView
            userProfilePicture.setImageBitmap(bitmap)
        }
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {

                finish()  // Close the current activity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showDatePicker(editTextDateOfBirth: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(editTextDateOfBirth.context, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = "${selectedDay}/${selectedMonth + 1}/$selectedYear"
            editTextDateOfBirth.setText(formattedDate)
        }, year, month, day)

        datePickerDialog.datePicker.maxDate = calendar.timeInMillis

        findViewById<ImageView>(R.id.calendaricon).setOnClickListener {
            datePickerDialog.show()
        }
    }

    fun setPrefilledData(candidat: Candidat) {
        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextSurname = findViewById<EditText>(R.id.editTextSurname)
        val editTextPhone = findViewById<EditText>(R.id.phoneTextView)
        val editTextEmail = findViewById<EditText>(R.id.emailTextName)
        val editTextSalary = findViewById<EditText>(R.id.salaryTextName)
        val editTextNote = findViewById<EditText>(R.id.noteTextName)
        val editTextDateOfBirth = findViewById<EditText>(R.id.editTextDateOfBirth)

        editTextName.setText(candidat.name)
        editTextSurname.setText(candidat.surname)
        editTextPhone.setText(candidat.phone)
        editTextEmail.setText(candidat.email)
        editTextSalary.setText(candidat.desiredSalary.toString())
        editTextNote.setText(candidat.note)
        editTextDateOfBirth.setText(candidat.birthdate.toString())

        // Format the birthdate to "dd/MM/yyyy" before setting it in the EditText
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(candidat.birthdate)
        editTextDateOfBirth.setText(formattedDate)

        isFav = candidat.isFav

    }

    fun saveCandidat() {
        try {
            Log.e("AppDatabase", "save function called")

            val editTextName = findViewById<EditText>(R.id.editTextName)
            val editTextSurname = findViewById<EditText>(R.id.editTextSurname)
            val editTextPhone = findViewById<EditText>(R.id.phoneTextView)
            val editTextEmail = findViewById<EditText>(R.id.emailTextName)
            val editTextSalary = findViewById<EditText>(R.id.salaryTextName)
            val editTextNote = findViewById<EditText>(R.id.noteTextName)
            val editBirthdate = findViewById<EditText>(R.id.editTextDateOfBirth)

            val name = editTextName.text.toString().trim()
            val surname = editTextSurname.text.toString().trim()
            val phone = editTextPhone.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val salary = editTextSalary.text.toString().trim().toDoubleOrNull() ?: 0.0
            val note = editTextNote.text.toString().trim()
            //val birthdate = editBirthdate.text.toString().trim()
            val profilePicture = yourBitmap
            val birthdateString = editBirthdate.text.toString().trim()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val birthdate: Date? = dateFormat.parse(birthdateString)


            // Fetch the existing Candidat to retain the existing profile picture if no new image is selected
            val existingCandidat = viewModel.candidat.value
            val finalProfilePicture = profilePicture ?: existingCandidat?.profilePicture


            if (birthdateString.isEmpty()) {
                Toast.makeText(this, "Please enter a valid birthday", Toast.LENGTH_SHORT).show()
                return
            }

            if (note.isEmpty()) {
                Toast.makeText(this, "Please enter a valid note", Toast.LENGTH_SHORT).show()
                return
            }

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter a valid name", Toast.LENGTH_SHORT).show()
                return
            }

            if (surname.isEmpty()) {
                Toast.makeText(this, "Please enter a valid surname", Toast.LENGTH_SHORT).show()
                return
            }

            if (!isValidEmail(email)) {
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                return
            }

            if (phone.isEmpty()) {
                Toast.makeText(this, "Please enter a phone number", Toast.LENGTH_SHORT).show()
                return
            }

            if (salary <= 0.0) {
                Toast.makeText(this, "Please enter a valid salary", Toast.LENGTH_SHORT).show()
                return
            }

            Log.e("AppDatabase", "Data collected: image:$profilePicture, Birthdate=$birthdate, Name=$name, Surname=$surname, Phone=$phone, Email=$email, Salary=$salary, Note=$note")
            try {
                // Create the Candidat object with the provided values
                val candidat = Candidat(
                    id = candidatId,
                    name = name,
                    surname = surname,
                    phone = phone,
                    email = email,
                    birthdate = birthdate!!,  // Use current date for birthdate, consider parsing if needed
                    desiredSalary = salary,
                    note = note,
                    isFav = isFav,  // Default value for isFav
                    profilePicture = finalProfilePicture  // The profile picture Bitmap (could be null)
                )

                // Call the ViewModel to update the Candidat (you should handle any potential failure here)
                viewModel.updateCandidat(candidat)

                Log.e("AppDatabase", "candidat update id is: ${candidat.id}")
                //Toast.makeText(this, "Candidat update is is: ${candidat.id}", Toast.LENGTH_SHORT).show()

                // Show a success message
                //Toast.makeText(this, "Candidat saved successfully", Toast.LENGTH_SHORT).show()

                // Clear the fields after saving
                clearFields()

                // Set the result and finish the activity (e.g., to go back to the previous screen)
                setResult(RESULT_OK)


                finish()

            } catch (e: IllegalArgumentException) {
                // Handle case where any argument is invalid, for example:
                Log.e("AppDatabase", "Error saving candidat: Invalid argument - ${e.message}")
                Toast.makeText(this, "Please check the input fields and try again.", Toast.LENGTH_LONG).show()

            } catch (e: Exception) {
                // Handle any other unexpected errors (network, database, etc.)
                Log.e("AppDatabase", "Error saving candidat: ${e.message}", e)
                Toast.makeText(this, "An error occurred while saving the Candidat. Please try again.", Toast.LENGTH_LONG).show()
            }


        } catch (e: Exception) {
            Log.e("AppDatabase", "Error saving candidat", e)
            Toast.makeText(this, "An error occurred while saving the Candidat. Please try again.", Toast.LENGTH_LONG).show()
        }
    }

    fun clearFields() {
        findViewById<EditText>(R.id.editTextName).text.clear()
        findViewById<EditText>(R.id.editTextSurname).text.clear()
        findViewById<EditText>(R.id.phoneTextView).text.clear()
        findViewById<EditText>(R.id.emailTextName).text.clear()
        findViewById<EditText>(R.id.salaryTextName).text.clear()
        findViewById<EditText>(R.id.noteTextName).text.clear()
    }
}
