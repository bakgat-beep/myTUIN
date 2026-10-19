package com.mytuin.gardenplanner.domain.usecases.soil

import com.mytuin.gardenplanner.domain.model.garden.NewSoilRecord
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeIdGenerator
import com.mytuin.gardenplanner.testdoubles.FakeSoilRecordRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CreateSoilRecordTest {
    private val repository = FakeSoilRecordRepository()
    private val idGenerator = FakeIdGenerator(nextSoilRecordIdValue = "soilrecord_test_0001")
    private val clock = FakeClock(nowMillisValue = 1_700_000_000_000L)

    private val createSoilRecord =
        CreateSoilRecord(
            repository = repository,
            idGenerator = idGenerator,
            clock = clock,
        )

    @Test
    fun invoke_returns_the_generated_id() =
        runBlocking {
            val id = createSoilRecord(minimalInput())
            assertEquals("soilrecord_test_0001", id)
        }

    @Test
    fun invoke_persists_with_assigned_id_status_and_timestamps() =
        runBlocking {
            createSoilRecord(minimalInput())

            val records = repository.snapshot()
            assertEquals(1, records.size)
            val record = records.first()

            assertEquals("soilrecord_test_0001", record.id)
            assertEquals("garden_test_0001", record.gardenId)
            assertEquals(RecordStatus.ACTIVE, record.status)
            assertEquals(1_700_000_000_000L, record.createdAt)
            assertEquals(1_700_000_000_000L, record.updatedAt)
        }

    @Test
    fun invoke_defaults_scope_and_optional_fields_to_null() =
        runBlocking {
            createSoilRecord(minimalInput())
            val record = repository.snapshot().first()

            assertNull(record.areaId)
            assertNull(record.growingSpaceId)
            assertNull(record.name)
            assertNull(record.notes)
        }

    @Test
    fun invoke_preserves_area_scope() =
        runBlocking {
            createSoilRecord(minimalInput().copy(areaId = "area_test_0001"))
            assertEquals("area_test_0001", repository.snapshot().first().areaId)
        }

    @Test
    fun invoke_preserves_growing_space_scope() =
        runBlocking {
            createSoilRecord(minimalInput().copy(growingSpaceId = "growingspace_test_0001"))
            assertEquals("growingspace_test_0001", repository.snapshot().first().growingSpaceId)
        }

    @Test
    fun invoke_preserves_name_and_notes() =
        runBlocking {
            createSoilRecord(
                minimalInput().copy(
                    name = "Vegetable bed soil",
                    notes = "Improved drainage in 2025",
                ),
            )
            val record = repository.snapshot().first()
            assertEquals("Vegetable bed soil", record.name)
            assertEquals("Improved drainage in 2025", record.notes)
        }

    private fun minimalInput(): NewSoilRecord = NewSoilRecord(gardenId = "garden_test_0001")
}
