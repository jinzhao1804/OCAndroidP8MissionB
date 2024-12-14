package com.example.p8vitesse.ui.home.favoris

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.p8vitesse.R
import com.example.p8vitesse.ui.detail.CandidatDetailActivity
import com.example.p8vitesse.ui.home.all.AllListAdapter
import com.example.p8vitesse.ui.home.all.AllViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavorisFragment : Fragment() {
    private val viewModel: FavorisViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavorisListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favoris, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)


        // Initialize the adapter and set it to the RecyclerView
        adapter = FavorisListAdapter(emptyList()) { candidat ->
            // Handle the item click and pass the Candidat ID to the activity
            val intent = Intent(requireContext(), CandidatDetailActivity::class.java)
            intent.putExtra("CANDIDAT_ID", candidat.id.toString())  // Pass the Candidat's ID
            startActivity(intent)  // Start the detail activity

            Log.e("AppDatabase", "Candidat id put: ${candidat.id}")

        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter


        // Observe the candidates list from the ViewModel
        lifecycleScope.launch {
            viewModel.favCandidats.collect { candidats ->
                // Update the RecyclerView adapter with the new list of candidats
                Log.e("AppDatabase", "Updated candidats: $candidats")

                adapter.updateCandidats(candidats)
            }
        }

        // Trigger fetching of candidates
        viewModel.fetchFavCandidats()

        return view
    }
}
