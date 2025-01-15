package com.example.p8vitesse.data.repository

import android.util.Log
import com.example.p8vitesse.data.converter.Converter
import com.example.p8vitesse.data.dao.CandidatDtoDao
import com.example.p8vitesse.domain.model.Candidat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

open class CandidatRepository @Inject constructor(private val candidatDao: CandidatDtoDao) {


     fun getAllCandidats(): Flow<List<Candidat>> {
        // This is a Flow of DTOs being transformed
        val allCandidat = candidatDao.getAllCandidats()
            .map { candidatesDto ->
                candidatesDto.map { Candidat.fromDto(it) } // Convert each DTO to Candidat
            }

        Log.e("AllViewModel", "All candidates: $allCandidat")

        // Return the Flow after transformation
        return allCandidat
    }



    suspend fun addCandidat(candidat: Candidat) {
        candidatDao.insertCandidat(candidat.toDto())
    }

    suspend fun deleteCandidat(candidat: Candidat) {
        candidat.id?.let {
            candidatDao.deleteCandidatById(
                id = candidat.id,
            )
        }
    }

     fun getFavoriteCandidats(): Flow<List<Candidat>> {
        return candidatDao.getFavorisCandidats()

    }

     fun getCandidatById(candidatId: Int): Flow<Candidat> {
        return candidatDao.getCandidatById(candidatId)
    }

    fun setFavoriteCandidat(candidatId: Int, isFavorite: Boolean){
        candidatDao.setFavoriteCandidat(candidatId, isFavorite)  // Make sure to implement this method in your DAO
    }

    suspend fun updateCandidat(candidat: Candidat) {
        // Convert Bitmap to String before passing it to DAO
        val profilePictureString = Converter().fromBitmap(candidat.profilePicture)
        candidatDao.updateCandidat(
            id = candidat.id,
            name = candidat.name,
            surname = candidat.surname,
            phone = candidat.phone,
            email = candidat.email,
            birthdate = candidat.birthdate,
            desiredSalary = candidat.desiredSalary,
            note = candidat.note,
            isFav = candidat.isFav,
            profilePicture = profilePictureString.toString() // Pass the converted string
        )
    }

}