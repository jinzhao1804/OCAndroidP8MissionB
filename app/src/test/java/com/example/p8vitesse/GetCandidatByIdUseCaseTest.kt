package com.example.p8vitesse

import com.example.p8vitesse.data.repository.CandidatRepository
import com.example.p8vitesse.domain.model.Candidat
import com.example.p8vitesse.domain.usecase.GetCandidatByIdUseCase
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

class GetCandidatByIdUseCaseTest {

    // Mock dependencies
    private lateinit var candidatRepository: CandidatRepository
    private lateinit var getCandidatByIdUseCase: GetCandidatByIdUseCase

    @Before
    fun setUp() {
        // Initialize the mock repository
        candidatRepository = mockk()
        // Initialize the use case with the mocked repository
        getCandidatByIdUseCase = GetCandidatByIdUseCase(candidatRepository)
    }

    @Test
    fun `execute should return flow of candidat when repository succeeds`() = runTest {
        // Arrange
        val candidatId = 1
        val candidat = Candidat(
            id = candidatId.toLong(),
            name = "John",
            surname = "Doe",
            phone = "1234567890",
            email = "john.doe@example.com",
            birthdate = Date(),
            desiredSalary = 50000.0,
            note = "Sample note",
            isFav = false,
            profilePicture = null
        )

        // Mock the repository behavior
        coEvery { candidatRepository.getCandidatById(candidatId) } returns flowOf(candidat)

        // Act
        val resultFlow = getCandidatByIdUseCase.execute(candidatId)

        // Assert
        val resultList = resultFlow?.toList()
        assertEquals(1, resultList?.size) // Flow emits a single candidat
        assertEquals(candidat, resultList?.get(0)) // Verify the emitted candidat matches the expected candidat
    }

    @Test(expected = Exception::class) // Use the expected parameter
    fun `execute should throw exception when repository throws exception`() = runTest {
        // Arrange
        val candidatId = 1
        val exception = Exception("Database error")

        // Mock the repository to throw an exception
        coEvery { candidatRepository.getCandidatById(candidatId) } throws exception

        runBlocking {
                getCandidatByIdUseCase.execute(candidatId)
        }
    }
}