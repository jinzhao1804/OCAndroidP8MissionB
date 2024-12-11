package com.example.p8vitesse.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.p8vitesse.R

class AllFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        // Set up RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = AllListAdapter(getDummyData())
        return view
    }

    private fun getDummyData(): List<String> {
        return listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
    }
}
