package com.example.p8vitesse.domain.usecase

import com.example.p8vitesse.data.repository.CandidatRepository
import com.example.p8vitesse.domain.model.Candidat
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class AddCandidatUseCase @Inject constructor(private val candidatRepository: CandidatRepository) {

    // Execute the use case to add a new Candidat
    suspend fun execute(candidat: Candidat) {
        try {
            // Perform the operation (e.g., add candidat to the repository)
            candidatRepository.addCandidat(candidat)
        } catch (e: CancellationException) {
            // Rethrow CancellationException to allow coroutine cancellation to propagate
            throw e
        } catch (e: Exception) {
            // Handle other exceptions
            throw Exception("Error adding candidat: ${e.message}", e)
        }
    }
}