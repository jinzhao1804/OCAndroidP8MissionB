package com.example.p8vitesse.ui.home.favoris

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p8vitesse.domain.model.Candidat
import com.example.p8vitesse.domain.usecase.GetAllCandidatsUseCase
import com.example.p8vitesse.domain.usecase.GetFavorisCandidatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FavorisViewModel @Inject constructor(private val getFavCandidatsUseCase: GetFavorisCandidatsUseCase) : ViewModel() {

    private val _favCandidats = MutableStateFlow<List<Candidat>>(emptyList())
    val favCandidats: StateFlow<List<Candidat>> = _favCandidats.asStateFlow()

    fun fetchFavCandidats() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Fetch candidates using the use case
                val favCandidatsList = getFavCandidatsUseCase.execute()
                // Log to see if we are receiving the correct data
                Log.e("AppDatabase", "Fetched candidates: $favCandidatsList")
                // Update the state flow with the fetched candidates
                _favCandidats.value = favCandidatsList
            } catch (e: Exception) {
                Log.e("AppDatabase", "Error fetching candidates", e)
                _favCandidats.value = emptyList() // Handle error
            }
        }
    }
}
