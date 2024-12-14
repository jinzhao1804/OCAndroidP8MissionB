package com.example.p8vitesse.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p8vitesse.domain.model.Candidat
import com.example.p8vitesse.domain.usecase.GetCandidatByIdUseCase
import com.example.p8vitesse.domain.usecase.GetFavorisCandidatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel  // Ensure this annotation is added
class CandidatDetailViewModel @Inject constructor(
    private val getCandidatByIdUseCase: GetCandidatByIdUseCase,
    private val getFavorisCandidatsUseCase: GetFavorisCandidatsUseCase
) : ViewModel() {

    // StateFlow to hold the fetched candidat
    private val _candidat = MutableStateFlow<Candidat?>(null)
    val candidat: StateFlow<Candidat?> = _candidat.asStateFlow()

    // Function to fetch a candidat by ID
    fun getCandidatById(candidatId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Fetch the candidat using GetCandidatByIdUseCase
                val fetchedCandidat = getCandidatByIdUseCase.execute(candidatId)
                _candidat.value = fetchedCandidat

                Log.e("AppDatabase", "Candidat detail viewmodel: $fetchedCandidat")
            } catch (e: Exception) {
                Log.e("AppDatabase", "Error fetching candidat", e)
            }
        }
    }

    // Toggle the favorite status and refresh the screen
    fun refreshListOfFav(candidat: Candidat) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getFavorisCandidatsUseCase.execute()
                // Refresh the screen by fetching the updated candidat
                candidat.id?.let { getCandidatById(it.toInt()) } // Re-fetch the candidate to reflect the updated favorite status
            } catch (e: Exception) {
                Log.e("Favorite", "Error updating favorite status", e)
            }
        }
    }

}