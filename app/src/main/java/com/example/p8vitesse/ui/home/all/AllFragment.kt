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
import com.example.p8vitesse.ui.add.AddCandidatActivity
import com.example.p8vitesse.ui.add.AddCandidatViewModel
import com.example.p8vitesse.ui.detail.CandidatDetailActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllFragment : Fragment(R.layout.fragment_all) {

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
        // Initialize the adapter with a click listener
        adapter = AllListAdapter(emptyList()) { candidat ->
            // Handle the item click and pass the Candidat ID to the activity
            val intent = Intent(requireContext(), CandidatDetailActivity::class.java)
            intent.putExtra("CANDIDAT_ID", candidat.id.toString())  // Pass the Candidat's ID
            startActivity(intent)  // Start the detail activity



            Log.e("AppDatabase", "Candidat id put: ${candidat.id}")


        }


        // Set the RecyclerView's layout manager and adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // Observe the candidates list from the ViewModel
        lifecycleScope.launch {
            viewModel.candidats.collect { candidats ->
                adapter.updateCandidats(candidats)  // Update the adapter with the latest list
            }
        }

        // Trigger fetching of candidates
        viewModel.fetchCandidats()

        // Set up FloatingActionButton to open AddCandidatActivity
        binding.squareFab.setOnClickListener {
            val intent = Intent(requireContext(), AddCandidatActivity::class.java)
            addCandidatLauncher.launch(intent)  // Launch the AddCandidatActivity
        }
    }



    private val addCandidatLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Refresh the list of candidats
                viewModel.fetchCandidats()
            }
        }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // Avoid memory leaks by setting the binding to null
    }
}
