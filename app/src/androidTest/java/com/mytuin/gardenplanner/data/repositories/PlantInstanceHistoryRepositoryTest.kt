package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.database.GardenDatabaseFactory
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.data.entities.GrowingSpaceEntity
import com.mytuin.gardenplanner.data.entities.PlantEntity
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.NewPlantInstanceLocation
import com.mytuin.gardenplanner.domain.model.garden.PlantInstance
import com.mytuin.gardenplanner.domain.repository.PlantInstanceHistoryRepository
import com.mytuin.gardenplanner.domain.repository.PlantInstanceRepository
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.PlantLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.platform.identifiers.UuidIdGenerator
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PlantInstanceHistoryRepositoryTest {
    private lateinit var db: GardenDatabase
    private lateinit var instanceRepository: PlantInstanceRepository
    private lateinit var historyRepository: PlantInstanceHistoryRepository

    private val gardenId = "garden_00000000-0000-0000-0000-000000000001"
    private val plantId = "plant_00000000-0000-0000-0000-000000000001"
    private val spaceAId = "growingspace_00000000-0000-0000-0000-000000000001"
    private val spaceBId = "growingspace_00000000-0000-0000-0000-000000000002"
    private val instanceId = "plantinstance_00000000-0000-0000-0000-000000000001"

    @Before
    fun setUp() =
        runBlocking {
            val context = ApplicationProvider.getApplicationContext<Context>()
            db =
                Room
                    .inMemoryDatabaseBuilder(context, GardenDatabase::class.java)
                    .addCallback(GardenDatabaseFactory.foreignKeysCallback)
                    .build()
            instanceRepository =
                PlantInstanceRepositoryImpl(
                    db = db,
                    plantInstanceDao = db.plantInstanceDao(),
                    plantInstanceHistoryDao = db.plantInstanceHistoryDao(),
                    idGenerator = UuidIdGenerator(),
                    gardenDao = db.gardenDao(),
                    plantDao = db.plantDao(),
                    cultivarDao = db.cultivarDao(),
                    growingSpaceDao = db.growingSpaceDao(),
                    spatialObjectDao = db.spatialObjectDao(),
                )
            historyRepository =
                PlantInstanceHistoryRepositoryImpl(
                    plantInstanceHistoryDao = db.plantInstanceHistoryDao(),
                )

            db.gardenDao().insert(sampleGarden())
            db.plantDao().insert(samplePlant())
            db.growingSpaceDao().insert(sampleSpace(spaceAId, "Bed A"))
            db.growingSpaceDao().insert(sampleSpace(spaceBId, "Bed B"))
            instanceRepository.insert(sampleInstance())
        }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun no_history_exists_until_first_change() =
        runBlocking {
            assertEquals(0, historyRepository.getHistoryFor(instanceId).size)
        }

    @Test
    fun first_change_writes_one_history_row_capturing_prior_state() =
        runBlocking {
            instanceRepository.updateLocation(
                id = instanceId,
                location = NewPlantInstanceLocation(growingSpaceId = spaceBId),
                effectiveAt = 1_700_000_500_000L,
                reason = "Rotation",
            )

            val history = historyRepository.getHistoryFor(instanceId)
            assertEquals(1, history.size)
            val row = history.first()

            assertEquals(spaceAId, row.growingSpaceId)
            assertNull(row.spatialObjectId)
            assertNull(row.geometry)
            assertEquals(1_700_000_000_000L, row.validFrom)
            assertEquals(1_700_000_500_000L, row.validTo)
            assertEquals(1_700_000_500_000L, row.recordedAt)
            assertEquals("Rotation", row.reason)

            // Current state now reflects the new location.
            assertEquals(
                spaceBId,
                instanceRepository.getPlantInstance(instanceId)?.growingSpaceId,
            )
        }

    @Test
    fun second_change_chains_valid_from_to_previous_valid_to() =
        runBlocking {
            instanceRepository.updateLocation(
                id = instanceId,
                location = NewPlantInstanceLocation(growingSpaceId = spaceBId),
                effectiveAt = 1_700_000_500_000L,
                reason = null,
            )
            instanceRepository.updateLocation(
                id = instanceId,
                location = NewPlantInstanceLocation(growingSpaceId = null),
                effectiveAt = 1_700_000_900_000L,
                reason = null,
            )

            val history = historyRepository.getHistoryFor(instanceId)
            assertEquals(2, history.size)

            // Sorted DESC by valid_from.
            val secondRow = history[0]
            val firstRow = history[1]

            assertEquals(1_700_000_000_000L, firstRow.validFrom)
            assertEquals(1_700_000_500_000L, firstRow.validTo)
            assertEquals(spaceAId, firstRow.growingSpaceId)

            assertEquals(1_700_000_500_000L, secondRow.validFrom)
            assertEquals(1_700_000_900_000L, secondRow.validTo)
            assertEquals(spaceBId, secondRow.growingSpaceId)

            // Current state reflects the final change (cleared).
            assertNull(
                instanceRepository.getPlantInstance(instanceId)?.growingSpaceId,
            )
        }

    @Test
    fun getHistoryAsOf_returns_the_row_effective_at_that_instant() =
        runBlocking {
            instanceRepository.updateLocation(
                id = instanceId,
                location = NewPlantInstanceLocation(growingSpaceId = spaceBId),
                effectiveAt = 1_700_000_500_000L,
                reason = null,
            )
            instanceRepository.updateLocation(
                id = instanceId,
                location = NewPlantInstanceLocation(growingSpaceId = null),
                effectiveAt = 1_700_000_900_000L,
                reason = null,
            )

            // During period of the first history row.
            val atTime = historyRepository.getHistoryAsOf(instanceId, 1_700_000_250_000L)
            assertEquals(spaceAId, atTime?.growingSpaceId)

            // During period of the second history row.
            val laterTime = historyRepository.getHistoryAsOf(instanceId, 1_700_000_700_000L)
            assertEquals(spaceBId, laterTime?.growingSpaceId)

            // After the latest edit — no history row covers this. The
            // current row does.
            val after = historyRepository.getHistoryAsOf(instanceId, 1_700_001_000_000L)
            assertNull(after)
        }

    @Test
    fun geometry_change_is_captured_in_history() =
        runBlocking {
            val initialPoint = Geometry.Point(Coordinate(0.0, 0.0))
            instanceRepository.updateLocation(
                id = instanceId,
                location = NewPlantInstanceLocation(geometry = initialPoint),
                effectiveAt = 1_700_000_500_000L,
                reason = null,
            )
            val newPoint = Geometry.Point(Coordinate(5.0, 5.0))
            instanceRepository.updateLocation(
                id = instanceId,
                location = NewPlantInstanceLocation(geometry = newPoint),
                effectiveAt = 1_700_000_900_000L,
                reason = null,
            )

            // History captures the prior state at each edit.
            // getHistoryFor orders by valid_from DESC:
            //   history[0] is the row written by the second edit:
            //              it captures the state before that edit
            //              (initialPoint), valid_from = 500.
            //   history[1] is the row written by the first edit:
            //              it captures the state before that edit
            //              (null, since the instance was created
            //              with no geometry), valid_from = created_at.
            // The current row is newPoint.
            val history = historyRepository.getHistoryFor(instanceId)
            assertEquals(2, history.size)
            assertNull(history[1].geometry)
            assertEquals(initialPoint, history[0].geometry)

            assertEquals(
                newPoint,
                instanceRepository.getPlantInstance(instanceId)?.geometry,
            )
        }

    @Test
    fun history_row_survives_archival_of_referenced_space() =
        runBlocking {
            // H3: history's spatial-context columns carry no FK. If
            // the referenced growing space is archived, the history
            // row remains readable.
            instanceRepository.updateLocation(
                id = instanceId,
                location = NewPlantInstanceLocation(growingSpaceId = spaceBId),
                effectiveAt = 1_700_000_500_000L,
                reason = null,
            )
            db.growingSpaceDao().updateStatus(spaceAId, RecordStatus.ARCHIVED, 1_700_001_000_000L)

            val history = historyRepository.getHistoryFor(instanceId)
            assertEquals(1, history.size)
            assertEquals(spaceAId, history.first().growingSpaceId)
        }

    @Test
    fun update_throws_NotFoundError_when_instance_missing() =
        runBlocking {
            try {
                instanceRepository.updateLocation(
                    id = "plantinstance_does_not_exist",
                    location = NewPlantInstanceLocation(),
                    effectiveAt = 1_700_000_500_000L,
                    reason = null,
                )
                fail("Expected NotFoundError; updateLocation succeeded")
            } catch (expected: NotFoundError) {
                assertEquals("PlantInstance", expected.entityType)
            }

            assertEquals(0, historyRepository.getHistoryFor(instanceId).size)
        }

    @Test
    fun update_throws_ValidationError_when_new_growing_space_missing() =
        runBlocking {
            try {
                instanceRepository.updateLocation(
                    id = instanceId,
                    location = NewPlantInstanceLocation(growingSpaceId = "growingspace_does_not_exist"),
                    effectiveAt = 1_700_000_500_000L,
                    reason = null,
                )
                fail("Expected ValidationError; updateLocation succeeded")
            } catch (expected: ValidationError) {
                assertEquals("growing_space_id", expected.field)
            }

            // No history row written on pre-check failure.
            assertEquals(0, historyRepository.getHistoryFor(instanceId).size)
        }

    @Test
    fun current_instance_geometry_is_updated_on_change() =
        runBlocking {
            val newPoint = Geometry.Point(Coordinate(3.0, 4.0))
            instanceRepository.updateLocation(
                id = instanceId,
                location = NewPlantInstanceLocation(geometry = newPoint),
                effectiveAt = 1_700_000_500_000L,
                reason = null,
            )

            val instance = instanceRepository.getPlantInstance(instanceId)
            assertNotNull(instance)
            assertEquals(newPoint, instance!!.geometry)
        }

    private fun sampleGarden(): GardenEntity =
        GardenEntity(
            id = gardenId,
            name = "Test Garden",
            description = null,
            country_code = null,
            region = null,
            locality = null,
            latitude = null,
            longitude = null,
            timezone = null,
            hemisphere = Hemisphere.UNKNOWN,
            status = RecordStatus.DRAFT,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
        )

    private fun samplePlant(): PlantEntity =
        PlantEntity(
            id = plantId,
            canonical_name = "Test plant",
            scientific_name = null,
            genus = null,
            species = null,
            family = null,
            lifecycle = PlantLifecycle.PERENNIAL,
            description = null,
            status = RecordStatus.ACTIVE,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
        )

    private fun sampleSpace(
        id: String,
        name: String,
    ): GrowingSpaceEntity =
        GrowingSpaceEntity(
            id = id,
            garden_id = gardenId,
            name = name,
            space_type = GrowingSpaceType.RAISED_BED,
            status = RecordStatus.ACTIVE,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
            geometry_type = null,
            geometry_data = null,
            length = null,
            width = null,
            height = null,
            diameter = null,
            area = null,
            volume = null,
            description = null,
            area_id = null,
            notes = null,
        )

    private fun sampleInstance(): PlantInstance =
        PlantInstance(
            id = instanceId,
            gardenId = gardenId,
            plantId = plantId,
            status = RecordStatus.ACTIVE,
            cultivarId = null,
            growingSpaceId = spaceAId,
            spatialObjectId = null,
            name = "Tomato in Bed A",
            quantity = null,
            plannedDate = null,
            plantedDate = null,
            expectedEndDate = null,
            removedDate = null,
            plantingStock = null,
            lifecycle = null,
            geometry = null,
            notes = null,
            createdAt = 1_700_000_000_000L,
            updatedAt = 1_700_000_000_000L,
        )
}
