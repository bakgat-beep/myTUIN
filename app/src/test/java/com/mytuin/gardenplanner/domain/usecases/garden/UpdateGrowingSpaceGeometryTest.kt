package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeGrowingSpaceRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Use case tests for UpdateGrowingSpaceGeometry.
 * TESTING_STRATEGY §4: pure unit tests, no Android runtime.
 */
class UpdateGrowingSpaceGeometryTest {

    private val repository = FakeGrowingSpaceRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_000_500_000L)

    private val useCase = UpdateGrowingSpaceGeometry(
        repository = repository,
        clock = clock,
    )

    private val existingId = "growingspace_test_0001"

    @Before
    fun setUp() = runBlocking {
        repository.insert(
            GrowingSpace(
                id = existingId,
                gardenId = "garden_test_0001",
                name = "Bed 2",
                spaceType = GrowingSpaceType.RAISED_BED,
                status = RecordStatus.ACTIVE,
                geometry = Geometry.Point(Coordinate(0.0, 0.0)),
                lengthMetres = null,
                widthMetres = null,
                heightMetres = null,
                diameterMetres = null,
                areaSquareMetres = null,
                volumeCubicMetres = null,
                description = null,
                notes = null,
                createdAt = 1_700_000_000_000L,
                updatedAt = 1_700_000_000_000L,
            )
        )
    }

    @Test
    fun invoke_forwards_new_geometry_to_repository() = runBlocking {
        val newGeometry = Geometry.LineString(
            listOf(Coordinate(0.0, 0.0), Coordinate(1.0, 1.0))
        )

        useCase(existingId, newGeometry)

        assertEquals(1, repository.updateCalls().size)
        assertEquals(newGeometry, repository.updateCalls().first().newGeometry)
    }

    @Test
    fun invoke_supplies_effectiveAt_from_clock() = runBlocking {
        useCase(existingId, null)

        assertEquals(1_700_000_500_000L, repository.updateCalls().first().effectiveAt)
    }

    @Test
    fun invoke_passes_reason_through() = runBlocking {
        useCase(existingId, null, reason = "Survey correction")

        assertEquals("Survey correction", repository.updateCalls().first().reason)
    }

    @Test
    fun invoke_defaults_reason_to_null() = runBlocking {
        useCase(existingId, null)

        assertNull(repository.updateCalls().first().reason)
    }

    @Test
    fun invoke_updates_current_geometry_on_the_stored_space() = runBlocking {
        val newGeometry = Geometry.Point(Coordinate(5.0, 5.0))

        useCase(existingId, newGeometry)

        assertEquals(newGeometry, repository.snapshot().first().geometry)
        assertEquals(1_700_000_500_000L, repository.snapshot().first().updatedAt)
    }
}