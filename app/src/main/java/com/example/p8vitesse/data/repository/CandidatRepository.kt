package com.example.p8vitesse.data.repository

import android.graphics.Bitmap
import android.util.Log
import com.example.p8vitesse.data.converter.Converter
import com.example.p8vitesse.data.dao.CandidatDtoDao
import com.example.p8vitesse.domain.model.Candidat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class CandidatRepository @Inject constructor(private val candidatDao: CandidatDtoDao) {


    suspend fun getAllCandidats(): Flow<List<Candidat>> {
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

    suspend fun getFavoriteCandidats(): Flow<List<Candidat>> {
        return candidatDao.getFavorisCandidats()

    }

    suspend fun getCandidatById(candidatId: Int): Candidat {
        return candidatDao.getCandidatById(candidatId)
    }

    suspend fun setFavoriteCandidat(candidatId: Int, isFavorite: Boolean){
        candidatDao.setFavoriteCandidat(candidatId, isFavorite)  // Make sure to implement this method in your DAO
    }

    suspend fun updateCandidat(
        candidat: Candidat
    ) {
        candidat.id?.let { id ->
            // Convert the Bitmap to String here before passing it to DAO
            val profilePictureString = Converter().fromBitmap(candidat.profilePicture)
            candidatDao.updateCandidat(
                id = id,
                name = candidat.name,
                surname = candidat.surname,
                phone = candidat.phone,
                email = candidat.email,
                birthdate = candidat.birthdate,
                desiredSalary = candidat.desiredSalary,
                note = candidat.note,
                isFav = candidat.isFav,
                profilePicture = profilePictureString.toString() // Pass the converted String
            )
        }
    }

}