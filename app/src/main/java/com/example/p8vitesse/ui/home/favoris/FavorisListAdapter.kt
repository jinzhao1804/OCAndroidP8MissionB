package com.example.p8vitesse.ui.home.favoris

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.p8vitesse.R
import com.example.p8vitesse.domain.model.Candidat

class FavorisListAdapter(
    private var candidats: MutableList<Candidat>, // Use MutableList to allow item modification
    private val onItemClick: (Candidat) -> Unit
) : RecyclerView.Adapter<FavorisListAdapter.CandidatViewHolder>() {

    // Update the entire candidates list
    fun updateCandidats(newCandidates: List<Candidat>) {
        val diffCallback = CandidatesDiffCallback(candidats, newCandidates)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        candidats.clear()
        candidats.addAll(newCandidates)
        diffResult.dispatchUpdatesTo(this)
    }

    // Update a single item in the list
    fun updateItem(position: Int, updatedItem: Candidat) {
        if (position in candidats.indices) {
            candidats[position] = updatedItem
            notifyItemChanged(position)
        }
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
        candidat.profilePicture?.let {
            holder.profilPictureView.setImageBitmap(it)
            // Scale the image to cover the entire width and height of the ImageView
            //holder.profilPictureView.scaleType = ImageView.ScaleType.FIT_XY
            // Alternatively, you can use ScaleType.CENTER_CROP if you want to crop the image to fit

            holder.profilPictureView.scaleType = ImageView.ScaleType.CENTER_CROP

        }

        // Set up the click listener for each item
        holder.itemView.setOnClickListener {
            onItemClick(candidat)  // Trigger the click callback with the selected candidat
        }
    }

    override fun getItemCount(): Int = candidats.size

    inner class CandidatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val surnameTextView: TextView = itemView.findViewById(R.id.surnameTextView)
        val profilPictureView: ImageView = itemView.findViewById(R.id.profilePictureView)
        val noteTextView: TextView = itemView.findViewById(R.id.noteTextView)
    }
}
