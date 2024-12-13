package com.example.p8vitesse.ui.home.all

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p8vitesse.domain.model.Candidat
import com.example.p8vitesse.domain.usecase.GetAllCandidatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AllViewModel @Inject constructor(private val getAllCandidatsUseCase: GetAllCandidatsUseCase) : ViewModel() {

    private val _candidats = MutableStateFlow<List<Candidat>>(emptyList())
    val candidats: StateFlow<List<Candidat>> = _candidats.asStateFlow()

    fun fetchCandidats() {
        // Launch a coroutine within the viewModelScope to handle the async operation
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Fetch candidates using the use case
                val candidatsList = getAllCandidatsUseCase.execute()
                // Update the state flow with the fetched candidates
                _candidats.value = candidatsList
            } catch (e: Exception) {
                // Handle any exceptions (e.g., log it or show an error state)
                _candidats.value = emptyList() // or provide a default value or error state
            }
        }
    }
}
