package com.example.p8vitesse.ui.add

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.p8vitesse.R
import com.example.p8vitesse.domain.model.Candidat
import java.io.FileNotFoundException
import java.util.Calendar
import java.util.Date

class AddCandidatActivity : AppCompatActivity() {
    private val REQUEST_CODE_PICK_IMAGE = 1001
    private lateinit var userProfilePicture: ImageView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_candidat)  // Replace with your correct layout name

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val editText = findViewById<EditText>(R.id.editTextDateOfBirth)
        userProfilePicture = findViewById<ImageView>(R.id.userProfilePicture)

        // Set the toolbar as the app's action bar
        setSupportActionBar(toolbar)

        // Enable the "up" button (back button) on the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set the title of the activity
        supportActionBar?.title = "Activity Title"

        showDatePicker(editText)
        userProfilePicture.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            // Get the URI of the selected image
            val imageUri: Uri = data.data!!

            // Convert URI to Bitmap
            val bitmap = getBitmapFromUri(imageUri)

            // Set the image to the ImageView
            userProfilePicture.setImageBitmap(bitmap)

            // Save the Bitmap in the Candidat object
            val profilePicture: Bitmap? = bitmap
            val candidat = Candidat(
                name = "John",
                surname = "Doe",
                phone = "123456789",
                email = "example@example.com",
                birthdate = Date(),
                desiredSalary = 50000.0,
                note = "Some notes",
                isFav = false,
                profilePicture = profilePicture
            )
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


    // Handle the "up" button click event
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Handle the return action, usually navigating back
                onBackPressed()  // Go back to the previous activity
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun showDatePicker(editTextDateOfBirth: EditText) {
        // Get the current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        // Create DatePickerDialog
        val datePickerDialog = DatePickerDialog(editTextDateOfBirth.context, { _, selectedYear, selectedMonth, selectedDay ->
            // Format the selected date as jj/mm/aaaa
            val formattedDate = "${selectedDay}/${selectedMonth + 1}/$selectedYear"
            editTextDateOfBirth.setText(formattedDate)
        }, year, month, day)

        // Restrict date picker to not allow selecting a future date
        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        // Show the DatePickerDialog when the EditText is clicked

        findViewById<ImageView>(R.id.calendaricon).setOnClickListener {
            datePickerDialog.show()
        }
    }

}