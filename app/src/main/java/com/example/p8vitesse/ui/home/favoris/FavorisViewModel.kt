package com.example.p8vitesse.ui.home.favoris

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p8vitesse.domain.model.Candidat
import com.example.p8vitesse.domain.usecase.GetFavorisCandidatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavorisViewModel @Inject constructor(
    private val getFavCandidatsUseCase: GetFavorisCandidatsUseCase
) : ViewModel() {

    // StateFlow to hold the list of favorite candidates
    val _favCandidats = MutableStateFlow<List<Candidat>>(emptyList())
    val favCandidats: StateFlow<List<Candidat>> = _favCandidats.asStateFlow()

    // StateFlow to hold error messages
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        // Fetch favorite candidates when the ViewModel is created
        fetchFavCandidats()
    }

    // Function to fetch favorite candidates
    fun fetchFavCandidats() {
        viewModelScope.launch {
            getFavCandidatsUseCase.execute()
                .catch { exception ->
                    // Handle errors
                    _errorMessage.value = "Error fetching favorites: ${exception.message}"
                    Log.e("FavorisViewModel", "Error fetching favorites", exception)
                }
                .collect { favCandidats ->
                    // Update the StateFlow with the new list of favorites
                    _favCandidats.value = favCandidats
                }
        }
    }
}