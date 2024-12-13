package com.example.p8vitesse.ui.add

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.p8vitesse.R

class AddCandidatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_candidat)  // Replace with your correct layout name

        // Find the toolbar by its ID
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        // Set the toolbar as the app's action bar
        setSupportActionBar(toolbar)

        // Enable the "up" button (back button) on the toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set the title of the activity
        supportActionBar?.title = "Activity Title"
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
}