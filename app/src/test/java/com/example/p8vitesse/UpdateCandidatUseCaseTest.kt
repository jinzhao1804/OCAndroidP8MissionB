package com.example.p8vitesse

import android.graphics.Bitmap
import android.util.Log
import com.example.p8vitesse.data.repository.CandidatRepository
import com.example.p8vitesse.domain.model.Candidat
import com.example.p8vitesse.domain.usecase.UpdateCandidatUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import java.util.Date

class UpdateCandidatUseCaseTest {

    // Mock dependencies
    private lateinit var candidatRepository: CandidatRepository
    private lateinit var updateCandidatUseCase: UpdateCandidatUseCase

    @Before
    fun setUp() {
        // Initialize the mock repository
        candidatRepository = mockk()
        // Initialize the use case with the mocked repository
        updateCandidatUseCase = UpdateCandidatUseCase(candidatRepository)
        // Set the main coroutine dispatcher for testing
        Dispatchers.setMain(Dispatchers.Unconfined)
    }


    @Test
    fun `execute should call repository updateCandidat when successful`() = runTest {
        // Mock the Log class
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0 // Mock Log.e to do nothing

        // Arrange
        val candidatId = 1L
        val name = "John"
        val surname = "Doe"
        val phone = "1234567890"
        val email = "john.doe@example.com"
        val birthdate = Date()
        val desiredSalary = 50000.0
        val note = "Sample note"
        val isFav = true
        val profilePicture: Bitmap? = mockk()

        // Capture the Candidat object passed to the repository
        val candidatSlot = slot<Candidat>()

        // Mock the repository behavior
        coEvery { candidatRepository.updateCandidat(capture(candidatSlot)) } returns Unit

        // Act
        updateCandidatUseCase.execute(
            candidatId = candidatId,
            name = name,
            surname = surname,
            phone = phone,
            email = email,
            birthdate = birthdate,
            desiredSalary = desiredSalary,
            note = note,
            isFav = isFav,
            profilePicture = profilePicture
        )

        // Assert
        coVerify(exactly = 1) { candidatRepository.updateCandidat(candidatSlot.captured) }

        // Verify the captured Candidat object
        assertEquals(candidatId, candidatSlot.captured.id)
        assertEquals(name, candidatSlot.captured.name)
        assertEquals(surname, candidatSlot.captured.surname)
        assertEquals(phone, candidatSlot.captured.phone)
        assertEquals(email, candidatSlot.captured.email)
        assertEquals(birthdate, candidatSlot.captured.birthdate)
        assertEquals(desiredSalary, candidatSlot.captured.desiredSalary, 0.0)
        assertEquals(note, candidatSlot.captured.note)
        assertEquals(isFav, candidatSlot.captured.isFav)
        assertEquals(profilePicture, candidatSlot.captured.profilePicture)
    }
    @Test(expected = Exception::class)
    fun `execute should throw exception when repository throws exception`() = runTest {
        // Arrange
        val candidatId = 1L
        val name = "John"
        val surname = "Doe"
        val phone = "1234567890"
        val email = "john.doe@example.com"
        val birthdate = Date()
        val desiredSalary = 50000.0
        val note = "Sample note"
        val isFav = true
        val profilePicture: Bitmap? = mockk()

        val exception = Exception("Database error")

        // Mock the repository to throw an exception
        coEvery {
            candidatRepository.updateCandidat(
                Candidat(
                    id = candidatId,
                    name = name,
                    surname = surname,
                    phone = phone,
                    email = email,
                    birthdate = birthdate,
                    desiredSalary = desiredSalary,
                    note = note,
                    isFav = isFav,
                    profilePicture = profilePicture
                )
            )
        } throws exception

            runBlocking {
                updateCandidatUseCase.execute(
                    candidatId = candidatId,
                    name = name,
                    surname = surname,
                    phone = phone,
                    email = email,
                    birthdate = birthdate,
                    desiredSalary = desiredSalary,
                    note = note,
                    isFav = isFav,
                    profilePicture = profilePicture
                )


        }
 }
}