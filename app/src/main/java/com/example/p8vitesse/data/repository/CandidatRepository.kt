package com.example.p8vitesse.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.p8vitesse.data.dao.CandidatDtoDao
import com.example.p8vitesse.domain.model.Candidat
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CandidatRepository @Inject constructor(private val candidatDao: CandidatDtoDao) {


    suspend fun getAllCandidats(): List<Candidat> {

        var allCandidat = candidatDao.getAllCandidats()
            .map { Candidat.fromDto(it) } // Convert every DTO to Candidat

        return allCandidat

        Log.e("AllViewModel", "All candidates: $allCandidat")

    }


    suspend fun addCandidat(candidat: Candidat) {
        candidatDao.insertCandidat(candidat.toDto())
    }

    suspend fun deleteExercise(candidat: Candidat) {
        candidat.id?.let {
            candidatDao.deleteCandidatById(
                id = candidat.id,
            )
        }
    }

    suspend fun getFavoriteCandidats(): List<Candidat> {
        return candidatDao.getFavoriteCandidats()
            .map { Candidat.fromDto(it) }
    }

    suspend fun getCandidatById(candidatId: Int): Candidat {
        return candidatDao.getCandidatById(candidatId)
    }

    suspend fun setFavoriteCandidat(candidatId: Int, isFavorite: Boolean){
        candidatDao.setFavoriteCandidat(candidatId, isFavorite)  // Make sure to implement this method in your DAO
    }



}