package com.mytuin.gardenplanner.ui.garden

import androidx.lifecycle.SavedStateHandle
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpaceFields
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveGrowingSpace
import com.mytuin.gardenplanner.domain.usecases.garden.UpdateGrowingSpaceFields
import com.mytuin.gardenplanner.domain.usecases.garden.UpdateGrowingSpaceGeometry
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
class EditGrowingSpaceViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val spaceId = "growingspace_test_0001"
    private val gardenId = "garden_test_0001"
    private lateinit var repository: FakeGrowingSpaceRepository
    private lateinit var viewModel: EditGrowingSpaceViewModel

    @Before
    fun setUp() =
        runTest(testDispatcher) {
            Dispatchers.setMain(testDispatcher)
            repository = FakeGrowingSpaceRepository()
            repository.insert(sampleSpace())

            val clock = FakeClock(nowMillisValue = 1_700_000_500_000L)
            val handle = SavedStateHandle(mapOf(Routes.EDIT_GROWING_SPACE_ARG_ID to spaceId))
            viewModel =
                EditGrowingSpaceViewModel(
                    savedStateHandle = handle,
                    observeGrowingSpace = ObserveGrowingSpace(repository),
                    updateGrowingSpaceFields = UpdateGrowingSpaceFields(repository, clock),
                    updateGrowingSpaceGeometry = UpdateGrowingSpaceGeometry(repository, clock),
                )
            advanceUntilIdle()
        }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loads_space_into_state() {
        val state = viewModel.uiState.value
        assertEquals(false, state.isLoading)
        assertEquals(false, state.loadError)
        assertEquals("Bed 2", state.space?.name)
    }

    @Test
    fun null_space_sets_load_error() =
        runTest(testDispatcher) {
            val handle = SavedStateHandle(mapOf(Routes.EDIT_GROWING_SPACE_ARG_ID to "missing"))
            val clock = FakeClock(nowMillisValue = 1_700_000_500_000L)
            val vm =
                EditGrowingSpaceViewModel(
                    savedStateHandle = handle,
                    observeGrowingSpace = ObserveGrowingSpace(repository),
                    updateGrowingSpaceFields = UpdateGrowingSpaceFields(repository, clock),
                    updateGrowingSpaceGeometry = UpdateGrowingSpaceGeometry(repository, clock),
                )
            advanceUntilIdle()

            assertEquals(false, vm.uiState.value.isLoading)
            assertTrue(vm.uiState.value.loadError)
            assertNull(vm.uiState.value.space)
        }

    @Test
    fun save_updates_fields_only_and_sets_savedEvent() =
        runTest(testDispatcher) {
            viewModel.save(
                sampleFormData().copy(
                    fields =
                        sampleFields().copy(
                            name = "Bed 2 (renamed)",
                            notes = "updated",
                        ),
                    geometryEdit = GeometryEdit.Unchanged,
                ),
            )
            advanceUntilIdle()

            val space = repository.snapshot().single()
            assertEquals("Bed 2 (renamed)", space.name)
            assertEquals("updated", space.notes)
            // Geometry unchanged
            assertEquals(sampleSpace().geometry, space.geometry)
            // Save flow completed
            assertTrue(viewModel.uiState.value.savedEvent)
            assertEquals(false, viewModel.uiState.value.isSaving)
        }

    @Test
    fun save_with_geometry_Replace_also_updates_geometry() =
        runTest(testDispatcher) {
            val newGeometry =
                Geometry.Polygon(
                    listOf(
                        Coordinate(0.0, 0.0),
                        Coordinate(4.0, 0.0),
                        Coordinate(4.0, 1.0),
                        Coordinate(0.0, 0.0),
                    ),
                )
            viewModel.save(
                sampleFormData().copy(
                    geometryEdit = GeometryEdit.Replace(newGeometry),
                ),
            )
            advanceUntilIdle()

            assertEquals(newGeometry, repository.snapshot().single().geometry)
            assertEquals(1, repository.updateCalls().size)
            assertEquals(newGeometry, repository.updateCalls().single().newGeometry)
            assertTrue(viewModel.uiState.value.savedEvent)
        }

    @Test
    fun onSavedHandled_clears_savedEvent() =
        runTest(testDispatcher) {
            viewModel.save(sampleFormData())
            advanceUntilIdle()
            assertTrue(viewModel.uiState.value.savedEvent)

            viewModel.onSavedHandled()
            assertEquals(false, viewModel.uiState.value.savedEvent)
        }

    private fun sampleFields(): GrowingSpaceFields =
        GrowingSpaceFields(
            name = "Bed 2",
            spaceType = GrowingSpaceType.RAISED_BED,
            description = null,
            lengthMetres = 3.0,
            widthMetres = 1.2,
            heightMetres = null,
            diameterMetres = null,
            areaSquareMetres = null,
            volumeCubicMetres = null,
            notes = null,
        )

    private fun sampleFormData(): GrowingSpaceFormData =
        GrowingSpaceFormData(
            fields = sampleFields(),
            geometryEdit = GeometryEdit.Unchanged,
        )

    private fun sampleSpace(): GrowingSpace =
        GrowingSpace(
            id = spaceId,
            gardenId = gardenId,
            name = "Bed 2",
            spaceType = GrowingSpaceType.RAISED_BED,
            status = RecordStatus.ACTIVE,
            geometry = Geometry.Point(Coordinate(1.0, 2.0)),
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
