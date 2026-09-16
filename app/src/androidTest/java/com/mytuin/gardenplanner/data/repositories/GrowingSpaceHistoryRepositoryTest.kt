package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.database.GardenDatabaseFactory
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.repository.GrowingSpaceHistoryRepository
import com.mytuin.gardenplanner.domain.repository.GrowingSpaceRepository
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.platform.identifiers.UuidIdGenerator
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GrowingSpaceHistoryRepositoryTest {

    private lateinit var db: GardenDatabase
    private lateinit var spaceRepository: GrowingSpaceRepository
    private lateinit var historyRepository: GrowingSpaceHistoryRepository

    private val gardenId = "garden_00000000-0000-0000-0000-000000000001"
    private val spaceId = "growingspace_00000000-0000-0000-0000-000000000001"

    @Before
    fun setUp() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, GardenDatabase::class.java)
            .addCallback(GardenDatabaseFactory.foreignKeysCallback)
            .build()
        spaceRepository = GrowingSpaceRepositoryImpl(
            db = db,
            growingSpaceDao = db.growingSpaceDao(),
            growingSpaceHistoryDao = db.growingSpaceHistoryDao(),
            idGenerator = UuidIdGenerator(),
        )
        historyRepository = GrowingSpaceHistoryRepositoryImpl(
            growingSpaceHistoryDao = db.growingSpaceHistoryDao(),
        )

        db.gardenDao().insert(sampleGarden())
        spaceRepository.insert(sampleSpaceWithInitialGeometry())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun no_history_exists_until_first_edit() = runBlocking {
        assertEquals(0, historyRepository.getHistoryForSpace(spaceId).size)
    }

    @Test
    fun edit_writes_one_history_row_capturing_prior_state() = runBlocking {
        val newGeometry = Geometry.Polygon(
            listOf(
                Coordinate(0.0, 0.0),
                Coordinate(4.0, 0.0),
                Coordinate(4.0, 1.0),
                Coordinate(0.0, 1.0),
                Coordinate(0.0, 0.0),
            )
        )

        spaceRepository.updateGeometry(
            id = spaceId,
            newGeometry = newGeometry,
            effectiveAt = 1_700_000_500_000L,
            reason = "Survey correction",
        )

        val history = historyRepository.getHistoryForSpace(spaceId)
        assertEquals(1, history.size)
        val row = history.first()

        // Prior geometry captured
        assertEquals(
            Geometry.Point(Coordinate(0.0, 0.0)),
            row.geometry,
        )
        assertEquals(1_700_000_000_000L, row.validFrom)
        assertEquals(1_700_000_500_000L, row.validTo)
        assertEquals(1_700_000_500_000L, row.recordedAt)
        assertEquals("Survey correction", row.reason)

        // Current state updated
        assertEquals(newGeometry, spaceRepository.getGrowingSpace(spaceId)?.geometry)
    }

    @Test
    fun second_edit_chains_valid_from_to_previous_valid_to() = runBlocking {
        spaceRepository.updateGeometry(spaceId, Geometry.Point(Coordinate(1.0, 1.0)), 1_700_000_500_000L, null)
        spaceRepository.updateGeometry(spaceId, Geometry.Point(Coordinate(2.0, 2.0)), 1_700_000_900_000L, null)

        val history = historyRepository.getHistoryForSpace(spaceId)
        assertEquals(2, history.size)

        // Sorted DESC by valid_from.
        val secondRow = history[0]
        val firstRow = history[1]

        assertEquals(1_700_000_000_000L, firstRow.validFrom)
        assertEquals(1_700_000_500_000L, firstRow.validTo)

        assertEquals(1_700_000_500_000L, secondRow.validFrom)
        assertEquals(1_700_000_900_000L, secondRow.validTo)

        // Current state
        assertEquals(
            Geometry.Point(Coordinate(2.0, 2.0)),
            spaceRepository.getGrowingSpace(spaceId)?.geometry,
        )
    }

    @Test
    fun getHistoryAsOf_returns_the_row_effective_at_that_instant() = runBlocking {
        spaceRepository.updateGeometry(spaceId, Geometry.Point(Coordinate(1.0, 1.0)), 1_700_000_500_000L, null)
        spaceRepository.updateGeometry(spaceId, Geometry.Point(Coordinate(2.0, 2.0)), 1_700_000_900_000L, null)

        // During period of the first history row
        val atTime = historyRepository.getHistoryAsOf(spaceId, 1_700_000_250_000L)
        assertEquals(Geometry.Point(Coordinate(0.0, 0.0)), atTime?.geometry)

        // During period of the second history row
        val laterTime = historyRepository.getHistoryAsOf(spaceId, 1_700_000_700_000L)
        assertEquals(Geometry.Point(Coordinate(1.0, 1.0)), laterTime?.geometry)

        // After the latest edit — no history row covers this. The
        // current row does.
        val after = historyRepository.getHistoryAsOf(spaceId, 1_700_001_000_000L)
        assertNull(after)
    }

    @Test
    fun history_is_not_written_when_editing_a_nonexistent_space() = runBlocking {
        try {
            spaceRepository.updateGeometry(
                id = "growingspace_does_not_exist",
                newGeometry = null,
                effectiveAt = 1_700_000_500_000L,
                reason = null,
            )
        } catch (expected: IllegalStateException) {
            // expected
        }

        assertEquals(0, historyRepository.getHistoryForSpace(spaceId).size)
    }

    private fun sampleGarden(): GardenEntity = GardenEntity(
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

    private fun sampleSpaceWithInitialGeometry(): GrowingSpace = GrowingSpace(
        id = spaceId,
        gardenId = gardenId,
        name = "Bed 2",
        spaceType = GrowingSpaceType.RAISED_BED,
        status = RecordStatus.ACTIVE,
        geometry = Geometry.Point(Coordinate(0.0, 0.0)),
        lengthMetres = 3.0,
        widthMetres = 1.2,
        heightMetres = null,
        diameterMetres = null,
        areaSquareMetres = null,
        volumeCubicMetres = null,
        description = null,
        notes = null,
        createdAt = 1_700_000_000_000L,
        updatedAt = 1_700_000_000_000L,
    )
}