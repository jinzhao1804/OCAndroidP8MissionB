package com.example.p8vitesse.ui.home.all


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.p8vitesse.R
import com.example.p8vitesse.domain.model.Candidat

class AllListAdapter(private var candidats: List<Candidat>) : RecyclerView.Adapter<AllListAdapter.CandidatViewHolder>() {

    // Update the candidates list when new data is received
    fun updateCandidats(newCandidats: List<Candidat>) {
        Log.e("AppDatabase", "Updating with ${newCandidats.size} candidates")  // Add this log

        candidats = newCandidats
        notifyDataSetChanged()  // Notify the adapter that data has changed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_candidat, parent, false)
        return CandidatViewHolder(view)
    }

    override fun onBindViewHolder(holder: CandidatViewHolder, position: Int) {
        val candidat = candidats[position]
        holder.nameTextView.text = candidat.name
        holder.surnameTextView.text = candidat.surname
    }


    override fun getItemCount(): Int {
        Log.d("AppDatabase", "Item count: ${candidats.size}")  // Log the size of the list
        return candidats.size
    }

    inner class CandidatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val surnameTextView: TextView = itemView.findViewById(R.id.surnameTextView)
    }
}