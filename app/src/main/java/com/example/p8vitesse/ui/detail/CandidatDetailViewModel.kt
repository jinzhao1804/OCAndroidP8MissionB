package com.example.p8vitesse.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import com.example.p8vitesse.domain.model.Candidat
import com.example.p8vitesse.domain.usecase.GetCandidatByIdUseCase
import com.example.p8vitesse.domain.usecase.GetFavorisCandidatsUseCase
import com.example.p8vitesse.domain.usecase.SetFavorisCandidatUsecase
import com.example.p8vitesse.ui.home.favoris.FavorisViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CandidatDetailViewModel @Inject constructor(
    private val getCandidatByIdUseCase: GetCandidatByIdUseCase, // Use case to fetch candidat by ID
    private val setFavorisCandidatUsecase: SetFavorisCandidatUsecase,
    private val getFavorisCandidatUsecase: GetFavorisCandidatsUseCase
) : ViewModel() {

    private val _candidat = MutableStateFlow<Candidat?>(null)  // For single Candidat
    val candidat: StateFlow<Candidat?> = _candidat.asStateFlow()

    private val _favorisList = MutableStateFlow<List<Candidat>>(emptyList())  // For list of Candidats
    val favorisList: StateFlow<List<Candidat>> get() = _favorisList

    init {
        fetchFavCandidats()  // Initial fetch of the favoris list
    }

    // Fetch the list of favoris (favorite candidates)
    fun fetchFavCandidats() {
        viewModelScope.launch {
            try {
                val list = getFavorisCandidatUsecase.execute()
                _favorisList.value = list
            } catch (e: Exception) {
                Log.e("Error", "Error fetching favoris list", e)
            }
        }
    }

    // Fetch a candidat by ID
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
                // Toggle the favorite status
                val updatedCandidat = candidat.copy(isFav = !candidat.isFav)

                updatedCandidat.id?.let {
                    setFavorisCandidatUsecase.execute(it.toInt(), updatedCandidat.isFav)
                }

                // Update the local candidat object immediately
                _candidat.value = updatedCandidat

                // Fetch the updated favoris list (if needed)
                fetchFavCandidats()

            } catch (e: Exception) {
                Log.e("Error", "Error toggling favorite", e)
            }
        }
    }
}
