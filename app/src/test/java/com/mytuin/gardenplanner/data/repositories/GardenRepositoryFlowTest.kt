package com.mytuin.gardenplanner.data.repositories

import app.cash.turbine.test
import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeGardenRepository
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Turbine proof (A141).
 *
 * Exercises Flow emissions from FakeGardenRepository. This proves
 * Turbine is on the classpath and usable for testing reactive
 * repositories.
 *
 * Uses FakeGardenRepository rather than Room because the point is the
 * Flow behaviour, not persistence. Repository-behaviour-against-Room
 * is covered by GardenRepositoryTest in androidTest.
 *
 * TESTING_STRATEGY §4: unit test, no Android runtime.
 */
class GardenRepositoryFlowTest {
    private val repository = FakeGardenRepository()

    @Test
    fun observeGardens_emits_empty_list_initially() =
        runTest {
            repository.observeGardens().test {
                assertEquals(emptyList<Garden>(), awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun observeGardens_emits_after_insert() =
        runTest {
            repository.observeGardens().test {
                assertEquals(0, awaitItem().size)

                repository.insert(sampleGarden("garden_1", "Alpha"))
                assertEquals(listOf("Alpha"), awaitItem().map { it.name })

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun observeGardens_emits_after_each_insert_in_order() =
        runTest {
            repository.observeGardens().test {
                assertEquals(0, awaitItem().size)

                repository.insert(sampleGarden("garden_1", "Alpha"))
                assertEquals(listOf("Alpha"), awaitItem().map { it.name })

                repository.insert(sampleGarden("garden_2", "Beta"))
                assertEquals(listOf("Alpha", "Beta"), awaitItem().map { it.name })

                cancelAndIgnoreRemainingEvents()
            }
        }

    private fun sampleGarden(
        id: String,
        name: String,
    ): Garden =
        Garden(
            id = id,
            name = name,
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
        )
}
