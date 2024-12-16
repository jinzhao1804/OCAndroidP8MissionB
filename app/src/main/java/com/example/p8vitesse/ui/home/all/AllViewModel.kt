package com.example.p8vitesse.ui.home.all

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p8vitesse.domain.model.Candidat
import com.example.p8vitesse.domain.usecase.GetAllCandidatsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllViewModel @Inject constructor(private val getAllCandidatsUseCase: GetAllCandidatsUseCase) : ViewModel() {

    private val _candidats = MutableStateFlow<List<Candidat>>(emptyList())
    val candidats: StateFlow<List<Candidat>> get() = _candidats

    fun fetchCandidats() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Collect the flow emitted by getAllCandidatsUseCase.execute()
                getAllCandidatsUseCase.execute().collect { candidatsList ->
                    // Log to see if we are receiving the correct data
                    Log.e("AppDatabase", "Fetched candidates: $candidatsList")
                    // Update the state flow with the fetched candidates
                    _candidats.value = candidatsList
                }
            } catch (e: Exception) {
                Log.e("AppDatabase", "Error fetching candidates", e)
                _candidats.value = emptyList() // Handle error by clearing the list
            }
        }
    }
}
