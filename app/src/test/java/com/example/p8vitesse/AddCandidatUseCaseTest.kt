package com.example.p8vitesse

import com.example.p8vitesse.data.repository.CandidatRepository
import com.example.p8vitesse.domain.model.Candidat
import com.example.p8vitesse.domain.usecase.AddCandidatUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.Date

class AddCandidatUseCaseTest {

    // Mock dependencies
    private lateinit var candidatRepository: CandidatRepository
    private lateinit var addCandidatUseCase: AddCandidatUseCase

    @Before
    fun setUp() {
        // Initialize the mock repository
        candidatRepository = mockk()
        // Initialize the use case with the mocked repository
        addCandidatUseCase = AddCandidatUseCase(candidatRepository)
    }

    @Test
    fun `execute should call repository addCandidat`() = runTest {
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
        // coEvery : C'est une fonction de MockK utilisée pour définir le
        // comportement d'une fonction suspend (coroutine) lors d'un test.
        coEvery { candidatRepository.addCandidat(candidat) } returns Unit

        // Act
        addCandidatUseCase.execute(candidat)

        // Assert
        coVerify(exactly = 1) { candidatRepository.addCandidat(candidat) }
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
        coEvery { candidatRepository.addCandidat(candidat) } throws exception

        // Act (the test will pass if this throws an Exception)
        // runBlocking démarre un contexte de coroutine.
        //La méthode execute(candidat) est appelée à l'intérieur de ce contexte.
        //Le thread actuel est bloqué jusqu'à ce que execute(candidat) termine son exécution.
        //Une fois terminé, le contexte de coroutine est fermé et le programme continue.
        runBlocking {
            addCandidatUseCase.execute(candidat)
        }
    }
}