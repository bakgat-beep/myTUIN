package com.mytuin.gardenplanner.ui.garden

import androidx.lifecycle.SavedStateHandle
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpaceFields
import com.mytuin.gardenplanner.domain.usecases.garden.CreateGrowingSpace
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeGrowingSpaceRepository
import com.mytuin.gardenplanner.testdoubles.FakeIdGenerator
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
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateGrowingSpaceViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val gardenId = "garden_test_0001"
    private lateinit var repository: FakeGrowingSpaceRepository
    private lateinit var viewModel: CreateGrowingSpaceViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeGrowingSpaceRepository()
        val handle = SavedStateHandle(mapOf(Routes.CREATE_GROWING_SPACE_ARG_GARDEN_ID to gardenId))
        val create =
            CreateGrowingSpace(
                repository = repository,
                idGenerator = FakeIdGenerator(nextGrowingSpaceIdValue = "growingspace_new_0001"),
                clock = FakeClock(nowMillisValue = 1_700_000_000_000L),
            )
        viewModel = CreateGrowingSpaceViewModel(handle, create)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initial_state_has_no_errors_and_no_saved_id() {
        val state = viewModel.uiState.value
        assertEquals(false, state.isSaving)
        assertEquals(false, state.saveError)
        assertNull(state.savedSpaceId)
    }

    @Test
    fun save_persists_and_sets_savedSpaceId() =
        runTest(testDispatcher) {
            viewModel.save(sampleFormData())
            advanceUntilIdle()

            assertEquals(1, repository.snapshot().size)
            assertEquals("growingspace_new_0001", viewModel.uiState.value.savedSpaceId)
            assertEquals(false, viewModel.uiState.value.isSaving)
            assertEquals(false, viewModel.uiState.value.saveError)
        }

    @Test
    fun save_with_geometry_Replace_creates_space_with_polygon() =
        runTest(testDispatcher) {
            viewModel.save(
                sampleFormData().copy(
                    geometryEdit =
                        GeometryEdit.Replace(
                            com.mytuin.gardenplanner.domain.model.garden.Geometry.Polygon(
                                listOf(
                                    com.mytuin.gardenplanner.domain.model.garden
                                        .Coordinate(0.0, 0.0),
                                    com.mytuin.gardenplanner.domain.model.garden
                                        .Coordinate(1.0, 0.0),
                                    com.mytuin.gardenplanner.domain.model.garden
                                        .Coordinate(1.0, 1.0),
                                    com.mytuin.gardenplanner.domain.model.garden
                                        .Coordinate(0.0, 0.0),
                                ),
                            ),
                        ),
                ),
            )
            advanceUntilIdle()

            val space = repository.snapshot().single()
            assertEquals(
                com.mytuin.gardenplanner.domain.model.garden.Geometry.Polygon(
                    listOf(
                        com.mytuin.gardenplanner.domain.model.garden
                            .Coordinate(0.0, 0.0),
                        com.mytuin.gardenplanner.domain.model.garden
                            .Coordinate(1.0, 0.0),
                        com.mytuin.gardenplanner.domain.model.garden
                            .Coordinate(1.0, 1.0),
                        com.mytuin.gardenplanner.domain.model.garden
                            .Coordinate(0.0, 0.0),
                    ),
                ),
                space.geometry,
            )
        }

    @Test
    fun save_with_geometry_Unchanged_creates_space_with_null_geometry() =
        runTest(testDispatcher) {
            viewModel.save(sampleFormData())
            advanceUntilIdle()

            val space = repository.snapshot().single()
            assertNull(space.geometry)
        }

    @Test
    fun onSavedHandled_clears_savedSpaceId() =
        runTest(testDispatcher) {
            viewModel.save(sampleFormData())
            advanceUntilIdle()
            assertEquals("growingspace_new_0001", viewModel.uiState.value.savedSpaceId)

            viewModel.onSavedHandled()
            assertNull(viewModel.uiState.value.savedSpaceId)
        }

    private fun sampleFormData(): GrowingSpaceFormData =
        GrowingSpaceFormData(
            fields =
                GrowingSpaceFields(
                    name = "Bed 1",
                    spaceType = GrowingSpaceType.RAISED_BED,
                    description = null,
                    lengthMetres = null,
                    widthMetres = null,
                    heightMetres = null,
                    diameterMetres = null,
                    areaSquareMetres = null,
                    volumeCubicMetres = null,
                    notes = null,
                ),
            geometryEdit = GeometryEdit.Unchanged,
        )
}
