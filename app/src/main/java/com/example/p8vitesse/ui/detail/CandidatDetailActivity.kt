package com.example.p8vitesse.ui.detail

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.p8vitesse.R

class CandidatDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidat_detail)  // Set the layout for the detail activity

        // Get the Candidat ID passed from the AllFragment
        val candidatId = intent.getIntExtra("CANDIDAT_ID", -1)  // Default value is -1 if no ID is passed

        // You can use the Candidat ID to fetch more details from the ViewModel or database
        if (candidatId != -1) {
            // Fetch and display the Candidat details using the ID
            // For example, call the ViewModel to fetch details for this ID
        } else {
            // Handle the case when no valid ID is passed
        }
    }
}
