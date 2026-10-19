package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.database.GardenDatabaseFactory
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.data.entities.GrowingSpaceEntity
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.SoilRecord
import com.mytuin.gardenplanner.domain.repository.SoilRecordRepository
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
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

@RunWith(AndroidJUnit4::class)
class SoilRecordRepositoryTest {
    private lateinit var db: GardenDatabase
    private lateinit var repository: SoilRecordRepository

    private val gardenId = "garden_00000000-0000-0000-0000-000000000001"
    private val spaceId = "growingspace_00000000-0000-0000-0000-000000000001"

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
                SoilRecordRepositoryImpl(
                    soilRecordDao = db.soilRecordDao(),
                    gardenDao = db.gardenDao(),
                    areaDao = db.areaDao(),
                    growingSpaceDao = db.growingSpaceDao(),
                )

            db.gardenDao().insert(sampleGarden())
            db.growingSpaceDao().insert(sampleGrowingSpace())
        }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getSoilRecord_returns_null_when_none_exists() =
        runBlocking {
            assertNull(repository.getSoilRecord("soilrecord_does_not_exist"))
        }

    @Test
    fun insert_then_get_round_trips_all_fields() =
        runBlocking {
            val record = sampleSoilRecord()
            repository.insert(record)

            assertEquals(record, repository.getSoilRecord(record.id))
        }

    @Test
    fun insert_garden_scoped_only_succeeds() =
        runBlocking {
            val record =
                sampleSoilRecord().copy(areaId = null, growingSpaceId = null)
            repository.insert(record)

            val retrieved = repository.getSoilRecord(record.id)!!
            assertNull(retrieved.areaId)
            assertNull(retrieved.growingSpaceId)
        }

    @Test
    fun insert_growing_space_scoped_succeeds() =
        runBlocking {
            val record =
                sampleSoilRecord().copy(areaId = null, growingSpaceId = spaceId)
            repository.insert(record)

            assertEquals(spaceId, repository.getSoilRecord(record.id)?.growingSpaceId)
        }

    @Test
    fun insert_requires_existing_garden() =
        runBlocking {
            val orphan =
                sampleSoilRecord().copy(
                    id = "soilrecord_00000000-0000-0000-0000-0000000000bb",
                    gardenId = "garden_does_not_exist",
                )
            try {
                repository.insert(orphan)
                fail("Expected ValidationError; insert succeeded")
            } catch (expected: ValidationError) {
                assertEquals("garden_id", expected.field)
            }
        }

    @Test
    fun insert_requires_existing_growing_space_when_supplied() =
        runBlocking {
            val orphan =
                sampleSoilRecord().copy(
                    id = "soilrecord_00000000-0000-0000-0000-0000000000cc",
                    areaId = null,
                    growingSpaceId = "growingspace_does_not_exist",
                )
            try {
                repository.insert(orphan)
                fail("Expected ValidationError; insert succeeded")
            } catch (expected: ValidationError) {
                assertEquals("growing_space_id", expected.field)
            }
        }

    @Test
    fun archive_sets_status_and_excludes_from_default_observation() =
        runBlocking {
            val record = sampleSoilRecord()
            repository.insert(record)

            repository.archive(record.id, 1_700_000_500_000L)

            assertEquals(
                RecordStatus.ARCHIVED,
                repository.getSoilRecord(record.id)?.status,
            )
            assertEquals(
                0,
                repository.observeSoilRecordsInGarden(gardenId).first().size,
            )
            assertEquals(
                1,
                repository.observeSoilRecordsInGarden(gardenId, includeArchived = true).first().size,
            )
        }

    @Test
    fun restore_returns_the_record_to_active() =
        runBlocking {
            val record = sampleSoilRecord()
            repository.insert(record)
            repository.archive(record.id, 1_700_000_500_000L)

            repository.restore(record.id, 1_700_001_000_000L)

            assertEquals(
                RecordStatus.ACTIVE,
                repository.getSoilRecord(record.id)?.status,
            )
        }

    @Test
    fun archive_throws_NotFoundError_when_record_missing() =
        runBlocking {
            try {
                repository.archive("soilrecord_does_not_exist", 1_700_000_500_000L)
                fail("Expected NotFoundError")
            } catch (expected: NotFoundError) {
                assertEquals("SoilRecord", expected.entityType)
            }
        }

    @Test
    fun name_and_notes_round_trip() =
        runBlocking {
            val record =
                sampleSoilRecord().copy(
                    name = "Bed soil",
                    notes = "Improved drainage in 2025",
                )
            repository.insert(record)

            val retrieved = repository.getSoilRecord(record.id)!!
            assertEquals("Bed soil", retrieved.name)
            assertEquals("Improved drainage in 2025", retrieved.notes)
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

    private fun sampleGrowingSpace(): GrowingSpaceEntity =
        GrowingSpaceEntity(
            id = spaceId,
            garden_id = gardenId,
            name = "Bed 2",
            space_type = GrowingSpaceType.RAISED_BED,
            status = RecordStatus.ACTIVE,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
            geometry_type = null,
            geometry_data = null,
            length = null,
            width = null,
            height = null,
            diameter = null,
            area = null,
            volume = null,
            description = null,
            area_id = null,
            notes = null,
        )

    private fun sampleSoilRecord(): SoilRecord =
        SoilRecord(
            id = "soilrecord_00000000-0000-0000-0000-000000000001",
            gardenId = gardenId,
            status = RecordStatus.ACTIVE,
            createdAt = 1_700_000_000_000L,
            updatedAt = 1_700_000_000_000L,
            areaId = null,
            growingSpaceId = spaceId,
            name = "Bed 2 soil",
            notes = null,
        )
}
