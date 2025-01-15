package com.example.p8vitesse.ui.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p8vitesse.domain.model.Candidat
import com.example.p8vitesse.domain.usecase.AddCandidatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel  // Ensure this annotation is added
class AddCandidatViewModel @Inject constructor(
    private val addCandidatUseCase: AddCandidatUseCase
) : ViewModel() {

    // StateFlow to hold the list of candidats
    private val _candidats = MutableStateFlow<List<Candidat>>(emptyList())
    val candidats: StateFlow<List<Candidat>> = _candidats.asStateFlow()

    private val _candidat = MutableStateFlow<Candidat?>(null)
    val candidat: StateFlow<Candidat?> = _candidat

    // Function to add a new candidat
    fun addCandidat(candidat: Candidat) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                addCandidatUseCase.execute(candidat)
                // Update the StateFlow with the newly added candidat
                _candidat.value = candidat

                Log.e("AppDatabase", "Candidat added successfully")
            } catch (e: Exception) {
                Log.e("AppDatabase", "Error adding candidat", e)
            }
        }
    }
}
