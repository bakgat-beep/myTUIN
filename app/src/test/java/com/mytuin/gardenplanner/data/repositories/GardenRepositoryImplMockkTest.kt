package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.NewGardenLocation
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

/**
 * MockK proof (A141).
 *
 * Exercises GardenRepositoryImpl against a mocked GardenDao. This
 * proves MockK is on the classpath and usable for repository unit
 * tests where standing up Room is unnecessary.
 *
 * TESTING_STRATEGY §78: fakes and mocks are both acceptable; this is
 * the mock exemplar. The fake-based tests remain as the primary
 * pattern for use case tests.
 *
 * If MockK's any() does not match a null argument for a nullable
 * parameter, replace any() with anyNullable() for the corresponding
 * parameter. Not observed to be necessary with MockK 1.13.13, but
 * recorded here.
 */
class GardenRepositoryImplMockkTest {

    private val gardenDao = mockk<GardenDao>()
    private val repository = GardenRepositoryImpl(gardenDao)

    @Test
    fun getGarden_returns_null_when_dao_returns_null() = runTest {
        coEvery { gardenDao.getById("garden_missing") } returns null

        assertNull(repository.getGarden("garden_missing"))
    }

    @Test
    fun getGarden_maps_entity_to_domain() = runTest {
        coEvery { gardenDao.getById("garden_x") } returns sampleEntity("garden_x")

        val garden = repository.getGarden("garden_x")

        assertNotNull(garden)
        assertEquals("garden_x", garden?.id)
        assertEquals("Test Garden", garden?.name)
        assertEquals(RecordStatus.DRAFT, garden?.status)
        assertEquals(Hemisphere.SOUTHERN, garden?.hemisphere)
    }

    @Test
    fun updateLocation_throws_NotFoundError_when_dao_reports_zero_rows() = runTest {
        coEvery {
            gardenDao.updateLocation(
                id = any(),
                countryCode = any(),
                region = any(),
                locality = any(),
                latitude = any(),
                longitude = any(),
                timezone = any(),
                hemisphere = any(),
                updatedAt = any(),
            )
        } returns 0

        var caught: NotFoundError? = null
        try {
            repository.updateLocation(
                id = "garden_missing",
                location = NewGardenLocation(),
                updatedAt = 1_700_000_000_000L,
            )
        } catch (e: NotFoundError) {
            caught = e
        }

        assertNotNull(caught)
        assertEquals("Garden", caught?.entityType)
        assertEquals("garden_missing", caught?.id)
    }

    private fun sampleEntity(id: String): GardenEntity = GardenEntity(
        id = id,
        name = "Test Garden",
        description = null,
        country_code = "NZ",
        region = null,
        locality = null,
        latitude = null,
        longitude = null,
        timezone = null,
        hemisphere = Hemisphere.SOUTHERN,
        status = RecordStatus.DRAFT,
        created_at = 1_700_000_000_000L,
        updated_at = 1_700_000_000_000L,
    )
}