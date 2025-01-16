package com.example.p8vitesse.ui.home.all

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.p8vitesse.R
import com.example.p8vitesse.databinding.FragmentAllBinding
import com.example.p8vitesse.domain.model.Candidat
import com.example.p8vitesse.ui.add.AddCandidatActivity
import com.example.p8vitesse.ui.add.AddCandidatViewModel
import com.example.p8vitesse.ui.detail.CandidatDetailActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllFragment : Fragment() {

// Using the constructor to directly set the layout

    private var _binding: FragmentAllBinding? = null  // Nullable binding reference
    private val binding get() = _binding!!  // Non-nullable reference to binding
    private val viewModel: AllViewModel by viewModels()

    private lateinit var adapter: AllListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllBinding.inflate(inflater, container, false)  // Inflate the layout using ViewBinding
        return binding.root  // Return the root view from the binding
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the adapter with a click listener
        adapter = AllListAdapter(emptyList()) { candidat ->
            candidat.id?.let { navigateToCandidatDetail(it) }
        }

        // Set up RecyclerView
        setupRecyclerView()

        // Show loading indicator and observe candidates
        observeCandidates()

        // Fetch candidates
        viewModel.fetchCandidats()

        // Set up FloatingActionButton
        setupFloatingActionButton()
    }

    // Helper function to navigate to CandidatDetailActivity
    private fun navigateToCandidatDetail(candidatId: Long) {
        val intent = Intent(requireContext(), CandidatDetailActivity::class.java).apply {
            putExtra("CANDIDAT_ID", candidatId.toString())
        }
        startActivity(intent)
        Log.e("AppDatabase", "Candidat id put: $candidatId")
    }

    // Helper function to set up RecyclerView
    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    // Helper function to observe candidates and update UI
    private fun observeCandidates() {
        showLoading(true) // Show loading indicator initially

        lifecycleScope.launch {
            viewModel.candidats.collect { candidats ->
                showLoading(false) // Hide loading indicator
                updateUI(candidats)
                adapter.updateCandidats(candidats) // Update adapter with the latest list
            }
        }
    }

    // Helper function to update UI based on candidates list
    private fun updateUI(candidats: List<Candidat>) {
        if (candidats.isEmpty()) {
            showLoading(true)
            binding.noCandidateText.visibility = View.VISIBLE // Show "No candidate" message
            binding.recyclerView.visibility = View.GONE // Hide RecyclerView
        } else {
            showLoading(false)
            binding.noCandidateText.visibility = View.GONE // Hide "No candidate" message
            binding.recyclerView.visibility = View.VISIBLE // Show RecyclerView
        }
    }

    // Helper function to set up FloatingActionButton
    private fun setupFloatingActionButton() {
        binding.squareFab.setOnClickListener {
            val intent = Intent(requireContext(), AddCandidatActivity::class.java)
            addCandidatLauncher.launch(intent) // Launch AddCandidatActivity
        }
    }

    // Helper function to show or hide loading indicator
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    private val addCandidatLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Refresh the list of candidats
                viewModel.fetchCandidats()
            }
        }

    // Method to filter candidates based on search query
    fun filterCandidates(query: String) {
        val filteredList = viewModel.candidats.value.filter { candidat ->
                    candidat.name.contains(query, ignoreCase = true) ||  // Search in name
                    candidat.surname.contains(query, ignoreCase = true) ||  // Search in surname
                    candidat.note.contains(query, ignoreCase = true)  // Search in note
        }
        adapter.updateCandidats(filteredList)
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Avoid memory leaks by setting the binding to null
    }
}
