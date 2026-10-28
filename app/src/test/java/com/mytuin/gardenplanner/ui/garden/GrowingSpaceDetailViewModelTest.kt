package com.mytuin.gardenplanner.ui.garden

import androidx.lifecycle.SavedStateHandle
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.usecases.garden.ArchiveGrowingSpace
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveGrowingSpace
import com.mytuin.gardenplanner.domain.usecases.garden.RestoreGrowingSpace
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
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
        val clock = FakeClock(nowMillisValue = 1_700_000_500_000L)
        val handle = SavedStateHandle(mapOf(Routes.GROWING_SPACE_DETAIL_ARG_ID to spaceId))
        return GrowingSpaceDetailViewModel(
            savedStateHandle = handle,
            observeGrowingSpace = ObserveGrowingSpace(repository),
            archiveGrowingSpace = ArchiveGrowingSpace(repository, clock),
            restoreGrowingSpace = RestoreGrowingSpace(repository, clock),
        )
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

    @Test
    fun archive_updates_status_and_emits_finished_event() =
        runTest(testDispatcher) {
            repository.insert(sampleSpace())
            val vm = buildViewModel()
            advanceUntilIdle()

            vm.archive()
            advanceUntilIdle()

            assertEquals(
                GrowingSpaceDetailUiState.FinishedEvent.ARCHIVED,
                vm.uiState.value.finishedEvent,
            )
            assertEquals(RecordStatus.ARCHIVED, repository.snapshot().single().status)
        }

    @Test
    fun restore_updates_status_and_emits_finished_event() =
        runTest(testDispatcher) {
            repository.insert(sampleSpace().copy(status = RecordStatus.ARCHIVED))
            val vm = buildViewModel()
            advanceUntilIdle()

            vm.restore()
            advanceUntilIdle()

            assertEquals(
                GrowingSpaceDetailUiState.FinishedEvent.RESTORED,
                vm.uiState.value.finishedEvent,
            )
            assertEquals(RecordStatus.ACTIVE, repository.snapshot().single().status)
        }

    @Test
    fun onFinishedHandled_clears_finishedEvent() =
        runTest(testDispatcher) {
            repository.insert(sampleSpace())
            val vm = buildViewModel()
            advanceUntilIdle()

            vm.archive()
            advanceUntilIdle()
            assertTrue(vm.uiState.value.finishedEvent != null)

            vm.onFinishedHandled()
            assertNull(vm.uiState.value.finishedEvent)
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
