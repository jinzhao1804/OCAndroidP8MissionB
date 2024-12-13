package com.example.p8vitesse.ui.add

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.p8vitesse.R
import java.util.Calendar

class AddCandidatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_candidat)  // Replace with your correct layout name

        // Find the toolbar by its ID
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val editText = findViewById<EditText>(R.id.editTextDateOfBirth)
        // Set the toolbar as the app's action bar
        setSupportActionBar(toolbar)

        // Enable the "up" button (back button) on the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set the title of the activity
        supportActionBar?.title = "Activity Title"

        showDatePicker(editText)
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