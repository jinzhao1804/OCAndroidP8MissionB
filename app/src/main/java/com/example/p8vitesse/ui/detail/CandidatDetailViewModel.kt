package com.example.p8vitesse.ui.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.p8vitesse.domain.model.Candidat
import com.example.p8vitesse.domain.usecase.DeleteCandidatUseCase
import com.example.p8vitesse.domain.usecase.GetAllCandidatsUseCase
import com.example.p8vitesse.domain.usecase.GetCandidatByIdUseCase
import com.example.p8vitesse.domain.usecase.GetFavorisCandidatsUseCase
import com.example.p8vitesse.domain.usecase.SetFavorisCandidatUsecase
import com.example.p8vitesse.ui.home.favoris.FavorisViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CandidatDetailViewModel @Inject constructor(
    private val getCandidatByIdUseCase: GetCandidatByIdUseCase, // Use case to fetch candidat by ID
    private val setFavorisCandidatUsecase: SetFavorisCandidatUsecase,
    private val getFavorisCandidatUsecase: GetFavorisCandidatsUseCase,
    private val deleteCandidatUseCase: DeleteCandidatUseCase,
    private val getAllCandidatsUseCase: GetAllCandidatsUseCase
) : ViewModel() {

    private val _candidat = MutableStateFlow<Candidat?>(null)  // For single Candidat
    val candidat: StateFlow<Candidat?> = _candidat.asStateFlow()

    private val _favorisList = MutableStateFlow<List<Candidat>>(emptyList())  // For list of Candidats
    val favorisList: StateFlow<List<Candidat>> get() = _favorisList

    private val _deletionStatus = MutableLiveData<Boolean>()
    val deletionStatus: LiveData<Boolean> get() = _deletionStatus

    private val _allCandidats = MutableStateFlow<List<Candidat>>(emptyList())
    val allCandidats: StateFlow<List<Candidat>> get() = _allCandidats

    init {
        fetchFavCandidats()  // Initial fetch of the favoris list
    }

    fun fetchAllCandidtas() {
        viewModelScope.launch {
            try {
                getAllCandidatsUseCase.execute()
                    .collect { list ->
                        _allCandidats.value = list // Update the StateFlow with the collected list
                    }
            } catch (e: Exception) {
                Log.e("Error", "Error fetching candidats list", e)
            }
        }
    }

    fun deleteCandidat(candidat: Candidat){
        viewModelScope.launch {
            try {
                deleteCandidatUseCase.execute(candidat)
                _deletionStatus.value = true // Notify success

            } catch (e: Exception) {
                Log.e("Error", "Error fetching favoris list", e)
                _deletionStatus.value = false // Notify failure

            }
        }
    }

    // Fetch the list of favoris (favorite candidates)
    fun fetchFavCandidats() {
        viewModelScope.launch {
            try {
                getFavorisCandidatUsecase.execute()
                    .collect { list ->
                        _favorisList.value = list // Update the LiveData or StateFlow
                    }
            } catch (e: Exception) {
                Log.e("Error", "Error fetching favoris list", e)
            }
        }
    }


    fun getCandidatById(candidatId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Collect the Flow emitted by the use case
                getCandidatByIdUseCase.execute(candidatId)?.collect { fetchedCandidat ->
                    // Assign the fetched Candidat to the StateFlow
                    _candidat.value = fetchedCandidat
                }
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
