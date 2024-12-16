package com.example.p8vitesse

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.p8vitesse.domain.model.Candidat
import com.example.p8vitesse.domain.usecase.GetAllCandidatsUseCase
import com.example.p8vitesse.ui.home.all.AllViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class AllViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() // Ensures LiveData/StateFlow updates run on the main thread

    private val testDispatcher = StandardTestDispatcher() // Modern coroutine test dispatcher
    private lateinit var getAllCandidatsUseCase: GetAllCandidatsUseCase
    private lateinit var viewModel: AllViewModel

    @Before
    fun setUp() {
        // Mock the GetAllCandidatsUseCase
        getAllCandidatsUseCase = mockk()
        viewModel = AllViewModel(getAllCandidatsUseCase)

        // Set the test dispatcher as the main dispatcher
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher after each test
    }

    @Test
    fun `test fetchCandidats success`() = runTest {
        // Arrange: Mock the use case to return a list of candidates
        val dateString = "Mon Dec 16 12:43:49 GMT+01:00 2024"
        val formatter = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
        val parsedDate = formatter.parse(dateString) // Parse the date string into a Date object

        val mockCandidats = listOf(
            Candidat(
                id = 1,
                name = "Jack",
                surname = "Poudadada",
                phone = "78",
                email = "a@a.com",
                birthdate = parsedDate,
                desiredSalary = 111.4,
                note = "something here",
                isFav = false,
                profilePicture = null
            ),
            Candidat(
                id = 2,
                name = "Smith",
                surname = "Jane",
                phone = "78",
                email = "s@s.c",
                birthdate = parsedDate,
                desiredSalary = 234.4,
                note = "something",
                isFav = false,
                profilePicture = null
            )
        )

        coEvery { getAllCandidatsUseCase.execute() } returns flowOf(mockCandidats)

        // Act: Call fetchCandidats()
        viewModel.fetchCandidats()

        // Assert: Collect the StateFlow and verify the result
        val actualCandidats = mutableListOf<List<Candidat>>()
        val job = launch {
            viewModel.candidats.toList(actualCandidats) // Collect emitted values into a list
        }

        advanceUntilIdle() // Ensure all coroutines complete

        // Verify the collected value matches the expected mock data
        assertEquals(mockCandidats, actualCandidats.last())

        // Verify that the use case was called
        coVerify { getAllCandidatsUseCase.execute() }

        job.cancel() // Cancel the collection job to avoid hanging tests
    }




    @Test
    fun `test fetchCandidats error`() = runTest {
        // Arrange: Mock the use case to throw an exception
        coEvery { getAllCandidatsUseCase.execute() } throws Exception("Error fetching candidates")

        // Act: Call fetchCandidats()
        viewModel.fetchCandidats()

        // Assert: Check that the state flow is empty (since there was an error)
        viewModel.candidats.collect { candidatsList ->
            assertTrue(candidatsList.isEmpty())
        }

        // Verify that the use case was called
        coVerify { getAllCandidatsUseCase.execute() }
    }
}
