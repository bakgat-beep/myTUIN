package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.database.GardenDatabaseFactory
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.data.entities.PlantEntity
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.GardenPlantPreference
import com.mytuin.gardenplanner.domain.model.garden.GardenPreference
import com.mytuin.gardenplanner.domain.repository.GardenPreferenceRepository
import com.mytuin.gardenplanner.domain.vocabulary.GardenPlantPreferenceKind
import com.mytuin.gardenplanner.domain.vocabulary.GardenPreferenceKey
import com.mytuin.gardenplanner.domain.vocabulary.GardenPriority
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.PlantLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GardenPreferenceRepositoryTest {
    private lateinit var db: GardenDatabase
    private lateinit var repository: GardenPreferenceRepository

    private val gardenId = "garden_00000000-0000-0000-0000-000000000001"
    private val plantId = "plant_00000000-0000-0000-0000-000000000001"

    @Before
    fun setUp() =
        runBlocking {
            val context = ApplicationProvider.getApplicationContext<Context>()
            db =
                Room
                    .inMemoryDatabaseBuilder(context, GardenDatabase::class.java)
                    .addCallback(GardenDatabaseFactory.foreignKeysCallback)
                    .build()
            repository =
                GardenPreferenceRepositoryImpl(
                    gardenPreferenceDao = db.gardenPreferenceDao(),
                    gardenDao = db.gardenDao(),
                    plantDao = db.plantDao(),
                )

            db.gardenDao().insert(sampleGarden())
            db.plantDao().insert(samplePlant())
        }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getPreferences_returns_empty_when_none_set() =
        runBlocking {
            assertEquals(emptyList<GardenPreference>(), repository.getPreferences(gardenId))
        }

    @Test
    fun setPreference_then_getPreference_round_trips() =
        runBlocking {
            val preference =
                GardenPreference(
                    gardenId = gardenId,
                    key = GardenPreferenceKey.WATER_CONSERVATION_PRIORITY,
                    priority = GardenPriority.HIGH,
                )
            repository.setPreference(preference)

            assertEquals(preference, repository.getPreference(gardenId, preference.key))
        }

    @Test
    fun setPreference_twice_replaces_the_prior_value() =
        runBlocking {
            repository.setPreference(
                GardenPreference(gardenId, GardenPreferenceKey.POLLINATOR_PRIORITY, GardenPriority.LOW),
            )
            repository.setPreference(
                GardenPreference(gardenId, GardenPreferenceKey.POLLINATOR_PRIORITY, GardenPriority.CRITICAL),
            )

            val all = repository.getPreferences(gardenId)
            assertEquals(1, all.size)
            assertEquals(GardenPriority.CRITICAL, all.first().priority)
        }

    @Test
    fun observePreferences_emits_after_set() =
        runBlocking {
            val initial = repository.observePreferences(gardenId).first()
            assertEquals(0, initial.size)

            repository.setPreference(
                GardenPreference(gardenId, GardenPreferenceKey.EXPERIMENTATION_PREFERENCE, GardenPriority.NORMAL),
            )

            val after = repository.observePreferences(gardenId).first()
            assertEquals(1, after.size)
            assertEquals(GardenPreferenceKey.EXPERIMENTATION_PREFERENCE, after.first().key)
        }

    @Test
    fun clearPreference_removes_the_row() =
        runBlocking {
            repository.setPreference(
                GardenPreference(gardenId, GardenPreferenceKey.NATIVE_PLANT_PREFERENCE, GardenPriority.HIGH),
            )
            repository.clearPreference(gardenId, GardenPreferenceKey.NATIVE_PLANT_PREFERENCE)

            assertNull(
                repository.getPreference(gardenId, GardenPreferenceKey.NATIVE_PLANT_PREFERENCE),
            )
        }

    @Test
    fun setPreference_throws_NotFoundError_when_garden_missing() =
        runBlocking {
            try {
                repository.setPreference(
                    GardenPreference(
                        gardenId = "garden_does_not_exist",
                        key = GardenPreferenceKey.POLLINATOR_PRIORITY,
                        priority = GardenPriority.LOW,
                    ),
                )
                fail("Expected NotFoundError")
            } catch (expected: NotFoundError) {
                assertEquals("Garden", expected.entityType)
                assertEquals("garden_does_not_exist", expected.id)
            }
        }

    @Test
    fun addPlantPreference_favourite_round_trips() =
        runBlocking {
            val preference =
                GardenPlantPreference(
                    gardenId = gardenId,
                    plantId = plantId,
                    kind = GardenPlantPreferenceKind.FAVOURITE,
                )
            repository.addPlantPreference(preference)

            val favourites =
                repository.getPlantPreferences(
                    gardenId,
                    GardenPlantPreferenceKind.FAVOURITE,
                )
            assertEquals(1, favourites.size)
            assertEquals(preference, favourites.first())

            val avoided =
                repository.getPlantPreferences(
                    gardenId,
                    GardenPlantPreferenceKind.AVOID,
                )
            assertEquals(0, avoided.size)
        }

    @Test
    fun a_plant_can_be_both_favourite_and_avoided() =
        runBlocking {
            repository.addPlantPreference(
                GardenPlantPreference(gardenId, plantId, GardenPlantPreferenceKind.FAVOURITE),
            )
            repository.addPlantPreference(
                GardenPlantPreference(gardenId, plantId, GardenPlantPreferenceKind.AVOID),
            )

            assertEquals(
                1,
                repository.getPlantPreferences(gardenId, GardenPlantPreferenceKind.FAVOURITE).size,
            )
            assertEquals(
                1,
                repository.getPlantPreferences(gardenId, GardenPlantPreferenceKind.AVOID).size,
            )
        }

    @Test
    fun addPlantPreference_is_idempotent() =
        runBlocking {
            val preference =
                GardenPlantPreference(
                    gardenId = gardenId,
                    plantId = plantId,
                    kind = GardenPlantPreferenceKind.FAVOURITE,
                )
            repository.addPlantPreference(preference)
            repository.addPlantPreference(preference)

            assertEquals(
                1,
                repository.getPlantPreferences(gardenId, GardenPlantPreferenceKind.FAVOURITE).size,
            )
        }

    @Test
    fun removePlantPreference_removes_only_the_matching_kind() =
        runBlocking {
            repository.addPlantPreference(
                GardenPlantPreference(gardenId, plantId, GardenPlantPreferenceKind.FAVOURITE),
            )
            repository.addPlantPreference(
                GardenPlantPreference(gardenId, plantId, GardenPlantPreferenceKind.AVOID),
            )

            repository.removePlantPreference(
                gardenId,
                plantId,
                GardenPlantPreferenceKind.FAVOURITE,
            )

            assertEquals(
                0,
                repository.getPlantPreferences(gardenId, GardenPlantPreferenceKind.FAVOURITE).size,
            )
            assertEquals(
                1,
                repository.getPlantPreferences(gardenId, GardenPlantPreferenceKind.AVOID).size,
            )
        }

    @Test
    fun addPlantPreference_throws_NotFoundError_when_plant_missing() =
        runBlocking {
            try {
                repository.addPlantPreference(
                    GardenPlantPreference(
                        gardenId = gardenId,
                        plantId = "plant_does_not_exist",
                        kind = GardenPlantPreferenceKind.FAVOURITE,
                    ),
                )
                fail("Expected NotFoundError")
            } catch (expected: NotFoundError) {
                assertEquals("Plant", expected.entityType)
                assertEquals("plant_does_not_exist", expected.id)
            }
        }

    @Test
    fun addPlantPreference_throws_NotFoundError_when_garden_missing() =
        runBlocking {
            try {
                repository.addPlantPreference(
                    GardenPlantPreference(
                        gardenId = "garden_does_not_exist",
                        plantId = plantId,
                        kind = GardenPlantPreferenceKind.FAVOURITE,
                    ),
                )
                fail("Expected NotFoundError")
            } catch (expected: NotFoundError) {
                assertEquals("Garden", expected.entityType)
            }
        }

    @Test
    fun preference_key_is_stored_as_canonical_id_not_enum_name() =
        runBlocking {
            repository.setPreference(
                GardenPreference(
                    gardenId,
                    GardenPreferenceKey.WATER_CONSERVATION_PRIORITY,
                    GardenPriority.HIGH,
                ),
            )

            db.openHelper.readableDatabase
                .query("SELECT preference_key, priority FROM garden_preference")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("water_conservation_priority", cursor.getString(0))
                    assertEquals("high", cursor.getString(1))
                }
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
            canonical_name = "Test Plant",
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
}
