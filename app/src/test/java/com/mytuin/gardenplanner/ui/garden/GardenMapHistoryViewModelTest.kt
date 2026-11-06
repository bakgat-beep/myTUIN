package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpaceHistory
import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.model.garden.SpatialObjectHistory
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveGrowingSpacesInGarden
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveSpatialObjectsInGarden
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeGardenRepository
import com.mytuin.gardenplanner.testdoubles.FakeGrowingSpaceHistoryRepository
import com.mytuin.gardenplanner.testdoubles.FakeGrowingSpaceRepository
import com.mytuin.gardenplanner.testdoubles.FakeSpatialObjectHistoryRepository
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
class GardenMapHistoryViewModelTest {
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
            createdAt = 1_000L,
            updatedAt = 1_000L,
        )

    private fun sampleSpace(createdAt: Long = 1_000L) =
        GrowingSpace(
            id = "gs_1",
            gardenId = "garden_1",
            name = "Bed",
            spaceType = GrowingSpaceType.RAISED_BED,
            status = RecordStatus.ACTIVE,
            geometry =
                Geometry.Polygon(
                    listOf(
                        Coordinate(5.0, 0.0),
                        Coordinate(6.0, 0.0),
                        Coordinate(6.0, 1.0),
                        Coordinate(5.0, 1.0),
                        Coordinate(5.0, 0.0),
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
            createdAt = createdAt,
            updatedAt = createdAt,
        )

    private fun oldGeometry() =
        Geometry.Polygon(
            listOf(
                Coordinate(0.0, 0.0),
                Coordinate(1.0, 0.0),
                Coordinate(1.0, 1.0),
                Coordinate(0.0, 1.0),
                Coordinate(0.0, 0.0),
            ),
        )

    private fun sampleHistoryRow(
        spaceId: String,
        geometry: Geometry,
        validFrom: Long,
        validTo: Long,
    ) = GrowingSpaceHistory(
        id = "hist_$spaceId",
        growingSpaceId = spaceId,
        geometry = geometry,
        lengthMetres = null,
        widthMetres = null,
        heightMetres = null,
        diameterMetres = null,
        areaSquareMetres = null,
        volumeCubicMetres = null,
        validFrom = validFrom,
        validTo = validTo,
        recordedAt = validTo,
        reason = null,
    )

    private fun newViewModel(
        gardenRepo: FakeGardenRepository = FakeGardenRepository(),
        spaceRepo: FakeGrowingSpaceRepository = FakeGrowingSpaceRepository(),
        objectRepo: FakeSpatialObjectRepository = FakeSpatialObjectRepository(),
        spaceHistory: FakeGrowingSpaceHistoryRepository = FakeGrowingSpaceHistoryRepository(),
        objectHistory: FakeSpatialObjectHistoryRepository = FakeSpatialObjectHistoryRepository(),
        clock: FakeClock = FakeClock(),
    ): GardenMapHistoryViewModel =
        GardenMapHistoryViewModel(
            gardenRepository = gardenRepo,
            observeGrowingSpacesInGarden = ObserveGrowingSpacesInGarden(spaceRepo),
            observeSpatialObjectsInGarden = ObserveSpatialObjectsInGarden(objectRepo),
            growingSpaceHistoryRepository = spaceHistory,
            spatialObjectHistoryRepository = objectHistory,
            clock = clock,
        )

    @Test
    fun `no garden emits NoGarden`() =
        runTest {
            val vm = newViewModel()
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.uiState.collect {} }
            testScheduler.advanceUntilIdle()
            assertEquals(GardenMapHistoryUiState.NoGarden, vm.uiState.value)
        }

    @Test
    fun `garden with no geometries emits NoShapes`() =
        runTest {
            val gardenRepo = FakeGardenRepository().apply { insert(sampleGarden()) }
            val vm = newViewModel(gardenRepo = gardenRepo)
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.uiState.collect {} }
            testScheduler.advanceUntilIdle()
            assertEquals(GardenMapHistoryUiState.NoShapes, vm.uiState.value)
        }

    @Test
    fun `garden with shapes but no history emits NoHistory`() =
        runTest {
            val gardenRepo = FakeGardenRepository().apply { insert(sampleGarden()) }
            val spaceRepo = FakeGrowingSpaceRepository().apply { insert(sampleSpace()) }
            val vm = newViewModel(gardenRepo = gardenRepo, spaceRepo = spaceRepo)
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.uiState.collect {} }
            testScheduler.advanceUntilIdle()
            assertEquals(GardenMapHistoryUiState.NoHistory, vm.uiState.value)
        }

    @Test
    fun `garden with history emits Ready at maxTime`() =
        runTest {
            val clock = FakeClock()
            val now = clock.nowMillis()
            val space = sampleSpace(createdAt = now - 10_000L)
            val gardenRepo = FakeGardenRepository().apply { insert(sampleGarden()) }
            val spaceRepo = FakeGrowingSpaceRepository().apply { insert(space) }
            val spaceHistory =
                FakeGrowingSpaceHistoryRepository().apply {
                    seed(
                        listOf(
                            sampleHistoryRow(
                                spaceId = space.id,
                                geometry = oldGeometry(),
                                validFrom = space.createdAt,
                                validTo = now - 5_000L,
                            ),
                        ),
                    )
                }
            val vm =
                newViewModel(
                    gardenRepo = gardenRepo,
                    spaceRepo = spaceRepo,
                    spaceHistory = spaceHistory,
                    clock = clock,
                )
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.uiState.collect {} }
            testScheduler.advanceUntilIdle()
            val state = vm.uiState.value
            assertTrue(state is GardenMapHistoryUiState.Ready)
            state as GardenMapHistoryUiState.Ready
            assertEquals("Back Garden", state.gardenName)
            assertEquals(now, state.currentTime)
            assertEquals(1, state.shapes.size)
        }

    @Test
    fun `setTime before first edit returns history row geometry`() =
        runTest {
            val clock = FakeClock()
            val now = clock.nowMillis()
            val space = sampleSpace(createdAt = now - 10_000L)
            val gardenRepo = FakeGardenRepository().apply { insert(sampleGarden()) }
            val spaceRepo = FakeGrowingSpaceRepository().apply { insert(space) }
            val spaceHistory =
                FakeGrowingSpaceHistoryRepository().apply {
                    seed(
                        listOf(
                            sampleHistoryRow(
                                spaceId = space.id,
                                geometry = oldGeometry(),
                                validFrom = space.createdAt,
                                validTo = now - 5_000L,
                            ),
                        ),
                    )
                }
            val vm =
                newViewModel(
                    gardenRepo = gardenRepo,
                    spaceRepo = spaceRepo,
                    spaceHistory = spaceHistory,
                    clock = clock,
                )
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.uiState.collect {} }
            testScheduler.advanceUntilIdle()

            vm.setTime(space.createdAt + 1_000L)
            testScheduler.advanceUntilIdle()

            val state = vm.uiState.value as GardenMapHistoryUiState.Ready
            assertEquals(space.createdAt + 1_000L, state.currentTime)
            assertEquals(oldGeometry(), state.shapes.first().geometry)
        }

    @Test
    fun `setTime at maxTime returns current geometry`() =
        runTest {
            val clock = FakeClock()
            val now = clock.nowMillis()
            val space = sampleSpace(createdAt = now - 10_000L)
            val gardenRepo = FakeGardenRepository().apply { insert(sampleGarden()) }
            val spaceRepo = FakeGrowingSpaceRepository().apply { insert(space) }
            val spaceHistory =
                FakeGrowingSpaceHistoryRepository().apply {
                    seed(
                        listOf(
                            sampleHistoryRow(
                                spaceId = space.id,
                                geometry = oldGeometry(),
                                validFrom = space.createdAt,
                                validTo = now - 5_000L,
                            ),
                        ),
                    )
                }
            val vm =
                newViewModel(
                    gardenRepo = gardenRepo,
                    spaceRepo = spaceRepo,
                    spaceHistory = spaceHistory,
                    clock = clock,
                )
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.uiState.collect {} }
            testScheduler.advanceUntilIdle()

            vm.setTime(now)
            testScheduler.advanceUntilIdle()

            val state = vm.uiState.value as GardenMapHistoryUiState.Ready
            assertEquals(now, state.currentTime)
            assertEquals(space.geometry, state.shapes.first().geometry)
        }

    @Test
    fun `setTime before entity createdAt returns empty shapes`() =
        runTest {
            val clock = FakeClock()
            val now = clock.nowMillis()
            val space = sampleSpace(createdAt = now - 10_000L)
            val gardenRepo = FakeGardenRepository().apply { insert(sampleGarden()) }
            val spaceRepo = FakeGrowingSpaceRepository().apply { insert(space) }
            val spaceHistory =
                FakeGrowingSpaceHistoryRepository().apply {
                    seed(
                        listOf(
                            sampleHistoryRow(
                                spaceId = space.id,
                                geometry = oldGeometry(),
                                validFrom = space.createdAt,
                                validTo = now - 5_000L,
                            ),
                        ),
                    )
                }
            val vm =
                newViewModel(
                    gardenRepo = gardenRepo,
                    spaceRepo = spaceRepo,
                    spaceHistory = spaceHistory,
                    clock = clock,
                )
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.uiState.collect {} }
            testScheduler.advanceUntilIdle()

            // Clamping prevents going before minTime (earliest createdAt).
            // minTime == space.createdAt here, so any earlier T clamps up.
            vm.setTime(space.createdAt)
            testScheduler.advanceUntilIdle()
            val state = vm.uiState.value as GardenMapHistoryUiState.Ready
            assertEquals(1, state.shapes.size)
        }

    @Test
    fun `jumpToNow sets currentTime back to maxTime`() =
        runTest {
            val clock = FakeClock()
            val now = clock.nowMillis()
            val space = sampleSpace(createdAt = now - 10_000L)
            val gardenRepo = FakeGardenRepository().apply { insert(sampleGarden()) }
            val spaceRepo = FakeGrowingSpaceRepository().apply { insert(space) }
            val spaceHistory =
                FakeGrowingSpaceHistoryRepository().apply {
                    seed(
                        listOf(
                            sampleHistoryRow(
                                spaceId = space.id,
                                geometry = oldGeometry(),
                                validFrom = space.createdAt,
                                validTo = now - 5_000L,
                            ),
                        ),
                    )
                }
            val vm =
                newViewModel(
                    gardenRepo = gardenRepo,
                    spaceRepo = spaceRepo,
                    spaceHistory = spaceHistory,
                    clock = clock,
                )
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.uiState.collect {} }
            testScheduler.advanceUntilIdle()

            vm.setTime(space.createdAt + 100L)
            testScheduler.advanceUntilIdle()
            vm.jumpToNow()
            testScheduler.advanceUntilIdle()

            val state = vm.uiState.value as GardenMapHistoryUiState.Ready
            assertEquals(now, state.currentTime)
        }

    @Test
    fun `spatial objects are included in the timeline`() =
        runTest {
            val clock = FakeClock()
            val now = clock.nowMillis()
            val obj =
                SpatialObject(
                    id = "so_1",
                    gardenId = "garden_1",
                    objectType = InfrastructureType.FENCE,
                    geometry = oldGeometry(),
                    status = RecordStatus.ACTIVE,
                    name = "Fence",
                    description = null,
                    areaId = null,
                    notes = null,
                    createdAt = now - 10_000L,
                    updatedAt = now - 10_000L,
                )
            val newGeom = sampleSpace().geometry!!
            val objectHistory =
                FakeSpatialObjectHistoryRepository().apply {
                    seed(
                        listOf(
                            SpatialObjectHistory(
                                id = "hist_so_1",
                                spatialObjectId = obj.id,
                                geometry = oldGeometry(),
                                validFrom = obj.createdAt,
                                validTo = now - 5_000L,
                                recordedAt = now - 5_000L,
                                reason = null,
                            ),
                        ),
                    )
                }
            val gardenRepo = FakeGardenRepository().apply { insert(sampleGarden()) }
            val objectRepo =
                FakeSpatialObjectRepository().apply {
                    insert(obj.copy(geometry = newGeom))
                }
            val vm =
                newViewModel(
                    gardenRepo = gardenRepo,
                    objectRepo = objectRepo,
                    objectHistory = objectHistory,
                    clock = clock,
                )
            backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { vm.uiState.collect {} }
            testScheduler.advanceUntilIdle()

            val state = vm.uiState.value as GardenMapHistoryUiState.Ready
            assertEquals(1, state.shapes.size)
            assertEquals(newGeom, state.shapes.first().geometry)

            vm.setTime(obj.createdAt + 100L)
            testScheduler.advanceUntilIdle()
            val pastState = vm.uiState.value as GardenMapHistoryUiState.Ready
            assertEquals(oldGeometry(), pastState.shapes.first().geometry)
        }
}
