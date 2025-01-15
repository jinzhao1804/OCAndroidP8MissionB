package com.example.p8vitesse.ui.home.all


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.p8vitesse.R
import com.example.p8vitesse.domain.model.Candidat

class AllListAdapter(private var candidats: List<Candidat>, private val onItemClick: (Candidat) -> Unit) : RecyclerView.Adapter<AllListAdapter.CandidatViewHolder>() {


    // Update the candidates list when new data is received
    fun updateCandidats(newCandidates: List<Candidat>) {
        val diffCallback = CandidatesDiffCallback(candidats, newCandidates)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        candidats = newCandidates
        diffResult.dispatchUpdatesTo(this)
        notifyDataSetChanged()

    }


    class CandidatesDiffCallback(
        private val oldList: List<Candidat>,
        private val newList: List<Candidat>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size

        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_candidat, parent, false)
        return CandidatViewHolder(view)
    }

    override fun onBindViewHolder(holder: CandidatViewHolder, position: Int) {
        val candidat = candidats[position]
        holder.nameTextView.text = candidat.name
        holder.surnameTextView.text = candidat.surname
        holder.noteTextView.text = candidat.note

        // Set the profile picture Bitmap to the ImageView
        if (candidat.profilePicture != null) {
            holder.profilPictureView.setImageBitmap(candidat.profilePicture)
        } else {
            // Reset the ImageView if there is no profile picture
            holder.profilPictureView.setImageResource(R.drawable.ic_add) // Use a default placeholder image
        }

        // Set up the click listener for each item
        holder.itemView.setOnClickListener {
            onItemClick(candidat)  // Trigger the click callback with the selected candidat
        }
    }
    override fun getItemCount(): Int {
        return candidats.size
    }

    inner class CandidatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val surnameTextView: TextView = itemView.findViewById(R.id.surnameTextView)
        val profilPictureView: ImageView = itemView.findViewById(R.id.profilePictureView)
        val noteTextView: TextView = itemView.findViewById(R.id.noteTextView)
    }
}
