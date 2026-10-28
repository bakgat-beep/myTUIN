package com.mytuin.gardenplanner.ui.garden

import androidx.lifecycle.SavedStateHandle
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeSpatialObjectRepository
import com.mytuin.gardenplanner.ui.navigation.Routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SpatialObjectsListViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun obj(
        id: String,
        status: RecordStatus,
        name: String? = "object-$id",
    ): SpatialObject =
        SpatialObject(
            id = id,
            gardenId = "garden-1",
            objectType = InfrastructureType.PATH,
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
            status = status,
            name = name,
            description = null,
            areaId = null,
            notes = null,
            createdAt = 1L,
            updatedAt = 1L,
        )

    private fun newViewModel(repo: FakeSpatialObjectRepository): SpatialObjectsListViewModel {
        val handle = SavedStateHandle(mapOf(Routes.SPATIAL_OBJECTS_ARG_GARDEN_ID to "garden-1"))
        return SpatialObjectsListViewModel(handle, repo)
    }

    @Test
    fun `ready state shows active only when showArchived false`() =
        runTest {
            val repo = FakeSpatialObjectRepository()
            repo.insert(obj("a", RecordStatus.ACTIVE))
            repo.insert(obj("b", RecordStatus.ARCHIVED))
            val vm = newViewModel(repo)

            // stateIn(WhileSubscribed) requires an active downstream
            // collector for the combine to start emitting. An
            // UnconfinedTestDispatcher on the same test scheduler
            // subscribes synchronously so the test does not race the
            // sharing startup. backgroundScope auto-cancels at the end
            // of runTest, so no manual job.cancel is needed.
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                vm.uiState.collect {}
            }
            testScheduler.advanceUntilIdle()

            val state = vm.uiState.value as SpatialObjectsListUiState.Ready
            assertEquals(1, state.objects.size)
            assertFalse(state.showArchived)
        }

    @Test
    fun `toggleShowArchived surfaces archived objects`() =
        runTest {
            val repo = FakeSpatialObjectRepository()
            repo.insert(obj("a", RecordStatus.ACTIVE))
            repo.insert(obj("b", RecordStatus.ARCHIVED))
            val vm = newViewModel(repo)

            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
                vm.uiState.collect {}
            }
            testScheduler.advanceUntilIdle()

            vm.toggleShowArchived()
            testScheduler.advanceUntilIdle()

            val state = vm.uiState.value as SpatialObjectsListUiState.Ready
            assertEquals(2, state.objects.size)
            assertTrue(state.showArchived)
        }
}
