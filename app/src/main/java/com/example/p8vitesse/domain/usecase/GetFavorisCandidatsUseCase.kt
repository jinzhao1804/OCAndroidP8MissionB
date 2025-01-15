package com.example.p8vitesse.domain.usecase

import com.example.p8vitesse.data.repository.CandidatRepository
import com.example.p8vitesse.domain.model.Candidat
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.util.concurrent.Flow

class GetFavorisCandidatsUseCase @Inject constructor(
    private val candidatRepository: CandidatRepository
) {

     fun execute() : kotlinx.coroutines.flow.Flow<List<Candidat>> {
        return flow {
            try {
                // Emit the list of favoris candidates fetched from the repository
                emitAll(candidatRepository.getFavoriteCandidats())
            } catch (e: Exception) {
                throw Exception("Error fetching favorites", e)
            }
        }.flowOn(Dispatchers.IO) // Ensure the flow operates on the IO thread
    }
}

