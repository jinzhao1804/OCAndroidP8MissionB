package com.example.p8vitesse.data.repository

import com.example.p8vitesse.data.dao.CandidatDtoDao
import com.example.p8vitesse.domain.model.Candidat
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CandidatRepository @Inject constructor(private val candidatDao: CandidatDtoDao) {


    suspend fun getAllCandidats(): List<Candidat> {
        return candidatDao.getAllCandidats()
            .map { Candidat.fromDto(it) } // Convert every DTO to Candidat
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

    suspend fun getFavoriteCandidates(): List<Candidat> {
        return candidatDao.getFavoriteCandidats()
            .map { Candidat.fromDto(it) }
    }

}