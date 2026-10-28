package com.mytuin.gardenplanner.ui.garden

import androidx.lifecycle.SavedStateHandle
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.usecases.garden.CreateSpatialObject
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.testdoubles.FakeAreaRepository
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeIdGenerator
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
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateSpatialObjectViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun newViewModel(
        repo: FakeSpatialObjectRepository = FakeSpatialObjectRepository(),
        areas: FakeAreaRepository = FakeAreaRepository(),
    ): Pair<CreateSpatialObjectViewModel, FakeSpatialObjectRepository> {
        val useCase = CreateSpatialObject(repo, FakeIdGenerator(), FakeClock())
        val handle = SavedStateHandle(mapOf(Routes.CREATE_SPATIAL_OBJECT_ARG_GARDEN_ID to "garden-1"))
        return CreateSpatialObjectViewModel(handle, useCase, areas) to repo
    }

    @Test
    fun `save inserts object with supplied fields`() =
        runTest {
            val (vm, repo) = newViewModel()
            vm.save(
                SpatialObjectFormData.ForCreate(
                    objectType = InfrastructureType.FENCE,
                    geometry =
                        Geometry.Polygon(
                            listOf(
                                Coordinate(0.0, 0.0),
                                Coordinate(2.0, 0.0),
                                Coordinate(2.0, 1.0),
                                Coordinate(0.0, 1.0),
                                Coordinate(0.0, 0.0),
                            ),
                        ),
                    name = "Back fence",
                    description = null,
                    areaId = null,
                    notes = null,
                ),
            )
            testScheduler.advanceUntilIdle()
            assertEquals(1, repo.snapshot().size)
            val saved = repo.snapshot().first()
            assertEquals("garden-1", saved.gardenId)
            assertEquals(InfrastructureType.FENCE, saved.objectType)
            assertEquals("Back fence", saved.name)
            assertNotNull(vm.uiState.value.savedObjectId)
            assertFalse(vm.uiState.value.isSaving)
            assertFalse(vm.uiState.value.saveError)
        }

    @Test
    fun `save ignores ForEdit payload`() =
        runTest {
            val (vm, repo) = newViewModel()
            vm.save(
                SpatialObjectFormData.ForEdit(
                    fields =
                        com.mytuin.gardenplanner.domain.model.garden.SpatialObjectFields(
                            objectType = InfrastructureType.PATH,
                            name = null,
                            description = null,
                            areaId = null,
                            notes = null,
                        ),
                ),
            )
            testScheduler.advanceUntilIdle()
            assertEquals(0, repo.snapshot().size)
            assertNull(vm.uiState.value.savedObjectId)
        }

    @Test
    fun `onSavedHandled clears savedObjectId`() =
        runTest {
            val (vm, _) = newViewModel()
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
                    name = null,
                    description = null,
                    areaId = null,
                    notes = null,
                ),
            )
            testScheduler.advanceUntilIdle()
            vm.onSavedHandled()
            assertNull(vm.uiState.value.savedObjectId)
        }
}
