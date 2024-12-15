package com.example.p8vitesse.domain.usecase

import android.graphics.Bitmap
import com.example.p8vitesse.data.repository.CandidatRepository
import com.example.p8vitesse.domain.model.Candidat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class UpdateCandidatUseCase @Inject constructor(private val candidatRepository: CandidatRepository) {

    suspend fun execute(
        candidatId: Long?,
        name: String,
        surname: String,
        phone: String,
        email: String,
        birthdate: Date,
        desiredSalary: Double,
        note: String,
        isFav: Boolean,
        profilePicture: Bitmap?
    ) {
        // Create a Candidat object from the provided data
        val candidat = Candidat(
            id = candidatId,
            name = name,
            surname = surname,
            phone = phone,
            email = email,
            birthdate = birthdate,
            desiredSalary = desiredSalary,
            note = note,
            isFav = isFav,
            profilePicture = profilePicture
        )

        // Perform database operations on the IO dispatcher
        withContext(Dispatchers.IO) {
            try {
                // Update the candidat in the repository
                candidatRepository.updateCandidat(candidat)
            } catch (e: Exception) {
                throw Exception("Error updating candidat: ${e.message}", e)
            }
        }
    }
}
