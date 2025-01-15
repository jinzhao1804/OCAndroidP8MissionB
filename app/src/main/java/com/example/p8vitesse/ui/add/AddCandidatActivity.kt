package com.example.p8vitesse.ui.add

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
import androidx.lifecycle.lifecycleScope
import com.example.p8vitesse.R
import com.example.p8vitesse.domain.model.Candidat
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.FileNotFoundException
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class AddCandidatActivity : AppCompatActivity() {

    private val REQUEST_CODE_PICK_IMAGE = 1001
    private lateinit var userProfilePicture: ImageView
    private var yourBitmap: Bitmap? = null // Make this nullable to handle the null case
    private val viewModel: AddCandidatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_candidat)  // Replace with your correct layout name

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val editText = findViewById<EditText>(R.id.editTextDateOfBirth)
        userProfilePicture = findViewById(R.id.userProfilePicture)
        val editTextEmail = findViewById<EditText>(R.id.emailTextName)
        val saveFloatingActionButton = findViewById<ExtendedFloatingActionButton>(R.id.fabSave)

        // Set the toolbar as the app's action bar
        setSupportActionBar(toolbar)

        // Enable the "up" button (back button) on the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set the title of the activity
        supportActionBar?.title = "Ajouter un candidat"

        // Show DatePicker when Date of Birth EditText is clicked
        showDatePicker(editText)

        // Set OnClickListener for profile picture selection
        userProfilePicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }

        // Validate email while typing
        validateEmailWhileTyping(editTextEmail)

        // Save the candidate when the save button is clicked
        saveFloatingActionButton.setOnClickListener { saveCandidat() }
    }

    // Function to validate email format while typing
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

    // Function to validate email using Android's built-in Patterns
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Handle the result from picking an image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            val imageUri: Uri = data.data!!  // Get the URI of the selected image

            // Convert URI to Bitmap
            yourBitmap = getBitmapFromUri(imageUri)


            // Set the image to the ImageView
            userProfilePicture.setImageBitmap(yourBitmap)
        }
    }

    // Convert URI to Bitmap
    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    // Handle the "up" button click event to go back
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                //onBackPressed()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Show DatePickerDialog for selecting birthdate
    fun showDatePicker(editTextDateOfBirth: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(editTextDateOfBirth.context, { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = "${selectedDay}/${selectedMonth + 1}/$selectedYear"
            editTextDateOfBirth.setText(formattedDate)
        }, year, month, day)

        // Restrict the date picker to not allow selecting a future date
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        findViewById<ImageView>(R.id.calendaricon).setOnClickListener {
            datePickerDialog.show()
        }
    }
    // Save the candidate data
    private fun saveCandidat() {
        lifecycleScope.launch {
            try {
                Log.e("AppDatabase", "save function called")

                // Get the input values from EditText fields
                val editTextName = findViewById<EditText>(R.id.editTextName)
                val editTextSurname = findViewById<EditText>(R.id.editTextSurname)
                val editTextPhone = findViewById<EditText>(R.id.phoneTextView)
                val editTextEmail = findViewById<EditText>(R.id.emailTextName)
                val editTextSalary = findViewById<EditText>(R.id.salaryTextName)
                val editTextNote = findViewById<EditText>(R.id.noteTextName)

                val name = editTextName.text.toString().trim()
                val surname = editTextSurname.text.toString().trim()
                val phone = editTextPhone.text.toString().trim()
                val email = editTextEmail.text.toString().trim()
                val salary = editTextSalary.text.toString().trim().toDoubleOrNull() ?: 0.0
                val note = editTextNote.text.toString().trim()

                // Validate input data
                if (name.isEmpty()) {
                    Toast.makeText(this@AddCandidatActivity, "Please enter a valid name", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                if (surname.isEmpty()) {
                    Toast.makeText(this@AddCandidatActivity, "Please enter a valid surname", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                if (!isValidEmail(email)) {
                    Toast.makeText(this@AddCandidatActivity, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                if (phone.isEmpty()) {
                    Toast.makeText(this@AddCandidatActivity, "Please enter a phone number", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                if (note.isEmpty()) {
                    Toast.makeText(this@AddCandidatActivity, "Please enter a note", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                if (salary <= 0.0) {
                    Toast.makeText(this@AddCandidatActivity, "Please enter a valid salary", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                Log.e("AppDatabase", "Data collected: Name=$name, Surname=$surname, Phone=$phone, Email=$email, Salary=$salary, Note=$note")

                // Create a new Candidat object with the provided values
                val candidat = Candidat(
                    name = name,
                    surname = surname,
                    phone = phone,
                    email = email,
                    birthdate = Date(),  // Use a DatePicker to get a date input
                    desiredSalary = salary,
                    note = note,
                    isFav = false, // Assuming isFav is a default value
                    profilePicture = yourBitmap  // Store the selected image
                )

                // Save the Candidat using the ViewModel
                viewModel.addCandidat(candidat)

                Log.e("AppDatabase", "Candidat saved successfully")
                Toast.makeText(this@AddCandidatActivity, "Candidat saved successfully", Toast.LENGTH_SHORT).show()

                // Clear the fields after saving
                clearFields()

                // Set the result to notify the calling fragment (AllFragment)
                setResult(RESULT_OK)
                finish()  // Close the activity and go back to AllFragment

            } catch (e: Exception) {
                Log.e("AppDatabase", "Error saving candidat", e)
                Toast.makeText(this@AddCandidatActivity, "An error occurred while saving the Candidat. Please try again.", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Optionally, clear the input fields after saving
    fun clearFields() {
        findViewById<EditText>(R.id.editTextName).text.clear()
        findViewById<EditText>(R.id.editTextSurname).text.clear()
        findViewById<EditText>(R.id.phoneTextView).text.clear()
        findViewById<EditText>(R.id.emailTextName).text.clear()
        findViewById<EditText>(R.id.salaryTextName).text.clear()
        findViewById<EditText>(R.id.noteTextName).text.clear()
    }
}