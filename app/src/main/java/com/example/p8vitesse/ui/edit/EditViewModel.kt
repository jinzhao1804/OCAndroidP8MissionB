package com.example.p8vitesse.ui.edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p8vitesse.domain.model.Candidat
import com.example.p8vitesse.domain.usecase.UpdateCandidatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel  // Ensure this annotation is added
class EditViewModel @Inject constructor(
    private val updateCandidatUseCase: UpdateCandidatUseCase
) : ViewModel() {

    // Mutable state flow to hold the updated candidat list or status
    private val _updatedCandidat = MutableStateFlow<Candidat?>(null)  // Holds the updated Candidat
    val updatedCandidat: StateFlow<Candidat?> = _updatedCandidat.asStateFlow()  // Expose it as immutable

    // Function to update a candidat
    fun updateCandidat(candidat: Candidat) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Execute the use case to update the candidat
                updateCandidatUseCase.execute(
                    candidat.id,
                    candidat.name,
                    candidat.surname,
                    candidat.phone,
                    candidat.email,
                    candidat.birthdate,
                    candidat.desiredSalary,
                    candidat.note,
                    candidat.isFav,
                    candidat.profilePicture
                )

                // After successful update, update the state flow
                _updatedCandidat.value = candidat

                Log.e("AppDatabase", "Candidat updated successfully")
            } catch (e: Exception) {
                Log.e("AppDatabase", "Error updating candidat", e)
            }
        }
    }
}
