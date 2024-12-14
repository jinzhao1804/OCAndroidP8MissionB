package com.example.p8vitesse.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.p8vitesse.domain.model.Candidat
import com.example.p8vitesse.domain.usecase.GetCandidatByIdUseCase
import com.example.p8vitesse.domain.usecase.GetFavorisCandidatsUseCase
import com.example.p8vitesse.domain.usecase.SetFavorisCandidatUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CandidatDetailViewModel @Inject constructor(
    private val getCandidatByIdUseCase: GetCandidatByIdUseCase,
    private val setFavorisCandidatUsecase: SetFavorisCandidatUsecase,
    private val getFavorisCandidatUsecase: GetFavorisCandidatsUseCase
) : ViewModel() {

    private val _candidat = MutableStateFlow<Candidat?>(null)  // For single Candidat
    val candidat: StateFlow<Candidat?> = _candidat.asStateFlow()

    private val _favorisList = MutableStateFlow<List<Candidat>>(emptyList())  // For list of Candidats
    val favorisList: StateFlow<List<Candidat>> = _favorisList.asStateFlow()

    // Function to fetch a candidat by ID
    fun getCandidatById(candidatId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fetchedCandidat = getCandidatByIdUseCase.execute(candidatId)
                _candidat.value = fetchedCandidat  // Correctly assign a single Candidat
            } catch (e: Exception) {
                Log.e("Error", "Error fetching candidat by id", e)
            }
        }
    }

    // Toggle the favorite status and refresh the list and screen
    fun toggleFavorite(candidat: Candidat) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Update the favorite status
                val updatedCandidat = candidat.copy(isFav = !candidat.isFav)
                updatedCandidat.id?.let {
                    setFavorisCandidatUsecase.execute(it.toInt(), updatedCandidat.isFav)
                }

                // After updating, refresh the list and screen
                _candidat.value = updatedCandidat  // Immediately update the screen with the new Candidat
                refreshFavoriteList()  // Refresh the favoris list
            } catch (e: Exception) {
                Log.e("Error", "Error toggling favorite", e)
            }
        }
    }

    // Refresh the list of favorite candidates
    private fun refreshFavoriteList() {
        viewModelScope.launch {
            try {
                val updatedFavoris = getFavorisCandidatUsecase.execute()
                _favorisList.value = updatedFavoris  // Assign the List<Candidat> directly to _favorisList
            } catch (e: Exception) {
                Log.e("Error", "Error fetching favoris list", e)
            }
        }
    }
}
