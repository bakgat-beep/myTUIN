package com.mytuin.gardenplanner.ui.garden

import androidx.lifecycle.SavedStateHandle
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveGrowingSpace
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
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GrowingSpaceDetailViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val spaceId = "growingspace_test_0001"
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

    private fun buildViewModel(): GrowingSpaceDetailViewModel {
        val handle = SavedStateHandle(mapOf(Routes.GROWING_SPACE_DETAIL_ARG_ID to spaceId))
        return GrowingSpaceDetailViewModel(handle, ObserveGrowingSpace(repository))
    }

    @Test
    fun loads_space_and_emits_ready_state() =
        runTest(testDispatcher) {
            repository.insert(sampleSpace())
            val vm = buildViewModel()
            advanceUntilIdle()

            val state = vm.uiState.value
            assertEquals(false, state.isLoading)
            assertEquals(false, state.loadError)
            assertEquals("Bed 2", state.space?.name)
        }

    @Test
    fun null_space_sets_load_error() =
        runTest(testDispatcher) {
            val vm = buildViewModel()
            advanceUntilIdle()

            assertEquals(false, vm.uiState.value.isLoading)
            assertTrue(vm.uiState.value.loadError)
            assertNull(vm.uiState.value.space)
        }

    private fun sampleSpace(): GrowingSpace =
        GrowingSpace(
            id = spaceId,
            gardenId = gardenId,
            name = "Bed 2",
            spaceType = GrowingSpaceType.RAISED_BED,
            status = RecordStatus.ACTIVE,
            geometry = null,
            lengthMetres = 3.0,
            widthMetres = 1.2,
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
