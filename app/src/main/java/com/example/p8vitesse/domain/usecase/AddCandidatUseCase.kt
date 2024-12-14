package com.example.p8vitesse.domain.usecase

import com.example.p8vitesse.data.repository.CandidatRepository
import com.example.p8vitesse.domain.model.Candidat
import javax.inject.Inject

class AddCandidatUseCase @Inject constructor(private val candidatRepository: CandidatRepository) {

    // Execute the use case to add a new Candidat
    suspend fun execute(candidat: Candidat){
        return try {
            // Add the new Candidat using the repository, then fetch the updated list
            candidatRepository.addCandidat(candidat)  // Save the Candidat
        } catch (e: Exception) {
            // Handle the error (e.g., log it or rethrow as a custom exception)
            throw Exception("Error adding candidat: ${e.message}", e)
        }
    }
}
