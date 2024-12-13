package com.example.p8vitesse.ui.home.all

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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllFragment : Fragment() {
    private val viewModel: AllViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AllListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)

        // Initialize the adapter and set it to the RecyclerView
        adapter = AllListAdapter(emptyList())  // Initialize with an empty list
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter


        // Observe the candidates list from the ViewModel
        lifecycleScope.launch {
            viewModel.candidats.collect { candidats ->
                // Update the RecyclerView adapter with the new list of candidats
                Log.e("AppDatabase", "Updated candidats: $candidats")

                adapter.updateCandidats(candidats)
            }
        }

        // Trigger fetching of candidates
        viewModel.fetchCandidats()

        return view
    }
}