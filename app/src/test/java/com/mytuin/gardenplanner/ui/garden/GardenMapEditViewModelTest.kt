package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.model.garden.SpatialObjectHistory
import com.mytuin.gardenplanner.domain.usecases.garden.ArchiveGrowingSpace
import com.mytuin.gardenplanner.domain.usecases.garden.ArchiveSpatialObject
import com.mytuin.gardenplanner.domain.usecases.garden.UpdateGrowingSpaceGeometry
import com.mytuin.gardenplanner.domain.usecases.garden.UpdateSpatialObjectGeometry
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeGrowingSpaceHistoryRepository
import com.mytuin.gardenplanner.testdoubles.FakeGrowingSpaceRepository
import com.mytuin.gardenplanner.testdoubles.FakeSpatialObjectHistoryRepository
import com.mytuin.gardenplanner.testdoubles.FakeSpatialObjectRepository
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
class GardenMapEditViewModelTest {
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
        gsRepo: FakeGrowingSpaceRepository = FakeGrowingSpaceRepository(),
        soRepo: FakeSpatialObjectRepository = FakeSpatialObjectRepository(),
        gsHistory: FakeGrowingSpaceHistoryRepository = FakeGrowingSpaceHistoryRepository(),
        soHistory: FakeSpatialObjectHistoryRepository = FakeSpatialObjectHistoryRepository(),
    ): Triple<GardenMapEditViewModel, FakeGrowingSpaceRepository, FakeSpatialObjectRepository> {
        val clock = FakeClock()
        val vm =
            GardenMapEditViewModel(
                updateGrowingSpaceGeometry = UpdateGrowingSpaceGeometry(gsRepo, clock),
                updateSpatialObjectGeometry = UpdateSpatialObjectGeometry(soRepo, clock),
                archiveGrowingSpace = ArchiveGrowingSpace(gsRepo, clock),
                archiveSpatialObject = ArchiveSpatialObject(soRepo, clock),
                growingSpaceHistoryRepository = gsHistory,
                spatialObjectHistoryRepository = soHistory,
            )
        return Triple(vm, gsRepo, soRepo)
    }

    private fun shapeFromGrowingSpace(space: GrowingSpace) =
        MapShape(space.id, MapShapeKind.GrowingSpace(space.spaceType), space.geometry!!)

    private fun shapeFromSpatialObject(obj: SpatialObject) = MapShape(obj.id, MapShapeKind.SpatialObject(obj.objectType), obj.geometry)

    private fun sampleSpace() =
        GrowingSpace(
            id = "gs_1",
            gardenId = "g_1",
            name = "Bed",
            spaceType = GrowingSpaceType.RAISED_BED,
            status = RecordStatus.ACTIVE,
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

    private fun sampleObject() =
        SpatialObject(
            id = "so_1",
            gardenId = "g_1",
            objectType = InfrastructureType.FENCE,
            geometry =
                Geometry.Polygon(
                    listOf(
                        Coordinate(0.0, 0.0),
                        Coordinate(5.0, 0.0),
                        Coordinate(5.0, 1.0),
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

    @Test
    fun `selectShape updates selectedShapeId`() =
        runTest {
            val (vm, _, _) = newViewModel()
            vm.selectShape("gs_1")
            assertEquals("gs_1", vm.uiState.value.selectedShapeId)
        }

    @Test
    fun `clearSelection clears selectedShapeId`() =
        runTest {
            val (vm, _, _) = newViewModel()
            vm.selectShape("gs_1")
            vm.clearSelection()
            assertNull(vm.uiState.value.selectedShapeId)
        }

    @Test
    fun `saveGeometry on growing space delegates to UpdateGrowingSpaceGeometry`() =
        runTest {
            val space = sampleSpace()
            val (vm, gsRepo, _) = newViewModel()
            gsRepo.insert(space)
            val newGeom =
                Geometry.Polygon(
                    listOf(
                        Coordinate(0.0, 0.0),
                        Coordinate(2.0, 0.0),
                        Coordinate(2.0, 1.0),
                        Coordinate(0.0, 1.0),
                        Coordinate(0.0, 0.0),
                    ),
                )
            vm.saveGeometry(shapeFromGrowingSpace(space), newGeom)
            testScheduler.advanceUntilIdle()
            assertEquals(newGeom, gsRepo.snapshot().first().geometry)
            assertFalse(vm.uiState.value.isSaving)
        }

    @Test
    fun `saveGeometry on spatial object delegates to UpdateSpatialObjectGeometry`() =
        runTest {
            val obj = sampleObject()
            val (vm, _, soRepo) = newViewModel()
            soRepo.insert(obj)
            val newGeom =
                Geometry.Polygon(
                    listOf(
                        Coordinate(0.0, 0.0),
                        Coordinate(6.0, 0.0),
                        Coordinate(6.0, 1.0),
                        Coordinate(0.0, 1.0),
                        Coordinate(0.0, 0.0),
                    ),
                )
            vm.saveGeometry(shapeFromSpatialObject(obj), newGeom)
            testScheduler.advanceUntilIdle()
            assertEquals(newGeom, soRepo.snapshot().first().geometry)
        }

    @Test
    fun `saveGeometry on area does nothing`() =
        runTest {
            val (vm, gsRepo, soRepo) = newViewModel()
            val areaShape =
                MapShape(
                    id = "area_1",
                    kind = MapShapeKind.Area,
                    geometry = Geometry.Point(Coordinate(0.0, 0.0)),
                )
            vm.saveGeometry(areaShape, Geometry.Point(Coordinate(5.0, 5.0)))
            testScheduler.advanceUntilIdle()
            assertEquals(0, gsRepo.snapshot().size)
            assertEquals(0, soRepo.snapshot().size)
        }

    @Test
    fun `archiveSelected archives the spatial object and clears selection`() =
        runTest {
            val obj = sampleObject()
            val (vm, _, soRepo) = newViewModel()
            soRepo.insert(obj)
            vm.selectShape(obj.id)
            vm.archiveSelected(shapeFromSpatialObject(obj))
            testScheduler.advanceUntilIdle()
            assertEquals(RecordStatus.ARCHIVED, soRepo.snapshot().first().status)
            assertNull(vm.uiState.value.selectedShapeId)
        }

    @Test
    fun `undoLastEdit reads latest spatial object history and applies it`() =
        runTest {
            val obj = sampleObject()
            val priorGeom = Geometry.Point(Coordinate(3.0, 3.0))
            val soHistory =
                FakeSpatialObjectHistoryRepository().apply {
                    seed(
                        listOf(
                            SpatialObjectHistory(
                                id = "hist_1",
                                spatialObjectId = obj.id,
                                geometry = priorGeom,
                                validFrom = 0L,
                                validTo = 100L,
                                recordedAt = 100L,
                                reason = "Original",
                            ),
                        ),
                    )
                }
            val (vm, _, soRepo) = newViewModel(soHistory = soHistory)
            soRepo.insert(obj)
            vm.undoLastEdit(shapeFromSpatialObject(obj))
            testScheduler.advanceUntilIdle()
            assertEquals(priorGeom, soRepo.snapshot().first().geometry)
        }

    @Test
    fun `undoLastEdit is a no-op when no history exists`() =
        runTest {
            val obj = sampleObject()
            val (vm, _, soRepo) = newViewModel()
            soRepo.insert(obj)
            vm.undoLastEdit(shapeFromSpatialObject(obj))
            testScheduler.advanceUntilIdle()
            assertEquals(obj.geometry, soRepo.snapshot().first().geometry)
        }

    @Test
    fun `onErrorHandled clears error flag`() =
        runTest {
            val (vm, _, _) = newViewModel()
            vm.saveGeometry(
                MapShape("missing", MapShapeKind.SpatialObject(InfrastructureType.PATH), Geometry.Point(Coordinate(0.0, 0.0))),
                Geometry.Point(Coordinate(1.0, 1.0)),
            )
            testScheduler.advanceUntilIdle()
            assertTrue(vm.uiState.value.error)
            vm.onErrorHandled()
            assertFalse(vm.uiState.value.error)
        }
}
