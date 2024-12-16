package com.example.p8vitesse.data.repository

import android.graphics.Bitmap
import android.util.Log
import com.example.p8vitesse.data.converter.Converter
import com.example.p8vitesse.data.dao.CandidatDtoDao
import com.example.p8vitesse.domain.model.Candidat
import kotlinx.coroutines.flow.Flow
import java.util.Date
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