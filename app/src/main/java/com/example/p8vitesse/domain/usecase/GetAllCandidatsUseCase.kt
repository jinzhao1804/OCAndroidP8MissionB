package com.example.p8vitesse.domain.usecase

import com.example.p8vitesse.data.repository.CandidatRepository
import com.example.p8vitesse.domain.model.Candidat
import javax.inject.Inject

class GetAllCandidatsUseCase @Inject constructor(private val candidatRepository: CandidatRepository) {

    suspend fun execute(): List<Candidat> {
        return try {
            // Try to fetch all exercises from the repository
            candidatRepository.getAllCandidats()
        } catch (e: Exception) {
            // Handle the error (e.g., log it or rethrow as a custom exception)
            throw Exception("Error fetching exercises: ${e.message}", e)
        }
    }
}