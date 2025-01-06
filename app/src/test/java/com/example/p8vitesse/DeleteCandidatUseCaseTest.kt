package com.example.p8vitesse

import com.example.p8vitesse.data.repository.CandidatRepository
import com.example.p8vitesse.domain.model.Candidat
import com.example.p8vitesse.domain.usecase.DeleteCandidatUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.util.Date

class DeleteCandidatUseCaseTest {

    // Mock dependencies
    private lateinit var candidatRepository: CandidatRepository
    private lateinit var deleteCandidatUseCase: DeleteCandidatUseCase

    @Before
    fun setUp() {
        // Initialize the mock repository
        candidatRepository = mockk()
        // Initialize the use case with the mocked repository
        deleteCandidatUseCase = DeleteCandidatUseCase(candidatRepository)
    }

    @Test
    fun `execute should call repository deleteCandidat`() = runTest {
        // Arrange
        val candidat = Candidat(
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
        )

        // Mock the repository behavior
        coEvery { candidatRepository.deleteCandidat(candidat) } returns Unit

        // Act
        deleteCandidatUseCase.execute(candidat)

        // Assert
        coVerify(exactly = 1) { candidatRepository.deleteCandidat(candidat) }
    }

    @Test(expected = Exception::class) // Use the expected parameter
    fun `execute should throw exception when repository throws exception`() {
        // Arrange
        val candidat = Candidat(
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
        )

        val exception = Exception("Database error")

        // Mock the repository to throw an exception
        coEvery { candidatRepository.deleteCandidat(candidat) } throws exception

        // Act (the test will pass if this throws an Exception)
        runBlocking {
            deleteCandidatUseCase.execute(candidat)
        }
    }
}