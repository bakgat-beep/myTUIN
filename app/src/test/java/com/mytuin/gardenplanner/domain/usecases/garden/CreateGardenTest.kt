package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.model.garden.NewGarden
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeGardenRepository
import com.mytuin.gardenplanner.testdoubles.FakeIdGenerator
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Use case tests for CreateGarden.
 *
 * TESTING_STRATEGY §4: unit tests, no Android runtime.
 * A47=A: use case tests are plain unit tests, using fakes.
 */
class CreateGardenTest {
    private val repository = FakeGardenRepository()
    private val idGenerator = FakeIdGenerator(nextGardenIdValue = "garden_test_0001")
    private val clock = FakeClock(nowMillisValue = 1_700_000_000_000L)

    private val createGarden =
        CreateGarden(
            repository = repository,
            idGenerator = idGenerator,
            clock = clock,
        )

    @Test
    fun invoke_returns_the_generated_id() =
        runBlocking {
            val id = createGarden(NewGarden(name = "Back Garden"))
            assertEquals("garden_test_0001", id)
        }

    @Test
    fun invoke_persists_a_garden_with_assigned_id_status_and_timestamps() =
        runBlocking {
            createGarden(NewGarden(name = "Back Garden"))

            val gardens = repository.snapshot()
            assertEquals(1, gardens.size)
            val garden = gardens.first()

            assertEquals("garden_test_0001", garden.id)
            assertEquals("Back Garden", garden.name)
            assertEquals(RecordStatus.DRAFT, garden.status)
            assertEquals(1_700_000_000_000L, garden.createdAt)
            assertEquals(1_700_000_000_000L, garden.updatedAt)
            assertEquals(1_700_000_000_000L, garden.createdAt)
        }

    @Test
    fun invoke_defaults_hemisphere_to_unknown_when_not_supplied() =
        runBlocking {
            createGarden(NewGarden(name = "Back Garden"))
            assertEquals(Hemisphere.UNKNOWN, repository.snapshot().first().hemisphere)
        }

    @Test
    fun invoke_preserves_supplied_hemisphere() =
        runBlocking {
            createGarden(NewGarden(name = "Southern Garden", hemisphere = Hemisphere.SOUTHERN))
            assertEquals(Hemisphere.SOUTHERN, repository.snapshot().first().hemisphere)
        }

    @Test
    fun invoke_preserves_optional_location_fields() =
        runBlocking {
            createGarden(
                NewGarden(
                    name = "Back Garden",
                    description = "Main vegetable plot",
                    countryCode = "NZ",
                    region = "Canterbury",
                    locality = "Christchurch",
                    latitude = -43.5321,
                    longitude = 172.6362,
                    timezone = "Pacific/Auckland",
                    hemisphere = Hemisphere.SOUTHERN,
                ),
            )

            val garden = repository.snapshot().first()
            assertEquals("Main vegetable plot", garden.description)
            assertEquals("NZ", garden.countryCode)
            assertEquals("Canterbury", garden.region)
            assertEquals("Christchurch", garden.locality)
            assertEquals(-43.5321, garden.latitude!!, 0.0)
            assertEquals(172.6362, garden.longitude!!, 0.0)
            assertEquals("Pacific/Auckland", garden.timezone)
            assertEquals(Hemisphere.SOUTHERN, garden.hemisphere)
        }

    @Test
    fun invoke_allows_two_gardens_with_the_same_name() =
        runBlocking {
            createGarden(NewGarden(name = "Back Garden"))
            createGarden(NewGarden(name = "Back Garden"))
            assertEquals(2, repository.snapshot().size)
        }
}
