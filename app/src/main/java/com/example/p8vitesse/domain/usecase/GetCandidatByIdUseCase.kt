package com.example.p8vitesse.domain.usecase

import com.example.p8vitesse.data.repository.CandidatRepository
import com.example.p8vitesse.domain.model.Candidat
import javax.inject.Inject

class GetCandidatByIdUseCase @Inject constructor(private val candidatRepository: CandidatRepository) {

    // Execute the use case to get a Candidat by ID
    suspend fun execute(candidatId: Int): Candidat? {
        return try {
            // Return the Candidat retrieved from the repository
            candidatRepository.getCandidatById(candidatId)
        } catch (e: Exception) {
            // Handle the error (e.g., log it or rethrow as a custom exception)
            throw Exception("Error fetching candidat: ${e.message}", e)
        }
    }
}
