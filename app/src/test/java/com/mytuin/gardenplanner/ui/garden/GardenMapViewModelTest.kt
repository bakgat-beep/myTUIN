package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveAreasInGarden
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveGrowingSpacesInGarden
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveSpatialObjectsInGarden
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeAreaRepository
import com.mytuin.gardenplanner.testdoubles.FakeGardenRepository
import com.mytuin.gardenplanner.testdoubles.FakeGrowingSpaceRepository
import com.mytuin.gardenplanner.testdoubles.FakeSpatialObjectRepository
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
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GardenMapViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun sampleGarden() =
        Garden(
            id = "garden_1",
            name = "Back Garden",
            description = null,
            countryCode = "NZ",
            region = null,
            locality = null,
            latitude = -43.5321,
            longitude = 172.6362,
            timezone = null,
            hemisphere = Hemisphere.SOUTHERN,
            status = RecordStatus.ACTIVE,
            createdAt = 1L,
            updatedAt = 1L,
        )

    private fun sampleSpace(id: String) =
        GrowingSpace(
            id = id,
            gardenId = "garden_1",
            name = "Bed $id",
            spaceType = GrowingSpaceType.RAISED_BED,
            status = RecordStatus.ACTIVE,
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
            lengthMetres = null,
            widthMetres = null,
            heightMetres = null,
            diameterMetres = null,
            areaSquareMetres = null,
            volumeCubicMetres = null,
            description = null,
            areaId = null,
            notes = null,
            createdAt = 1L,
            updatedAt = 1L,
        )

    private fun sampleObject(id: String) =
        SpatialObject(
            id = id,
            gardenId = "garden_1",
            objectType = InfrastructureType.FENCE,
            geometry = Geometry.LineString(listOf(Coordinate(0.0, 0.0), Coordinate(5.0, 0.0))),
            status = RecordStatus.ACTIVE,
            name = "Fence",
            description = null,
            areaId = null,
            notes = null,
            createdAt = 1L,
            updatedAt = 1L,
        )

    @Test
    fun `no garden emits NoGarden`() =
        runTest {
            val gardenRepo = FakeGardenRepository()
            val vm =
                GardenMapViewModel(
                    gardenRepo,
                    ObserveGrowingSpacesInGarden(FakeGrowingSpaceRepository()),
                    ObserveSpatialObjectsInGarden(FakeSpatialObjectRepository()),
                    ObserveAreasInGarden(FakeAreaRepository()),
                )
            val job = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.uiState.collect {} }
            testScheduler.advanceUntilIdle()
            assertEquals(GardenMapUiState.NoGarden, vm.uiState.value)
            job.cancel()
        }

    @Test
    fun `garden without geometry emits NoGeometries`() =
        runTest {
            val gardenRepo = FakeGardenRepository()
            gardenRepo.insert(sampleGarden())
            val vm =
                GardenMapViewModel(
                    gardenRepo,
                    ObserveGrowingSpacesInGarden(FakeGrowingSpaceRepository()),
                    ObserveSpatialObjectsInGarden(FakeSpatialObjectRepository()),
                    ObserveAreasInGarden(FakeAreaRepository()),
                )
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.uiState.collect {} }
            testScheduler.advanceUntilIdle()
            assertEquals(GardenMapUiState.NoGeometries, vm.uiState.value)
        }

    @Test
    fun `garden with shapes emits Ready with viewport and origin`() =
        runTest {
            val gardenRepo = FakeGardenRepository().apply { insert(sampleGarden()) }
            val spaceRepo = FakeGrowingSpaceRepository().apply { insert(sampleSpace("space_1")) }
            val objectRepo = FakeSpatialObjectRepository().apply { insert(sampleObject("object_1")) }
            val vm =
                GardenMapViewModel(
                    gardenRepo,
                    ObserveGrowingSpacesInGarden(spaceRepo),
                    ObserveSpatialObjectsInGarden(objectRepo),
                    ObserveAreasInGarden(FakeAreaRepository()),
                )
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.uiState.collect {} }
            testScheduler.advanceUntilIdle()
            val state = vm.uiState.value
            assertTrue(state is GardenMapUiState.Ready)
            state as GardenMapUiState.Ready
            assertEquals("Back Garden", state.gardenName)
            assertEquals(-43.5321, state.origin.latitude, 1e-9)
            assertEquals(172.6362, state.origin.longitude, 1e-9)
            assertEquals(2, state.shapes.size)
            val ids = state.shapes.map { it.id }.toSet()
            assertEquals(setOf("space_1", "object_1"), ids)
        }
}
