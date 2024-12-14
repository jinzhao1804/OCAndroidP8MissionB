package com.example.p8vitesse.domain.usecase

import com.example.p8vitesse.data.repository.CandidatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SetFavorisCandidatUsecase @Inject constructor(private val candidatRepository: CandidatRepository) {

    suspend fun execute(candidatId: Int, isFavorite: Boolean) {
        // Perform database operations on the IO dispatcher
        withContext(Dispatchers.IO) {
            try {
                // Assuming this updates the database
                candidatRepository.setFavoriteCandidat(candidatId, isFavorite)
            } catch (e: Exception) {
                throw Exception("Error setting favorite: ${e.message}", e)
            }
        }
    }
}
