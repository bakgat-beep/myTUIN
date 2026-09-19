package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.model.garden.NewGardenLocation
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeGardenRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

/**
 * Use case tests for UpdateGardenLocation.
 * TESTING_STRATEGY §4: pure unit tests, no Android runtime.
 */
class UpdateGardenLocationTest {
    private val repository = FakeGardenRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_000_500_000L)

    private val useCase =
        UpdateGardenLocation(
            repository = repository,
            clock = clock,
        )

    private val existingId = "garden_test_0001"

    @Before
    fun setUp() =
        runBlocking {
            repository.insert(
                Garden(
                    id = existingId,
                    name = "Back Garden",
                    description = null,
                    countryCode = null,
                    region = null,
                    locality = null,
                    latitude = null,
                    longitude = null,
                    timezone = null,
                    hemisphere = Hemisphere.UNKNOWN,
                    status = RecordStatus.DRAFT,
                    createdAt = 1_700_000_000_000L,
                    updatedAt = 1_700_000_000_000L,
                ),
            )
        }

    @Test
    fun invoke_forwards_location_fields_to_repository() =
        runBlocking {
            val location =
                NewGardenLocation(
                    countryCode = "NZ",
                    region = "Canterbury",
                    locality = "Christchurch",
                    latitude = -43.5321,
                    longitude = 172.6362,
                    timezone = "Pacific/Auckland",
                    hemisphere = Hemisphere.SOUTHERN,
                )

            useCase(existingId, location)

            val call = repository.updateLocationCalls().single()
            assertEquals(existingId, call.id)
            assertEquals(location, call.location)
        }

    @Test
    fun invoke_supplies_updatedAt_from_clock() =
        runBlocking {
            useCase(existingId, NewGardenLocation())

            assertEquals(
                1_700_000_500_000L,
                repository.updateLocationCalls().single().updatedAt,
            )
        }

    @Test
    fun invoke_applies_all_fields_to_the_stored_garden() =
        runBlocking {
            useCase(
                existingId,
                NewGardenLocation(
                    countryCode = "NZ",
                    region = "Canterbury",
                    locality = "Christchurch",
                    latitude = -43.5321,
                    longitude = 172.6362,
                    timezone = "Pacific/Auckland",
                    hemisphere = Hemisphere.SOUTHERN,
                ),
            )

            val garden = repository.snapshot().single()
            assertEquals("NZ", garden.countryCode)
            assertEquals("Canterbury", garden.region)
            assertEquals("Christchurch", garden.locality)
            assertEquals(-43.5321, garden.latitude!!, 0.0)
            assertEquals(172.6362, garden.longitude!!, 0.0)
            assertEquals("Pacific/Auckland", garden.timezone)
            assertEquals(Hemisphere.SOUTHERN, garden.hemisphere)
            assertEquals(1_700_000_500_000L, garden.updatedAt)
        }

    @Test
    fun invoke_with_all_nulls_clears_existing_location() =
        runBlocking {
            useCase(
                existingId,
                NewGardenLocation(
                    countryCode = "NZ",
                    hemisphere = Hemisphere.SOUTHERN,
                ),
            )

            useCase(existingId, NewGardenLocation())

            val garden = repository.snapshot().single()
            assertNull(garden.countryCode)
            assertNull(garden.region)
            assertNull(garden.locality)
            assertNull(garden.latitude)
            assertNull(garden.longitude)
            assertNull(garden.timezone)
            assertEquals(Hemisphere.UNKNOWN, garden.hemisphere)
        }

    @Test
    fun invoke_does_not_change_id_name_description_status_or_createdAt() =
        runBlocking {
            useCase(
                existingId,
                NewGardenLocation(
                    countryCode = "NZ",
                    hemisphere = Hemisphere.SOUTHERN,
                ),
            )

            val garden = repository.snapshot().single()
            assertEquals(existingId, garden.id)
            assertEquals("Back Garden", garden.name)
            assertNull(garden.description)
            assertEquals(RecordStatus.DRAFT, garden.status)
            assertEquals(1_700_000_000_000L, garden.createdAt)
        }
}
