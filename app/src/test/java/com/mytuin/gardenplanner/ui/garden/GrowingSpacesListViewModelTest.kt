package com.mytuin.gardenplanner.ui.garden

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveGrowingSpacesInGarden
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeGrowingSpaceRepository
import com.mytuin.gardenplanner.ui.navigation.Routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GrowingSpacesListViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val gardenId = "garden_test_0001"
    private lateinit var repository: FakeGrowingSpaceRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeGrowingSpaceRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel(): GrowingSpacesListViewModel {
        val handle = SavedStateHandle(mapOf(Routes.GROWING_SPACES_ARG_GARDEN_ID to gardenId))
        return GrowingSpacesListViewModel(handle, ObserveGrowingSpacesInGarden(repository))
    }

    @Test
    fun uiState_emits_NoSpaces_when_repository_is_empty() =
        runTest(testDispatcher) {
            val vm = buildViewModel()
            vm.uiState.test {
                assertEquals(GrowingSpacesListUiState.Loading, awaitItem())
                assertEquals(GrowingSpacesListUiState.NoSpaces, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun uiState_emits_Ready_with_spaces() =
        runTest(testDispatcher) {
            repository.insert(sampleSpace("growingspace_1", "Bed 1"))
            repository.insert(sampleSpace("growingspace_2", "Bed 2"))
            val vm = buildViewModel()

            vm.uiState.test {
                assertEquals(GrowingSpacesListUiState.Loading, awaitItem())
                advanceUntilIdle()
                val ready = awaitItem() as GrowingSpacesListUiState.Ready
                assertEquals(listOf("Bed 1", "Bed 2"), ready.spaces.map { it.name })
                cancelAndIgnoreRemainingEvents()
            }
        }

    private fun sampleSpace(
        id: String,
        name: String,
    ): GrowingSpace =
        GrowingSpace(
            id = id,
            gardenId = gardenId,
            name = name,
            spaceType = GrowingSpaceType.RAISED_BED,
            status = RecordStatus.ACTIVE,
            geometry = null,
            lengthMetres = null,
            widthMetres = null,
            heightMetres = null,
            diameterMetres = null,
            areaSquareMetres = null,
            volumeCubicMetres = null,
            description = null,
            areaId = null,
            notes = null,
            createdAt = 1_700_000_000_000L,
            updatedAt = 1_700_000_000_000L,
        )
}
