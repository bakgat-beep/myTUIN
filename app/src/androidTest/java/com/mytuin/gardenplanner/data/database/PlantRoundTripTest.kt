package com.mytuin.gardenplanner.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.dao.CultivarDao
import com.mytuin.gardenplanner.data.dao.PlantAliasDao
import com.mytuin.gardenplanner.data.dao.PlantDao
import com.mytuin.gardenplanner.data.entities.CultivarEntity
import com.mytuin.gardenplanner.data.entities.PlantAliasEntity
import com.mytuin.gardenplanner.data.entities.PlantEntity
import com.mytuin.gardenplanner.domain.vocabulary.PlantAliasType
import com.mytuin.gardenplanner.domain.vocabulary.PlantLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Round-trip tests for the Plant knowledge entities.
 *
 * PHASE_0_PROJECT_FOUNDATION §30 item 9, §31 (Data, Vocabulary).
 * TESTING_STRATEGY §8, §13, §50.
 *
 * These tests use synthetic reference data only (A19; TESTING_STRATEGY
 * §74). They do not couple to any authored plant dataset.
 *
 * Tests run against an in-memory database with the same foreign-key
 * pragma that production uses (A15/D11). Without the pragma the
 * FK-violation test would pass for the wrong reason.
 */
@RunWith(AndroidJUnit4::class)
class PlantRoundTripTest {

    private lateinit var db: GardenDatabase
    private lateinit var plantDao: PlantDao
    private lateinit var aliasDao: PlantAliasDao
    private lateinit var cultivarDao: CultivarDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, GardenDatabase::class.java)
            .addCallback(GardenDatabaseFactory.foreignKeysCallback)
            .build()
        plantDao = db.plantDao()
        aliasDao = db.plantAliasDao()
        cultivarDao = db.cultivarDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun plant_with_alias_and_cultivar_round_trips() = runBlocking {
        val plant = samplePlant()
        plantDao.insert(plant)

        val alias = sampleAlias(plantId = plant.id)
        aliasDao.insert(alias)

        val cultivar = sampleCultivar(plantId = plant.id)
        cultivarDao.insert(cultivar)

        assertEquals(plant, plantDao.getById(plant.id))

        val aliases = aliasDao.getForPlant(plant.id)
        assertEquals(1, aliases.size)
        assertEquals(alias, aliases.first())

        val cultivars = cultivarDao.getForPlant(plant.id)
        assertEquals(1, cultivars.size)
        assertEquals(cultivar, cultivars.first())
    }

    @Test
    fun plant_lifecycle_is_stored_as_canonical_id_not_enum_name() = runBlocking {
        val plant = samplePlant()
        plantDao.insert(plant)

        db.openHelper.readableDatabase
            .query("SELECT lifecycle FROM plant WHERE id = ?", arrayOf(plant.id))
            .use { cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals("perennial", cursor.getString(0))
            }
    }

    @Test
    fun plant_alias_type_is_stored_as_canonical_id_not_enum_name() = runBlocking {
        val plant = samplePlant()
        plantDao.insert(plant)
        val alias = sampleAlias(plantId = plant.id)
        aliasDao.insert(alias)

        db.openHelper.readableDatabase
            .query("SELECT alias_type FROM plant_alias WHERE id = ?", arrayOf(alias.id))
            .use { cursor ->
                assertTrue(cursor.moveToFirst())
                assertEquals("common_name", cursor.getString(0))
            }
    }

    @Test
    fun plant_alias_requires_existing_plant() = runBlocking {
        val orphan = sampleAlias(plantId = "plant_does_not_exist")
        try {
            aliasDao.insert(orphan)
            fail("Expected foreign-key violation; insert succeeded")
        } catch (expected: android.database.sqlite.SQLiteConstraintException) {
            // V1_DATABASE_SCHEMA §67: foreign keys enforced.
        }
    }

    @Test
    fun cultivar_requires_existing_plant() = runBlocking {
        val orphan = sampleCultivar(plantId = "plant_does_not_exist")
        try {
            cultivarDao.insert(orphan)
            fail("Expected foreign-key violation; insert succeeded")
        } catch (expected: android.database.sqlite.SQLiteConstraintException) {
            // V1_DATABASE_SCHEMA §67: foreign keys enforced.
        }
    }

    @Test
    fun plant_with_unknown_lifecycle_round_trips_with_null() = runBlocking {
        val plant = samplePlant().copy(
            id = "plant_00000000-0000-0000-0000-000000000099",
            lifecycle = null,
        )
        plantDao.insert(plant)
        val retrieved = plantDao.getById(plant.id)
        assertNotNull(retrieved)
        assertEquals(null, retrieved!!.lifecycle)
    }

    private fun samplePlant(): PlantEntity = PlantEntity(
        id = "plant_00000000-0000-0000-0000-000000000001",
        canonical_name = "Test plant",
        scientific_name = "Testus plantus",
        genus = "Testus",
        species = "plantus",
        family = "Testaceae",
        lifecycle = PlantLifecycle.PERENNIAL,
        description = "Synthetic reference data for round-trip testing.",
        created_at = 1_700_000_000_000L,
        updated_at = 1_700_000_000_000L,
        status = RecordStatus.ACTIVE,
    )

    private fun sampleAlias(plantId: String): PlantAliasEntity = PlantAliasEntity(
        id = "plantalias_00000000-0000-0000-0000-000000000001",
        plant_id = plantId,
        alias = "Test plant alias",
        alias_type = PlantAliasType.COMMON_NAME,
        language = "en",
    )

    private fun sampleCultivar(plantId: String): CultivarEntity = CultivarEntity(
        id = "cultivar_00000000-0000-0000-0000-000000000001",
        plant_id = plantId,
        name = "Test cultivar",
        description = "Synthetic cultivar for round-trip testing.",
        notes = null,
        created_at = 1_700_000_000_000L,
        updated_at = 1_700_000_000_000L,
        status = RecordStatus.ACTIVE,
    )
}