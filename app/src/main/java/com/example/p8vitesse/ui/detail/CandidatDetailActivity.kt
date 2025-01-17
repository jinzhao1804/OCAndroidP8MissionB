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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.p8vitesse.domain.model.Candidat
import com.example.p8vitesse.domain.usecase.GetFavorisCandidatsUseCase
import com.example.p8vitesse.domain.usecase.SetFavorisCandidatUsecase
import com.example.p8vitesse.ui.edit.EditActivity
import com.example.p8vitesse.ui.home.favoris.FavorisListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject
import androidx.fragment.app.FragmentManager
import com.example.p8vitesse.R


@AndroidEntryPoint
class CandidatDetailActivity : AppCompatActivity() {

    private val candidatViewModel: CandidatDetailViewModel by viewModels()
    @Inject
    lateinit var setFavorisCandidatUsecase: SetFavorisCandidatUsecase

    @Inject
    lateinit var getFavorisCandidatUsecase: GetFavorisCandidatsUseCase

    lateinit var favorisListAdapter: FavorisListAdapter

    private var favoriteIcon: MenuItem? = null // Change to nullable
    private val REQUEST_CODE_EDIT = 1001  // Define a unique request code for EditActivity

    lateinit var candidatId: String


    // Handle result from EditActivity when it finishes
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_EDIT && resultCode == RESULT_OK) {
            // If result is OK (data was updated in EditActivity), refresh the data
            refreshData()
        }
    }

    // Function to refresh or reload the data in the fragment
    private fun refreshData() {
        // Your logic to refresh data, e.g., making a network call or reloading from the database
        Log.e("AppDatabase", "Data refreshed")
        candidatViewModel.getCandidatById(candidatId.toInt())

        lifecycleScope.launch {
            candidatViewModel.candidat.collect { updatedCandidat ->
                updatedCandidat?.let {
                    it.id?.let { id -> fetchAndDisplayCandidatDetails(id.toInt()) }
                }
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidat_detail)

        val candidatId = intent.getStringExtra("CANDIDAT_ID")

        if (candidatId != null && candidatId.toInt() != -1) {
            fetchAndDisplayCandidatDetails(candidatId.toInt())
        }

        favorisListAdapter = FavorisListAdapter(mutableListOf()) { candidat ->
            // Handle item click here
            // Example: Log or perform an action when an item is clicked
            Log.d("FavorisListAdapter", "Clicked on: ${candidat.name}")
        }

        // Observe the favoris list changes
        lifecycleScope.launch {
            candidatViewModel.favorisList.collect { updatedFavorisList ->
                // This block will be triggered whenever the favoris list is updated
                refreshFavorisList(updatedFavorisList)
            }
        }

        candidatId?.let { candidatViewModel.getCandidatById(it.toInt()) }

        lifecycleScope.launch {
            candidatViewModel.candidat.collect { updatedCandidat ->
                updatedCandidat?.let {
                    it.id?.let { id -> fetchAndDisplayCandidatDetails(id.toInt()) }
                }
            }
        }

        // Initialize the favorite button based on the menu
        favoriteIcon?.isVisible = true  // Ensure the icon is visible when the menu is ready
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

                    // Update favorite icon
                    updateFavoriteIcon(candidat.isFav)
                    setOnClickListeners(candidat)
                    updateSalaries(candidat.desiredSalary)
                    setNoteContent("Notes", candidat.note)
                    updateBirthdateAndAnniversary(candidat.birthdate.toString())
                    setProfileImage(candidat)
                }
            }
        }
    }

    private fun setProfileImage(candidat: Candidat?) {
        val profileImageView = findViewById<ImageView>(R.id.profilePicImageView)

        // Check if ImageView is null
        if (profileImageView == null) {
            Log.e("CandidatDetailActivity", "ImageView with ID 'profilePicImageView' is null. Please check the layout.")
            return
        }

        // Check if the candidate has a profile picture
        if (candidat?.profilePicture != null) {
            // Set the image bitmap
            profileImageView.setImageBitmap(candidat.profilePicture)

            // Ensure the image is scaled correctly to maintain aspect ratio
            profileImageView.scaleType = ImageView.ScaleType.CENTER_CROP
        } else {
            // Set a default image if no profile picture is available
            profileImageView.setImageResource(R.drawable.ic_add)
        }
    }




    private fun refreshFavorisList(favorisList: List<Candidat>) {
        favorisListAdapter.updateCandidats(favorisList)
        favorisListAdapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        favoriteIcon = menu?.findItem(R.id.action_favorite)

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

    private fun deleteCandidat(candidat: Candidat) {
        // Show a confirmation dialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Confirm Deletion")
            .setMessage("Are you sure you want to delete ${candidat.name}?")
            .setPositiveButton("Yes") { _, _ ->
                // Proceed with deletion
                candidatViewModel.deleteCandidat(candidat)

                // Observe for successful deletion and close the activity
                candidatViewModel.deletionStatus.observe(this) { status ->
                    if (status) {
                        Toast.makeText(this, "${candidat.name} deleted successfully", Toast.LENGTH_SHORT).show()
                        finish() // Close the activity and return to the previous screen
                        candidatViewModel.fetchAllCandidtas()
                        //refreshListScreen() // complete this code here
                    } else {
                        Toast.makeText(this, "Failed to delete ${candidat.name}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss() // Cancel the operation
            }
            .create()

        dialog.show()
    }

    override fun onBackPressed() {

        super.onBackPressed()

        finish()  // Close the current activity
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // Trigger back navigation
        finish()
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {

                finish()
                true
            }
            R.id.action_favorite -> {
                val candidat = candidatViewModel.candidat.value
                candidat?.let {
                    // Toggle the favorite status using the viewModel
                    candidatViewModel.toggleFavorite(it)
                    // Update the favorite icon in the menu
                    updateFavoriteIcon(it.isFav)
                }
                // Clear the back stack before navigating
                supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                true
            }
            R.id.action_delete -> {
                val candidat = candidatViewModel.candidat.value
                candidat?.let {
                    deleteCandidat(candidat)
                }

                true
            }
            R.id.action_edit -> {

                val candidat = candidatViewModel.candidat.value
                candidat?.let {
                    val intent = Intent(this, EditActivity::class.java)

                    // Optionally, you can pass data to the EditActivity using intent extras
                    intent.putExtra("CANDIDAT_ID", candidat.id)  // Example of passing the Candidat ID

                        Log.e("AppDatabase", "candidat to edit ${candidat.id}")
                    // Start the EditActivity
                    startActivity(intent)
                    finish()
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
        }
    }

    fun onSmsClicked(candidat: Candidat) {
        val smsNumber = candidat.phone
        if (!smsNumber.isNullOrEmpty()) {
            val smsUri = Uri.parse("smsto:$smsNumber")
            val intent = Intent(Intent.ACTION_SENDTO, smsUri)
            startActivity(intent)
        }
    }

    fun onEmailClicked(candidat: Candidat) {
        val email = candidat.email
        if (!email.isNullOrEmpty()) {
            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
            startActivity(Intent.createChooser(emailIntent, "Send Email"))
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

