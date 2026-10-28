package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpaceFields
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeGrowingSpaceRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class UpdateGrowingSpaceFieldsTest {
    private val repository = FakeGrowingSpaceRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_000_500_000L)
    private val useCase = UpdateGrowingSpaceFields(repository, clock)

    private val spaceId = "growingspace_test_0001"
    private val originalGeometry = Geometry.Point(Coordinate(1.0, 2.0))

    @Before
    fun setUp() =
        runBlocking {
            repository.insert(
                GrowingSpace(
                    id = spaceId,
                    gardenId = "garden_test_0001",
                    name = "Bed 2",
                    spaceType = GrowingSpaceType.RAISED_BED,
                    status = RecordStatus.ACTIVE,
                    geometry = originalGeometry,
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
                ),
            )
        }

    @Test
    fun invoke_updates_mutable_fields() =
        runBlocking {
            useCase(
                spaceId,
                GrowingSpaceFields(
                    name = "Bed 2 (renamed)",
                    spaceType = GrowingSpaceType.IN_GROUND_BED,
                    description = "Now in-ground",
                    lengthMetres = 4.0,
                    widthMetres = 1.5,
                    heightMetres = null,
                    diameterMetres = null,
                    areaSquareMetres = null,
                    volumeCubicMetres = null,
                    notes = "Moved to in-ground",
                ),
            )

            val updated = repository.snapshot().single()
            assertEquals("Bed 2 (renamed)", updated.name)
            assertEquals(GrowingSpaceType.IN_GROUND_BED, updated.spaceType)
            assertEquals("Now in-ground", updated.description)
            assertEquals(4.0, updated.lengthMetres!!, 0.0)
            assertEquals(1.5, updated.widthMetres!!, 0.0)
            assertEquals("Moved to in-ground", updated.notes)
        }

    @Test
    fun invoke_preserves_geometry_status_and_createdAt() =
        runBlocking {
            useCase(
                spaceId,
                GrowingSpaceFields(
                    name = "Bed 2",
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
            )

            val updated = repository.snapshot().single()
            assertEquals(originalGeometry, updated.geometry)
            assertEquals(RecordStatus.ACTIVE, updated.status)
            assertEquals(1_700_000_000_000L, updated.createdAt)
            assertEquals(1_700_000_500_000L, updated.updatedAt)
        }

    @Test
    fun invoke_trims_name_description_and_notes() =
        runBlocking {
            useCase(
                spaceId,
                GrowingSpaceFields(
                    name = "  Bed 2  ",
                    spaceType = GrowingSpaceType.RAISED_BED,
                    description = "  desc  ",
                    lengthMetres = null,
                    widthMetres = null,
                    heightMetres = null,
                    diameterMetres = null,
                    areaSquareMetres = null,
                    volumeCubicMetres = null,
                    notes = "  note  ",
                ),
            )

            val updated = repository.snapshot().single()
            assertEquals("Bed 2", updated.name)
            assertEquals("desc", updated.description)
            assertEquals("note", updated.notes)
        }

    @Test
    fun invoke_stores_blank_description_as_null() =
        runBlocking {
            useCase(
                spaceId,
                GrowingSpaceFields(
                    name = "Bed 2",
                    spaceType = GrowingSpaceType.RAISED_BED,
                    description = "   ",
                    lengthMetres = null,
                    widthMetres = null,
                    heightMetres = null,
                    diameterMetres = null,
                    areaSquareMetres = null,
                    volumeCubicMetres = null,
                    notes = null,
                ),
            )

            assertNull(repository.snapshot().single().description)
        }

    @Test
    fun invoke_throws_ValidationError_when_name_is_blank() =
        runBlocking {
            try {
                useCase(
                    spaceId,
                    GrowingSpaceFields(
                        name = "   ",
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
                )
                fail("Expected ValidationError")
            } catch (e: ValidationError) {
                assertEquals("name", e.field)
            }
        }

    @Test
    fun invoke_throws_ValidationError_when_length_is_negative() =
        runBlocking {
            try {
                useCase(
                    spaceId,
                    GrowingSpaceFields(
                        name = "Bed 2",
                        spaceType = GrowingSpaceType.RAISED_BED,
                        description = null,
                        lengthMetres = -1.0,
                        widthMetres = null,
                        heightMetres = null,
                        diameterMetres = null,
                        areaSquareMetres = null,
                        volumeCubicMetres = null,
                        notes = null,
                    ),
                )
                fail("Expected ValidationError")
            } catch (e: ValidationError) {
                assertEquals("length", e.field)
            }
        }

    @Test
    fun invoke_throws_ValidationError_when_width_is_zero() =
        runBlocking {
            try {
                useCase(
                    spaceId,
                    GrowingSpaceFields(
                        name = "Bed 2",
                        spaceType = GrowingSpaceType.RAISED_BED,
                        description = null,
                        lengthMetres = null,
                        widthMetres = 0.0,
                        heightMetres = null,
                        diameterMetres = null,
                        areaSquareMetres = null,
                        volumeCubicMetres = null,
                        notes = null,
                    ),
                )
                fail("Expected ValidationError")
            } catch (e: ValidationError) {
                assertEquals("width", e.field)
            }
        }

    @Test
    fun invoke_throws_NotFoundError_when_space_missing() =
        runBlocking {
            try {
                useCase(
                    "growingspace_does_not_exist",
                    GrowingSpaceFields(
                        name = "Bed 2",
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
                )
                fail("Expected NotFoundError")
            } catch (e: NotFoundError) {
                assertEquals("GrowingSpace", e.entityType)
            }
        }

    @Test
    fun invoke_records_the_update_call_with_clock_timestamp() =
        runBlocking {
            useCase(
                spaceId,
                GrowingSpaceFields(
                    name = "Bed 2",
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
            )

            val call = repository.updateFieldsCalls().single()
            assertEquals(spaceId, call.id)
            assertEquals(1_700_000_500_000L, call.updatedAt)
            assertNotNull(call.fields)
        }
}
