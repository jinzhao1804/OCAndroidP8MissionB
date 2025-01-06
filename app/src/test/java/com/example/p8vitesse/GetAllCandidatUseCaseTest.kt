package com.example.p8vitesse

import com.example.p8vitesse.data.repository.CandidatRepository
import com.example.p8vitesse.domain.model.Candidat
import com.example.p8vitesse.domain.usecase.GetAllCandidatsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.util.Date

class GetAllCandidatsUseCaseTest {

    // Mock dependencies
    private lateinit var candidatRepository: CandidatRepository
    private lateinit var getAllCandidatsUseCase: GetAllCandidatsUseCase

    @Before
    fun setUp() {
        // Initialize the mock repository
        candidatRepository = mockk()
        // Initialize the use case with the mocked repository
        getAllCandidatsUseCase = GetAllCandidatsUseCase(candidatRepository)
    }

    @Test
    fun `execute should return flow of candidats when repository succeeds`() = runTest {
        // Arrange
        val candidats = listOf(
            Candidat(
                id = 1,
                name = "John",
                surname = "Doe",
                phone = "1234567890",
                email = "john.doe@example.com",
                birthdate = Date(),
                desiredSalary = 50000.0,
                note = "Sample note",
                isFav = false,
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
        coEvery { candidatRepository.getAllCandidats() } returns flowOf(candidats)

        // Act
        val resultFlow = getAllCandidatsUseCase.execute()

        // Assert
        val resultList = resultFlow.toList()
        assertEquals(1, resultList.size) // Flow emits a single list
        assertEquals(candidats, resultList[0]) // Verify the emitted list matches the expected candidats
    }

    @Test(expected = Exception::class) // Use the expected parameter
    fun `execute should throw exception when repository throws exception`() = runTest {
        // Arrange
        val exception = Exception("Database error")

        // Mock the repository to throw an exception
        coEvery { candidatRepository.getAllCandidats() } throws exception

        runBlocking {
                getAllCandidatsUseCase.execute()
        }

    }
}