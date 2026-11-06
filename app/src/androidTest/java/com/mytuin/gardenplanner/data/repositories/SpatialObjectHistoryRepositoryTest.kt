package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.database.GardenDatabaseFactory
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.repository.SpatialObjectHistoryRepository
import com.mytuin.gardenplanner.domain.repository.SpatialObjectRepository
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.platform.identifiers.UuidIdGenerator
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SpatialObjectHistoryRepositoryTest {
    private lateinit var db: GardenDatabase
    private lateinit var objectRepository: SpatialObjectRepository
    private lateinit var historyRepository: SpatialObjectHistoryRepository

    private val gardenId = "garden_00000000-0000-0000-0000-000000000001"
    private val objectId = "spatialobject_00000000-0000-0000-0000-000000000001"

    @Before
    fun setUp() =
        runBlocking {
            val context = ApplicationProvider.getApplicationContext<Context>()
            db =
                Room
                    .inMemoryDatabaseBuilder(context, GardenDatabase::class.java)
                    .addCallback(GardenDatabaseFactory.foreignKeysCallback)
                    .build()
            objectRepository =
                SpatialObjectRepositoryImpl(
                    db = db,
                    spatialObjectDao = db.spatialObjectDao(),
                    spatialObjectHistoryDao = db.spatialObjectHistoryDao(),
                    idGenerator = UuidIdGenerator(),
                    gardenDao = db.gardenDao(),
                    areaDao = db.areaDao(),
                )
            historyRepository =
                SpatialObjectHistoryRepositoryImpl(
                    spatialObjectHistoryDao = db.spatialObjectHistoryDao(),
                )

            db.gardenDao().insert(sampleGarden())
            objectRepository.insert(sampleObjectWithInitialGeometry())
        }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun no_history_exists_until_first_edit() =
        runBlocking {
            assertEquals(0, historyRepository.getHistoryForObject(objectId).size)
        }

    @Test
    fun edit_writes_one_history_row_capturing_prior_state() =
        runBlocking {
            val initialGeometry = sampleObjectWithInitialGeometry().geometry
            val newGeometry =
                Geometry.Polygon(
                    listOf(
                        Coordinate(0.0, 0.0),
                        Coordinate(4.0, 0.0),
                        Coordinate(4.0, 1.0),
                        Coordinate(0.0, 1.0),
                        Coordinate(0.0, 0.0),
                    ),
                )

            objectRepository.updateGeometry(
                id = objectId,
                newGeometry = newGeometry,
                effectiveAt = 1_700_000_500_000L,
                reason = "Edited on map",
            )

            val history = historyRepository.getHistoryForObject(objectId)
            assertEquals(1, history.size)
            val row = history.first()

            assertEquals(initialGeometry, row.geometry)
            assertEquals(1_700_000_000_000L, row.validFrom)
            assertEquals(1_700_000_500_000L, row.validTo)
            assertEquals(1_700_000_500_000L, row.recordedAt)
            assertEquals("Edited on map", row.reason)

            assertEquals(newGeometry, objectRepository.getSpatialObject(objectId)?.geometry)
        }

    @Test
    fun second_edit_chains_valid_from_to_previous_valid_to() =
        runBlocking {
            objectRepository.updateGeometry(
                objectId,
                Geometry.Polygon(
                    listOf(
                        Coordinate(0.0, 0.0),
                        Coordinate(4.0, 0.0),
                        Coordinate(4.0, 1.0),
                        Coordinate(0.0, 1.0),
                        Coordinate(0.0, 0.0),
                    ),
                ),
                1_700_000_500_000L,
                null,
            )
            objectRepository.updateGeometry(
                objectId,
                Geometry.Polygon(
                    listOf(
                        Coordinate(0.0, 0.0),
                        Coordinate(5.0, 0.0),
                        Coordinate(5.0, 1.0),
                        Coordinate(0.0, 1.0),
                        Coordinate(0.0, 0.0),
                    ),
                ),
                1_700_000_900_000L,
                null,
            )

            val history = historyRepository.getHistoryForObject(objectId)
            assertEquals(2, history.size)

            val secondRow = history[0]
            val firstRow = history[1]

            assertEquals(1_700_000_000_000L, firstRow.validFrom)
            assertEquals(1_700_000_500_000L, firstRow.validTo)
            assertEquals(1_700_000_500_000L, secondRow.validFrom)
            assertEquals(1_700_000_900_000L, secondRow.validTo)
        }

    @Test
    fun getHistoryAsOf_returns_the_row_effective_at_that_instant() =
        runBlocking {
            objectRepository.updateGeometry(
                objectId,
                Geometry.Polygon(
                    listOf(
                        Coordinate(0.0, 0.0),
                        Coordinate(4.0, 0.0),
                        Coordinate(4.0, 1.0),
                        Coordinate(0.0, 1.0),
                        Coordinate(0.0, 0.0),
                    ),
                ),
                1_700_000_500_000L,
                null,
            )

            val atTime = historyRepository.getHistoryAsOf(objectId, 1_700_000_250_000L)
            assertEquals(sampleObjectWithInitialGeometry().geometry, atTime?.geometry)

            val afterAll = historyRepository.getHistoryAsOf(objectId, 1_700_001_000_000L)
            assertNull(afterAll)
        }

    @Test
    fun history_is_not_written_when_editing_a_nonexistent_object() =
        runBlocking {
            try {
                objectRepository.updateGeometry(
                    id = "spatialobject_does_not_exist",
                    newGeometry =
                        Geometry.Polygon(
                            listOf(
                                Coordinate(0.0, 0.0),
                                Coordinate(1.0, 0.0),
                                Coordinate(1.0, 1.0),
                                Coordinate(0.0, 1.0),
                                Coordinate(0.0, 0.0),
                            ),
                        ),
                    effectiveAt = 1_700_000_500_000L,
                    reason = null,
                )
                fail("Expected NotFoundError; updateGeometry succeeded")
            } catch (expected: NotFoundError) {
                assertEquals("SpatialObject", expected.entityType)
                assertEquals("spatialobject_does_not_exist", expected.id)
            }

            assertEquals(0, historyRepository.getHistoryForObject(objectId).size)
        }

    @Test
    fun archive_preserves_history_rows_and_current_row() =
        runBlocking {
            objectRepository.updateGeometry(
                id = objectId,
                newGeometry =
                    Geometry.Polygon(
                        listOf(
                            Coordinate(0.0, 0.0),
                            Coordinate(4.0, 0.0),
                            Coordinate(4.0, 1.0),
                            Coordinate(0.0, 1.0),
                            Coordinate(0.0, 0.0),
                        ),
                    ),
                effectiveAt = 1_700_000_500_000L,
                reason = null,
            )
            assertEquals(1, historyRepository.getHistoryForObject(objectId).size)

            objectRepository.archive(objectId, 1_700_001_000_000L)

            val obj = objectRepository.getSpatialObject(objectId)
            assertEquals(RecordStatus.ARCHIVED, obj?.status)

            val history = historyRepository.getHistoryForObject(objectId)
            assertEquals(1, history.size)
            assertEquals(sampleObjectWithInitialGeometry().geometry, history.first().geometry)
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

    private fun sampleObjectWithInitialGeometry(): SpatialObject =
        SpatialObject(
            id = objectId,
            gardenId = gardenId,
            objectType = InfrastructureType.FENCE,
            geometry =
                Geometry.Polygon(
                    listOf(
                        Coordinate(0.0, 0.0),
                        Coordinate(3.0, 0.0),
                        Coordinate(3.0, 1.0),
                        Coordinate(0.0, 1.0),
                        Coordinate(0.0, 0.0),
                    ),
                ),
            status = RecordStatus.ACTIVE,
            name = "Back fence",
            description = null,
            areaId = null,
            notes = null,
            createdAt = 1_700_000_000_000L,
            updatedAt = 1_700_000_000_000L,
        )
}
