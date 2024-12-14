package com.example.p8vitesse.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.p8vitesse.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class CandidatDetailActivity : AppCompatActivity() {

    private val candidatViewModel: CandidatDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidat_detail)  // Set the layout for the detail activity

        Log.e("AppDatabase", "Intent data: ${intent.getIntExtra("CANDIDAT_ID", -1)}")

        // Get the Candidat ID passed from the AllFragment
        val candidatId = intent.getStringExtra("CANDIDAT_ID")  // Default value is -1 if no ID is passed

        Log.e("AppDatabase", "CandidatId fetched id is : $candidatId")

        // You can use the Candidat ID to fetch more details from the ViewModel or database
        if (candidatId != null) {
            if (candidatId.toInt() != -1) {
                // Fetch and display the Candidat details using the ID
                fetchAndDisplayCandidatDetails(candidatId.toInt())
            } else {
                // Handle the case when no valid ID is passed
            }
        }


    }

    private fun fetchAndDisplayCandidatDetails(candidatId: Int) {

        // First, trigger the ViewModel to fetch the candidat by ID
        candidatViewModel.getCandidatById(candidatId)
        // Collect the Candidat data from the StateFlow
        lifecycleScope.launch {
            candidatViewModel.candidat.collect { candidat ->
                candidat?.let {

                    // Initialize the Toolbar
                    val toolbar: Toolbar = findViewById(R.id.toolbar)
                    setSupportActionBar(toolbar)
                    supportActionBar?.title = candidat.name +" " + candidat.surname.toUpperCase()
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    // Populate the UI with the fetched Candidat data
                    updateBirthdateAndAnniversary(candidat.birthdate.toString())
                    updateSalaries(candidat.desiredSalary)
                    setNoteContent("Notes", candidat.note)
                }
            }
        }
    }

    // Inflate the menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    // Handle the click events for the icons
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Handle back button click
                onBackPressed()
                return true
            }
            R.id.action_favorite -> {
                // Handle the star (favorite) icon click
                return true
            }
            R.id.action_edit -> {
                // Handle the edit icon click
                return true
            }
            R.id.action_delete -> {
                // Handle the delete icon click
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    // Handle call icon click
    fun onCallClicked(view: View) {
        val phoneNumber = "tel:+1234567890"  // Replace with the desired phone number
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber))
        startActivity(intent)
    }

    // Handle SMS icon click
    fun onSmsClicked(view: View) {
        val smsUri = Uri.parse("smsto:+1234567890")  // Replace with the desired phone number
        val intent = Intent(Intent.ACTION_SENDTO, smsUri)
        startActivity(intent)
    }

    // Handle email icon click
    fun onEmailClicked(view: View) {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:example@example.com"))  // Replace with the desired email
        startActivity(Intent.createChooser(emailIntent, "Send Email"))
    }

    // Function to update birthdate, age, and anniversary
    private fun updateBirthdateAndAnniversary(birthdate: String) {
        // Get references to the TextViews
        val textViewBirthdate: TextView = findViewById(R.id.textViewBirthdate)
        val textViewAnniversaire: TextView = findViewById(R.id.textViewAnniversaire)

        // Format the birthdate to jj/mm/aaaa
        val formattedBirthdate = formatBirthdate(birthdate)

        // Calculate the age
        val age = calculateAge(birthdate)

        // Set the text for the birthdate, age, and anniversary
        textViewBirthdate.text = "Birthdate: $formattedBirthdate (Age: $age)"
        textViewAnniversaire.text = "Anniversaire"
    }

    // Function to format the birthdate to jj/mm/aaaa
    private fun formatBirthdate(birthdate: String): String {
        try {
            val sdfInput = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
            val date = sdfInput.parse(birthdate)

            val sdfOutput = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())  // Desired format
            return sdfOutput.format(date)
        } catch (e: Exception) {
            Log.e("CandidatDetailActivity", "Error formatting birthdate", e)
            return "Invalid Date"
        }
    }


    // Function to calculate the age from the birthdate
    private fun calculateAge(birthdate: String): Int {
        val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
        val birthDate = sdf.parse(birthdate)  // Parsing the birthdate string

        // Check if parsing was successful
        birthDate?.let {
            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)

            // Get birth year
            val birthCalendar = Calendar.getInstance()
            birthCalendar.time = birthDate
            val birthYear = birthCalendar.get(Calendar.YEAR)

            var age = currentYear - birthYear

            // If birthday hasn't occurred yet this year, subtract 1 from age
            if (calendar.get(Calendar.MONTH) < birthCalendar.get(Calendar.MONTH) ||
                (calendar.get(Calendar.MONTH) == birthCalendar.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) < birthCalendar.get(Calendar.DAY_OF_MONTH))) {
                age--
            }

            return age
        }

        // Return 0 if parsing failed (in case birthDate is null)
        return 0
    }


    // Function to update the salaries in the TextViews
    fun updateSalaries(salaryInEUR: Double) {
        // Get references to the TextViews
        val textViewSalaryEUR: TextView = findViewById(R.id.textViewSalaryEUR)
        val textViewSalaryGBP: TextView = findViewById(R.id.textViewSalaryGBP)

        // Convert Euro to Pounds
        val salaryInGBP = convertEuroToPounds(salaryInEUR)

        // Set the text for the salaries
        textViewSalaryEUR.text = "$salaryInEUR EUR"
        textViewSalaryGBP.text = "$salaryInGBP Pounds"
    }

    // Function to convert Euro to Pounds
    fun convertEuroToPounds(euroAmount: Double): Double {
        val exchangeRate = 0.86  // Example exchange rate: 1 Euro = 0.86 Pounds
        return euroAmount * exchangeRate
    }

    fun setNoteContent(noteTitle: String, noteContent: String) {
        // Get references to the TextViews
        val textViewNotesTitle: TextView = findViewById(R.id.textViewNotesTitle)
        val textViewNotesContent: TextView = findViewById(R.id.textViewNotesContent)

        // Set the text for the title and content
        textViewNotesTitle.text = noteTitle
        textViewNotesContent.text = noteContent
    }

}
