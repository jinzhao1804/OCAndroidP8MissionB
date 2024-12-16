package com.example.p8vitesse.ui.edit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.p8vitesse.domain.model.Candidat
import com.example.p8vitesse.domain.usecase.GetCandidatByIdUseCase
import com.example.p8vitesse.domain.usecase.UpdateCandidatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel  // Ensure this annotation is added
class EditViewModel @Inject constructor(
    private val updateCandidatUseCase: UpdateCandidatUseCase,
    private val getCandidatByIdUseCase:GetCandidatByIdUseCase
) : ViewModel() {

    // Mutable state flow to hold the updated candidat list or status
    private val _updatedCandidat = MutableStateFlow<Candidat?>(null)  // Holds the updated Candidat
    val updatedCandidat: StateFlow<Candidat?> = _updatedCandidat.asStateFlow()  // Expose it as immutable

    // MutableStateFlow for the current Candidat data
    private val _candidat = MutableStateFlow<Candidat?>(null)
    val candidat: StateFlow<Candidat?> = _candidat


    fun getCandidatById(candidatId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Collect the Flow emitted by the use case
                getCandidatByIdUseCase.execute(candidatId)?.collect { fetchedCandidat ->
                    // Directly update the _candidat value on the same thread (since this is in viewModelScope)
                    _candidat.value = fetchedCandidat
                }
            } catch (e: Exception) {
                Log.e("Error", "Error fetching candidat by id", e)
            }
        }
    }


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
