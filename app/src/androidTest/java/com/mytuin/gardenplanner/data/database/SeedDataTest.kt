package com.mytuin.gardenplanner.data.database

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests for the seed-data mechanism.
 *
 * A137=a: file-backed database, because only real first creation
 * triggers RoomDatabase.Callback.onCreate. An in-memory database
 * would exercise the callback too, but the point of this test is
 * the wiring — that SeedCallback is registered on the production
 * factory path.
 *
 * PHASE_0_PROJECT_FOUNDATION §11, §30 item 12.
 */
@RunWith(AndroidJUnit4::class)
class SeedDataTest {
    private val dbName = "seed-data-test.db"

    private lateinit var context: Context
    private lateinit var db: GardenDatabase

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        context.deleteDatabase(dbName)
        db = GardenDatabaseFactory.create(context, dbName)
    }

    @After
    fun tearDown() {
        db.close()
        context.deleteDatabase(dbName)
    }

    @Test
    fun first_database_creation_seeds_two_plants() =
        runBlocking {
            val plants = db.plantDao().observeAll().first()
            assertEquals(2, plants.size)
        }

    @Test
    fun seed_rows_have_the_expected_ids_and_names() =
        runBlocking {
            val plants = db.plantDao().observeAll().first()

            assertEquals(
                setOf("plant_example_0001", "plant_example_0002"),
                plants.map { it.id }.toSet(),
            )
            assertEquals(
                setOf("Example Plant A", "Example Plant B"),
                plants.map { it.canonical_name }.toSet(),
            )
        }

    @Test
    fun seed_rows_are_active_and_have_null_lifecycle() =
        runBlocking {
            val plants = db.plantDao().observeAll().first()
            plants.forEach { plant ->
                assertEquals(RecordStatus.ACTIVE, plant.status)
                assertNull(plant.lifecycle)
                assertNull(plant.scientific_name)
            }
        }

    @Test
    fun seeding_runs_only_once_per_database_file() =
        runBlocking {
            // Force first access so onCreate fires.
            db.plantDao().observeAll().first()

            // Close and reopen the same file. onCreate must not fire again.
            db.close()
            db = GardenDatabaseFactory.create(context, dbName)

            val plants = db.plantDao().observeAll().first()
            assertEquals("seeding must not double-apply", 2, plants.size)
        }
}
