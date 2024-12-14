package com.example.p8vitesse.domain.usecase

import com.example.p8vitesse.data.repository.CandidatRepository
import com.example.p8vitesse.domain.model.Candidat
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetFavorisCandidatsUseCase @Inject constructor(
    private val candidatRepository: CandidatRepository
) {

    suspend fun execute(): List<Candidat> {
        return withContext(Dispatchers.IO) { // Switch to the IO thread
            try {
                // Fetch the list of favoris candidates from the repository
                candidatRepository.getFavoriteCandidats()
            } catch (e: Exception) {
                throw Exception("Error fetching favorites", e)
            }
        }
    }
}
