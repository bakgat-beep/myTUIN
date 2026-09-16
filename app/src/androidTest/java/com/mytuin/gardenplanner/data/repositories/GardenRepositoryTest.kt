package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.database.GardenDatabaseFactory
import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.repository.GardenRepository
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.NewGardenLocation

/**
 * Repository tests for the Garden read/write path.
 *
 * PHASE_0_PROJECT_FOUNDATION §30 item 10. TESTING_STRATEGY §7, §56.
 */
@RunWith(AndroidJUnit4::class)
class GardenRepositoryTest {

    private lateinit var db: GardenDatabase
    private lateinit var repository: GardenRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, GardenDatabase::class.java)
            .addCallback(GardenDatabaseFactory.foreignKeysCallback)
            .build()
        repository = GardenRepositoryImpl(gardenDao = db.gardenDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getGarden_returns_null_when_no_garden_exists() = runBlocking {
        assertNull(repository.getGarden("garden_does_not_exist"))
    }

    @Test
    fun insert_then_get_round_trips_all_fields() = runBlocking {
        val garden = sampleGarden()
        repository.insert(garden)

        val retrieved = repository.getGarden(garden.id)

        assertEquals(garden, retrieved)
    }

    @Test
    fun insert_then_get_preserves_unknown_hemisphere() = runBlocking {
        val garden = sampleGarden().copy(
            id = "garden_00000000-0000-0000-0000-0000000000aa",
            hemisphere = Hemisphere.UNKNOWN,
        )
        repository.insert(garden)

        assertEquals(Hemisphere.UNKNOWN, repository.getGarden(garden.id)?.hemisphere)
    }

    @Test
    fun insert_then_get_preserves_null_location_fields() = runBlocking {
        val garden = sampleGarden().copy(
            id = "garden_00000000-0000-0000-0000-0000000000bb",
            countryCode = null,
            region = null,
            locality = null,
            latitude = null,
            longitude = null,
            timezone = null,
        )
        repository.insert(garden)

        val retrieved = repository.getGarden(garden.id)
        assertNull(retrieved?.countryCode)
        assertNull(retrieved?.region)
        assertNull(retrieved?.locality)
        assertNull(retrieved?.latitude)
        assertNull(retrieved?.longitude)
        assertNull(retrieved?.timezone)
    }

    @Test
    fun observeGardens_emits_inserted_rows_in_name_order() = runBlocking {
        repository.insert(sampleGarden().copy(
            id = "garden_00000000-0000-0000-0000-0000000000bb",
            name = "Zeta Garden",
        ))
        repository.insert(sampleGarden().copy(
            id = "garden_00000000-0000-0000-0000-0000000000aa",
            name = "Alpha Garden",
        ))

        val gardens = repository.observeGardens().first()
        assertEquals(2, gardens.size)
        assertEquals("Alpha Garden", gardens[0].name)
        assertEquals("Zeta Garden", gardens[1].name)
    }

    @Test
    fun insert_allows_duplicate_names() = runBlocking {
        repository.insert(sampleGarden().copy(id = "garden_00000000-0000-0000-0000-000000000001"))
        repository.insert(sampleGarden().copy(id = "garden_00000000-0000-0000-0000-000000000002"))
        assertEquals(2, repository.observeGardens().first().size)
    }

    @Test
    fun updateLocation_replaces_all_seven_location_fields() = runBlocking {
        val garden = sampleGarden()
        repository.insert(garden)

        repository.updateLocation(
            id = garden.id,
            location = NewGardenLocation(
                countryCode = "AU",
                region = "Victoria",
                locality = "Melbourne",
                latitude = -37.8136,
                longitude = 144.9631,
                timezone = "Australia/Melbourne",
                hemisphere = Hemisphere.SOUTHERN,
            ),
            updatedAt = 1_700_000_500_000L,
        )

        val updated = repository.getGarden(garden.id)!!
        assertEquals("AU", updated.countryCode)
        assertEquals("Victoria", updated.region)
        assertEquals("Melbourne", updated.locality)
        assertEquals(-37.8136, updated.latitude!!, 0.0)
        assertEquals(144.9631, updated.longitude!!, 0.0)
        assertEquals("Australia/Melbourne", updated.timezone)
        assertEquals(Hemisphere.SOUTHERN, updated.hemisphere)
        assertEquals(1_700_000_500_000L, updated.updatedAt)
    }

    @Test
    fun updateLocation_leaves_id_name_description_status_and_createdAt_alone() = runBlocking {
        val garden = sampleGarden()
        repository.insert(garden)

        repository.updateLocation(
            id = garden.id,
            location = NewGardenLocation(
                countryCode = "AU",
                hemisphere = Hemisphere.SOUTHERN,
            ),
            updatedAt = 1_700_000_500_000L,
        )

        val updated = repository.getGarden(garden.id)!!
        assertEquals(garden.id, updated.id)
        assertEquals(garden.name, updated.name)
        assertEquals(garden.description, updated.description)
        assertEquals(garden.status, updated.status)
        assertEquals(garden.createdAt, updated.createdAt)
    }

    @Test
    fun updateLocation_with_nulls_clears_existing_values() = runBlocking {
        val garden = sampleGarden()
        repository.insert(garden)

        repository.updateLocation(
            id = garden.id,
            location = NewGardenLocation(),
            updatedAt = 1_700_000_500_000L,
        )

        val updated = repository.getGarden(garden.id)!!
        assertNull(updated.countryCode)
        assertNull(updated.region)
        assertNull(updated.locality)
        assertNull(updated.latitude)
        assertNull(updated.longitude)
        assertNull(updated.timezone)
        assertEquals(Hemisphere.UNKNOWN, updated.hemisphere)
    }

    @Test
    fun updateLocation_throws_NotFoundError_when_garden_does_not_exist() = runBlocking {
        try {
            repository.updateLocation(
                id = "garden_does_not_exist",
                location = NewGardenLocation(hemisphere = Hemisphere.SOUTHERN),
                updatedAt = 1_700_000_500_000L,
            )
            fail("Expected NotFoundError; updateLocation succeeded")
        } catch (expected: NotFoundError) {
            assertEquals("Garden", expected.entityType)
            assertEquals("garden_does_not_exist", expected.id)
        }
    }

    private fun sampleGarden(): Garden = Garden(
        id = "garden_00000000-0000-0000-0000-000000000001",
        name = "Back Garden",
        description = "Main vegetable plot",
        countryCode = "NZ",
        region = "Canterbury",
        locality = "Christchurch",
        latitude = -43.5321,
        longitude = 172.6362,
        timezone = "Pacific/Auckland",
        hemisphere = Hemisphere.SOUTHERN,
        status = RecordStatus.DRAFT,
        createdAt = 1_700_000_000_000L,
        updatedAt = 1_700_000_000_000L,
    )
}