package com.example.p8vitesse.ui.detail

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.p8vitesse.R
import com.example.p8vitesse.domain.model.Candidat
import com.example.p8vitesse.domain.usecase.GetFavorisCandidatsUseCase
import com.example.p8vitesse.domain.usecase.SetFavorisCandidatUsecase
import com.example.p8vitesse.ui.home.favoris.FavorisListAdapter
import com.example.p8vitesse.ui.home.favoris.FavorisViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
@AndroidEntryPoint
class CandidatDetailActivity : AppCompatActivity() {

    private val candidatViewModel: CandidatDetailViewModel by viewModels()
    @Inject
    lateinit var setFavorisCandidatUsecase: SetFavorisCandidatUsecase

    @Inject
    lateinit var getFavorisCandidatUsecase: GetFavorisCandidatsUseCase

    lateinit var favorisListAdapter: FavorisListAdapter
    private val favorisViewModel: FavorisViewModel by viewModels()

    private var favoriteIcon: MenuItem? = null // Change to nullable

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidat_detail)

        Log.e("AppDatabase", "Intent data: ${intent.getIntExtra("CANDIDAT_ID", -1)}")

        val candidatId = intent.getStringExtra("CANDIDAT_ID")
        Log.e("AppDatabase", "CandidatId fetched id is : $candidatId")

        if (candidatId != null && candidatId.toInt() != -1) {
            fetchAndDisplayCandidatDetails(candidatId.toInt())
        }

       // Initialize the favorisListAdapter
        favorisListAdapter = FavorisListAdapter(emptyList()) {}

        // Observe favoris list changes
        observeFavorisList()

        candidatId?.let { candidatViewModel.getCandidatById(it.toInt()) }

        lifecycleScope.launch {
            candidatViewModel.candidat.collect { updatedCandidat ->
                updatedCandidat?.let {
                    it.id?.let { id -> fetchAndDisplayCandidatDetails(id.toInt()) }
                }
            }
        }

        lifecycleScope.launch {
            favorisViewModel.favCandidats.collect { updatedFavorisList ->
                refreshFavorisList(updatedFavorisList)
            }
        }
    }

    private fun observeFavorisList() {
        lifecycleScope.launch {
            favorisViewModel.favCandidats.collect { favorisList ->
                // Update the UI with the new favoris list
                refreshFavorisList(favorisList)
            }
        }
    }

    private fun fetchAndDisplayCandidatDetails(candidatId: Int) {
        candidatViewModel.getCandidatById(candidatId)

        lifecycleScope.launch {
            candidatViewModel.candidat.collect { candidat ->
                candidat?.let {
                    val toolbar: Toolbar = findViewById(R.id.toolbar)
                    setSupportActionBar(toolbar)
                    supportActionBar?.title = "${candidat.name} ${candidat.surname.toUpperCase()}"
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)

                    // Initialize the favorite button based on the menu
                    favoriteIcon?.isVisible = true  // Ensure the icon is visible when the menu is ready
                    updateFavoriteIcon(candidat.isFav)
                    setOnClickListeners(candidat)
                    updateSalaries(candidat.desiredSalary)
                    setNoteContent("Notes",candidat.note)
                }
            }
        }
    }

    private fun refreshFavorisList(favorisList: List<Candidat>) {
        Log.d("Favoris", "Updated favoris list: $favorisList")
        favorisViewModel.fetchFavCandidats()
        // Update the adapter data with the new favoris list
        favorisListAdapter.updateCandidats(favorisList)
        // Notify the adapter to refresh the list (if needed)
        favorisListAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        favoriteIcon = menu?.findItem(R.id.action_favorite) // Initialize here

        val candidat = candidatViewModel.candidat.value
        if (candidat != null) {
            updateFavoriteIcon(candidat.isFav)
        }

        return true
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        favoriteIcon?.let {
            if (isFavorite) {
                it.setIcon(R.drawable.ic_star_filled)
            } else {
                it.setIcon(R.drawable.ic_star)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_favorite -> {
                val candidat = candidatViewModel.candidat.value
                candidat?.let {
                    candidatViewModel.toggleFavorite(it)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun setOnClickListeners(candidat: Candidat) {
        findViewById<View>(R.id.icon_call).setOnClickListener { onCallClicked(candidat) }
        findViewById<View>(R.id.icon_sms).setOnClickListener { onSmsClicked(candidat) }
        findViewById<View>(R.id.icon_email).setOnClickListener { onEmailClicked(candidat) }
    }

    fun onCallClicked(candidat: Candidat) {
        val phoneNumber = candidat.phone
        if (!phoneNumber.isNullOrEmpty()) {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
            startActivity(intent)
        } else {
            Log.e("CandidatDetailActivity", "Phone number is not available.")
        }
    }

    fun onSmsClicked(candidat: Candidat) {
        val smsNumber = candidat.phone
        if (!smsNumber.isNullOrEmpty()) {
            val smsUri = Uri.parse("smsto:$smsNumber")
            val intent = Intent(Intent.ACTION_SENDTO, smsUri)
            startActivity(intent)
        } else {
            Log.e("CandidatDetailActivity", "SMS number is not available.")
        }
    }

    fun onEmailClicked(candidat: Candidat) {
        val email = candidat.email
        if (!email.isNullOrEmpty()) {
            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
            startActivity(Intent.createChooser(emailIntent, "Send Email"))
        } else {
            Log.e("CandidatDetailActivity", "Email is not available.")
        }
    }

    private fun updateBirthdateAndAnniversary(birthdate: String) {
        val textViewBirthdate: TextView = findViewById(R.id.textViewBirthdate)
        val textViewAnniversaire: TextView = findViewById(R.id.textViewAnniversaire)

        val formattedBirthdate = formatBirthdate(birthdate)
        val age = calculateAge(birthdate)

        textViewBirthdate.text = "Birthdate: $formattedBirthdate (Age: $age)"
        textViewAnniversaire.text = "Anniversaire"
    }

    private fun formatBirthdate(birthdate: String): String {
        try {
            val sdfInput = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
            val date = sdfInput.parse(birthdate)

            val sdfOutput = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return sdfOutput.format(date)
        } catch (e: Exception) {
            Log.e("CandidatDetailActivity", "Error formatting birthdate", e)
            return "Invalid Date"
        }
    }

    private fun calculateAge(birthdate: String): Int {
        val sdf = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
        val birthDate = sdf.parse(birthdate)

        birthDate?.let {
            val calendar = Calendar.getInstance()
            val currentYear = calendar.get(Calendar.YEAR)

            val birthCalendar = Calendar.getInstance()
            birthCalendar.time = birthDate
            val birthYear = birthCalendar.get(Calendar.YEAR)

            var age = currentYear - birthYear

            if (calendar.get(Calendar.MONTH) < birthCalendar.get(Calendar.MONTH) ||
                (calendar.get(Calendar.MONTH) == birthCalendar.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) < birthCalendar.get(Calendar.DAY_OF_MONTH))) {
                age--
            }

            return age
        }

        return 0
    }

    fun updateSalaries(salaryInEUR: Double) {
        val textViewSalaryEUR: TextView = findViewById(R.id.textViewSalaryEUR)
        val textViewSalaryGBP: TextView = findViewById(R.id.textViewSalaryGBP)

        val salaryInGBP = convertEuroToPounds(salaryInEUR)

        textViewSalaryEUR.text = "$salaryInEUR EUR"
        textViewSalaryGBP.text = "$salaryInGBP Pounds"
    }

    fun convertEuroToPounds(euroAmount: Double): Double {
        val exchangeRate = 0.86
        return euroAmount * exchangeRate
    }

    fun setNoteContent(noteTitle: String, noteContent: String) {
        val textViewNotesTitle: TextView = findViewById(R.id.textViewNotesTitle)
        val textViewNotesContent: TextView = findViewById(R.id.textViewNotesContent)

        textViewNotesTitle.text = noteTitle
        textViewNotesContent.text = noteContent
    }
}
