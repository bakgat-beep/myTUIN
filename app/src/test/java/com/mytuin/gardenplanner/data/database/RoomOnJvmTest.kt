package com.mytuin.gardenplanner.data.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Robolectric proof (A141, A142=a).
 *
 * Runs an in-memory Room database on the JVM, without an emulator or
 * device. This proves Robolectric is on the classpath and Room works
 * under it, which is the prerequisite for potentially moving
 * repository tests from androidTest to test in a future step.
 *
 * A142=a: JUnit 4 with RobolectricTestRunner, running under the
 * vintage engine. Robolectric's JUnit 5 integration is newer and this
 * test does not need it.
 *
 * @Config(sdk = 30): pins the emulated SDK level to one Robolectric
 * 4.14.1 supports unambiguously. Compile SDK is 36, which may exceed
 * the version's supported range. minSdk is 26; 30 is comfortably
 * within both bounds.
 *
 * This file uses JUnit 4 imports deliberately. It is the one
 * deliberate JUnit 4 file inside src/test after A139=a and A142=a.
 * All other src/test tests use JUnit 5.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [30])
class RoomOnJvmTest {
    private lateinit var db: GardenDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db =
            Room
                .inMemoryDatabaseBuilder(context, GardenDatabase::class.java)
                .addCallback(GardenDatabaseFactory.foreignKeysCallback)
                .build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insert_and_read_garden_on_jvm() =
        runBlocking {
            val entity = sampleGarden()
            db.gardenDao().insert(entity)

            val retrieved = db.gardenDao().getById(entity.id)

            assertEquals(entity, retrieved)
        }

    @Test
    fun get_by_id_returns_null_for_missing_on_jvm() =
        runBlocking {
            assertNull(db.gardenDao().getById("garden_does_not_exist"))
        }

    private fun sampleGarden(): GardenEntity =
        GardenEntity(
            id = "garden_robolectric_0001",
            name = "Robolectric Garden",
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
}
