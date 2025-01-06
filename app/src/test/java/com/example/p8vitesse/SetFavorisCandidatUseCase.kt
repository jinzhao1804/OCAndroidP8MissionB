package com.example.p8vitesse

import com.example.p8vitesse.data.repository.CandidatRepository
import com.example.p8vitesse.domain.usecase.SetFavorisCandidatUsecase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

class SetFavorisCandidatUsecaseTest {

    // Mock dependencies
    private lateinit var candidatRepository: CandidatRepository
    private lateinit var setFavorisCandidatUsecase: SetFavorisCandidatUsecase

    @Before
    fun setUp() {
        // Initialize the mock repository
        candidatRepository = mockk()
        // Initialize the use case with the mocked repository
        setFavorisCandidatUsecase = SetFavorisCandidatUsecase(candidatRepository)
        // Set the main coroutine dispatcher for testing
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `execute should call repository setFavoriteCandidat when successful`() = runTest {
        // Arrange
        val candidatId = 1
        val isFavorite = true

        // Mock the repository behavior
        coEvery { candidatRepository.setFavoriteCandidat(candidatId, isFavorite) } returns Unit

        // Act
        setFavorisCandidatUsecase.execute(candidatId, isFavorite)

        // Assert
        coVerify(exactly = 1) { candidatRepository.setFavoriteCandidat(candidatId, isFavorite) }
    }

    @Test(expected = Exception::class)
    fun `execute should throw exception when repository throws exception`() = runTest {
        // Arrange
        val candidatId = 1
        val isFavorite = true
        val exception = Exception("Database error")

        // Mock the repository to throw an exception
        coEvery { candidatRepository.setFavoriteCandidat(candidatId, isFavorite) } throws exception

            runBlocking {
                setFavorisCandidatUsecase.execute(candidatId, isFavorite)
            }
    }
}