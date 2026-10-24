package com.mytuin.gardenplanner.ui.home

import app.cash.turbine.test
import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveGardens
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeGardenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun uiState_emits_NoGarden_when_repository_is_empty() =
        runTest(testDispatcher) {
            val repository = FakeGardenRepository()
            val viewModel = HomeViewModel(ObserveGardens(repository))

            viewModel.uiState.test {
                assertEquals(HomeUiState.Loading, awaitItem())
                assertEquals(HomeUiState.NoGarden, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun uiState_emits_Ready_with_first_garden_when_repository_has_one() =
        runTest(testDispatcher) {
            val repository = FakeGardenRepository()
            val garden = sampleGarden()
            repository.insert(garden)
            val viewModel = HomeViewModel(ObserveGardens(repository))

            viewModel.uiState.test {
                assertEquals(HomeUiState.Loading, awaitItem())
                assertEquals(HomeUiState.Ready(garden), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun uiState_emits_Ready_with_alphabetically_first_garden_when_multiple_exist() =
        runTest(testDispatcher) {
            val repository = FakeGardenRepository()
            val zeta = sampleGarden().copy(id = "garden_z", name = "Zeta")
            val alpha = sampleGarden().copy(id = "garden_a", name = "Alpha")
            repository.insert(zeta)
            repository.insert(alpha)
            val viewModel = HomeViewModel(ObserveGardens(repository))

            viewModel.uiState.test {
                assertEquals(HomeUiState.Loading, awaitItem())
                val ready = awaitItem()
                assertEquals(HomeUiState.Ready(alpha), ready)
                cancelAndIgnoreRemainingEvents()
            }
        }

    private fun sampleGarden(): Garden =
        Garden(
            id = "garden_test_0001",
            name = "Back Garden",
            description = null,
            countryCode = null,
            region = null,
            locality = null,
            latitude = null,
            longitude = null,
            timezone = null,
            hemisphere = Hemisphere.UNKNOWN,
            status = RecordStatus.DRAFT,
            createdAt = 1_700_000_000_000L,
            updatedAt = 1_700_000_000_000L,
        )
}
