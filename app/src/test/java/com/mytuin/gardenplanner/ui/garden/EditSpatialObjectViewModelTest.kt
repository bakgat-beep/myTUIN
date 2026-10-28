package com.mytuin.gardenplanner.ui.garden

import androidx.lifecycle.SavedStateHandle
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.model.garden.SpatialObjectFields
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveSpatialObject
import com.mytuin.gardenplanner.domain.usecases.garden.UpdateSpatialObjectFields
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeAreaRepository
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
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EditSpatialObjectViewModelTest {
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
            status = RecordStatus.ACTIVE,
            name = "Main path",
            description = null,
            areaId = null,
            notes = null,
            createdAt = 1L,
            updatedAt = 1L,
        )

    private suspend fun seededRepo(): FakeSpatialObjectRepository {
        val repo = FakeSpatialObjectRepository()
        repo.insert(sample)
        return repo
    }

    private fun newViewModel(repo: FakeSpatialObjectRepository): EditSpatialObjectViewModel {
        val handle = SavedStateHandle(mapOf(Routes.EDIT_SPATIAL_OBJECT_ARG_ID to "spatialobject_1"))
        val observe = ObserveSpatialObject(repo)
        val update = UpdateSpatialObjectFields(repo, FakeClock())
        return EditSpatialObjectViewModel(handle, observe, update, FakeAreaRepository())
    }

    @Test
    fun `save forwards ForEdit fields to repository`() =
        runTest {
            val repo = seededRepo()
            val vm = newViewModel(repo)
            testScheduler.advanceUntilIdle()
            assertEquals(sample, vm.uiState.value.spatialObject)

            vm.save(
                SpatialObjectFormData.ForEdit(
                    fields =
                        SpatialObjectFields(
                            objectType = InfrastructureType.FENCE,
                            name = "Renamed path",
                            description = "Now a fence",
                            areaId = null,
                            notes = "note",
                        ),
                ),
            )
            testScheduler.advanceUntilIdle()

            val updated = repo.snapshot().first { it.id == "spatialobject_1" }
            assertEquals(InfrastructureType.FENCE, updated.objectType)
            assertEquals("Renamed path", updated.name)
            assertEquals("Now a fence", updated.description)
            assertTrue(vm.uiState.value.savedEvent)
            assertFalse(vm.uiState.value.saveError)
        }

    @Test
    fun `save ignores ForCreate payload`() =
        runTest {
            val repo = seededRepo()
            val vm = newViewModel(repo)
            testScheduler.advanceUntilIdle()

            vm.save(
                SpatialObjectFormData.ForCreate(
                    objectType = InfrastructureType.WALL,
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
                    name = "x",
                    description = null,
                    areaId = null,
                    notes = null,
                ),
            )
            testScheduler.advanceUntilIdle()
            assertTrue(repo.fieldsUpdateCalls().isEmpty())
            assertFalse(vm.uiState.value.savedEvent)
        }

    @Test
    fun `onSavedHandled clears savedEvent`() =
        runTest {
            val repo = seededRepo()
            val vm = newViewModel(repo)
            testScheduler.advanceUntilIdle()
            vm.save(
                SpatialObjectFormData.ForEdit(
                    fields =
                        SpatialObjectFields(
                            objectType = InfrastructureType.PATH,
                            name = "New name",
                            description = null,
                            areaId = null,
                            notes = null,
                        ),
                ),
            )
            testScheduler.advanceUntilIdle()
            vm.onSavedHandled()
            assertFalse(vm.uiState.value.savedEvent)
        }

    @Test
    fun `loadError set when object missing`() =
        runTest {
            val repo = FakeSpatialObjectRepository()
            val vm = newViewModel(repo)
            testScheduler.advanceUntilIdle()
            assertNull(vm.uiState.value.spatialObject)
            assertTrue(vm.uiState.value.loadError)
        }
}
