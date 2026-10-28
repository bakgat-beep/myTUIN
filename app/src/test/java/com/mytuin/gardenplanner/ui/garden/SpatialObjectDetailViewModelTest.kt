package com.mytuin.gardenplanner.ui.garden

import androidx.lifecycle.SavedStateHandle
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.usecases.garden.ArchiveSpatialObject
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveSpatialObject
import com.mytuin.gardenplanner.domain.usecases.garden.RestoreSpatialObject
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeSpatialObjectRepository
import com.mytuin.gardenplanner.ui.navigation.Routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SpatialObjectDetailViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private val sample =
        SpatialObject(
            id = "spatialobject_1",
            gardenId = "garden-1",
            objectType = InfrastructureType.FENCE,
            geometry =
                Geometry.Polygon(
                    listOf(
                        Coordinate(0.0, 0.0),
                        Coordinate(1.0, 0.0),
                        Coordinate(1.0, 1.0),
                        Coordinate(0.0, 1.0),
                        Coordinate(0.0, 0.0),
                    ),
                ),
            status = RecordStatus.ACTIVE,
            name = "Fence",
            description = null,
            areaId = null,
            notes = null,
            createdAt = 1L,
            updatedAt = 1L,
        )

    private fun newViewModel(repo: FakeSpatialObjectRepository): SpatialObjectDetailViewModel {
        val handle = SavedStateHandle(mapOf(Routes.SPATIAL_OBJECT_DETAIL_ARG_ID to "spatialobject_1"))
        return SpatialObjectDetailViewModel(
            handle,
            ObserveSpatialObject(repo),
            ArchiveSpatialObject(repo, FakeClock()),
            RestoreSpatialObject(repo, FakeClock()),
        )
    }

    @Test
    fun `archive emits ARCHIVED finished event`() =
        runTest {
            val repo = FakeSpatialObjectRepository()
            repo.insert(sample)
            val vm = newViewModel(repo)
            testScheduler.advanceUntilIdle()
            vm.archive()
            testScheduler.advanceUntilIdle()
            assertEquals(SpatialObjectDetailUiState.FinishedEvent.ARCHIVED, vm.uiState.value.finishedEvent)
        }

    @Test
    fun `restore emits RESTORED finished event`() =
        runTest {
            val repo = FakeSpatialObjectRepository()
            repo.insert(sample.copy(status = RecordStatus.ARCHIVED))
            val vm = newViewModel(repo)
            testScheduler.advanceUntilIdle()
            vm.restore()
            testScheduler.advanceUntilIdle()
            assertEquals(SpatialObjectDetailUiState.FinishedEvent.RESTORED, vm.uiState.value.finishedEvent)
        }

    @Test
    fun `onFinishedHandled clears event`() =
        runTest {
            val repo = FakeSpatialObjectRepository()
            repo.insert(sample)
            val vm = newViewModel(repo)
            testScheduler.advanceUntilIdle()
            vm.archive()
            testScheduler.advanceUntilIdle()
            vm.onFinishedHandled()
            assertNull(vm.uiState.value.finishedEvent)
        }
}
