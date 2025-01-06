package com.example.p8vitesse.domain.usecase

import com.example.p8vitesse.data.repository.CandidatRepository
import com.example.p8vitesse.domain.model.Candidat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.util.Date

class GetFavorisCandidatsUseCaseTest {

    // Mock dependencies
    private lateinit var candidatRepository: CandidatRepository
    private lateinit var getFavorisCandidatsUseCase: GetFavorisCandidatsUseCase

    @Before
    fun setUp() {
        // Initialize the mock repository
        candidatRepository = mockk()
        // Initialize the use case with the mocked repository
        getFavorisCandidatsUseCase = GetFavorisCandidatsUseCase(candidatRepository)
        // Set the main coroutine dispatcher for testing
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `execute should return flow of favorite candidats when repository succeeds`() = runTest {
        // Arrange
        val favoriteCandidats = listOf(
            Candidat(
                id = 1,
                name = "John",
                surname = "Doe",
                phone = "1234567890",
                email = "john.doe@example.com",
                birthdate = Date(),
                desiredSalary = 50000.0,
                note = "Sample note",
                isFav = true,
                profilePicture = null
            ),
            Candidat(
                id = 2,
                name = "Jane",
                surname = "Doe",
                phone = "0987654321",
                email = "jane.doe@example.com",
                birthdate = Date(),
                desiredSalary = 60000.0,
                note = "Another note",
                isFav = true,
                profilePicture = null
            )
        )

        // Mock the repository behavior
        coEvery { candidatRepository.getFavoriteCandidats() } returns flowOf(favoriteCandidats)

        // Act
        val resultFlow = getFavorisCandidatsUseCase.execute()

        // Assert
        val resultList = resultFlow.toList()
        assertEquals(1, resultList.size) // Flow emits a single list
        assertEquals(favoriteCandidats, resultList[0]) // Verify the emitted list matches the expected favorite candidats
    }

    @Test(expected = Exception::class)
    fun `execute should throw exception when repository throws exception`() = runTest {
        // Arrange
        val exception = Exception("Database error")

        // Mock the repository to throw an exception
        coEvery { candidatRepository.getFavoriteCandidats() } throws exception


        runBlocking {
            getFavorisCandidatsUseCase.execute().toList() // Trigger the flow to throw the exception

        }

    }
}