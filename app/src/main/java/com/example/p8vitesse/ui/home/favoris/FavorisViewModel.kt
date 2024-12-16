package com.example.p8vitesse.ui.home.favoris

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
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
class FavorisViewModel @Inject constructor(
    private val getFavCandidatsUseCase: GetFavorisCandidatsUseCase
) : ViewModel() {

    private val _favCandidats = MutableStateFlow<List<Candidat>>(emptyList())
    val favCandidats: StateFlow<List<Candidat>> = _favCandidats.asStateFlow()

    init {
        viewModelScope.launch {
            getFavCandidatsUseCase.execute().collect { favCandidats ->
                _favCandidats.value = favCandidats
            }
        }
    }

    fun fetchFavCandidats() {
        viewModelScope.launch {

                getFavCandidatsUseCase.execute().collect { favCandidats ->
                    _favCandidats.value = favCandidats
                }
            }
    }
}

