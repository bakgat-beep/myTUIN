package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.database.GardenDatabaseFactory
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.data.entities.InboxItemEntity
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.InboxItem
import com.mytuin.gardenplanner.domain.repository.InboxItemRepository
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.InboxItemRelatedEntityType
import com.mytuin.gardenplanner.domain.vocabulary.InboxItemStatus
import com.mytuin.gardenplanner.domain.vocabulary.InboxItemType
import com.mytuin.gardenplanner.domain.vocabulary.PlanningPriority
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
class InboxItemRepositoryTest {
    private lateinit var db: GardenDatabase
    private lateinit var repository: InboxItemRepository

    private val gardenId = "garden_00000000-0000-0000-0000-000000000001"

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
                InboxItemRepositoryImpl(
                    inboxItemDao = db.inboxItemDao(),
                    gardenDao = db.gardenDao(),
                )

            db.gardenDao().insert(sampleGarden())
        }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getInboxItem_returns_null_when_none_exists() =
        runBlocking {
            assertNull(repository.getInboxItem("inboxitem_does_not_exist"))
        }

    @Test
    fun insert_then_get_round_trips_all_fields() =
        runBlocking {
            val item = sampleInboxItem()
            repository.insert(item)

            assertEquals(item, repository.getInboxItem(item.id))
        }

    @Test
    fun insert_requires_existing_garden() =
        runBlocking {
            val orphan =
                sampleInboxItem().copy(
                    id = "inboxitem_00000000-0000-0000-0000-0000000000bb",
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
    fun observeInboxItemsInGarden_excludes_closed_by_default() =
        runBlocking {
            repository.insert(sampleInboxItem())
            repository.insert(
                sampleInboxItem().copy(
                    id = "inboxitem_00000000-0000-0000-0000-000000000002",
                    status = InboxItemStatus.DISMISSED,
                ),
            )
            repository.insert(
                sampleInboxItem().copy(
                    id = "inboxitem_00000000-0000-0000-0000-000000000003",
                    status = InboxItemStatus.COMPLETED,
                ),
            )

            val pending = repository.observeInboxItemsInGarden(gardenId).first()
            assertEquals(1, pending.size)

            val all = repository.observeInboxItemsInGarden(gardenId, includeClosed = true).first()
            assertEquals(3, all.size)
        }

    @Test
    fun updateStatus_changes_status_and_updatedAt() =
        runBlocking {
            val item = sampleInboxItem()
            repository.insert(item)

            repository.updateStatus(item.id, InboxItemStatus.COMPLETED, 1_700_000_500_000L)

            val updated = repository.getInboxItem(item.id)!!
            assertEquals(InboxItemStatus.COMPLETED, updated.status)
            assertEquals(1_700_000_500_000L, updated.updatedAt)
        }

    @Test
    fun updateStatus_throws_NotFoundError_when_missing() =
        runBlocking {
            try {
                repository.updateStatus(
                    "inboxitem_does_not_exist",
                    InboxItemStatus.COMPLETED,
                    1_700_000_500_000L,
                )
                fail("Expected NotFoundError")
            } catch (expected: NotFoundError) {
                assertEquals("InboxItem", expected.entityType)
            }
        }

    @Test
    fun mute_sets_mutedUntil_without_changing_status() =
        runBlocking {
            val item = sampleInboxItem()
            repository.insert(item)

            repository.mute(item.id, 1_700_007_200_000L, 1_700_000_500_000L)

            val updated = repository.getInboxItem(item.id)!!
            assertEquals(1_700_007_200_000L, updated.mutedUntil)
            assertEquals(InboxItemStatus.PENDING, updated.status)
        }

    @Test
    fun mute_with_null_clears_the_mute() =
        runBlocking {
            val item = sampleInboxItem().copy(mutedUntil = 1_700_007_200_000L)
            repository.insert(item)

            repository.mute(item.id, null, 1_700_000_500_000L)

            assertNull(repository.getInboxItem(item.id)?.mutedUntil)
        }

    @Test
    fun mapper_rejects_inconsistent_related_entity_pair() =
        runBlocking {
            // Insert directly via DAO with mismatched related_entity
            // fields.
            db.inboxItemDao().insert(
                InboxItemEntity(
                    id = "inboxitem_00000000-0000-0000-0000-0000000000cc",
                    garden_id = gardenId,
                    item_type = InboxItemType.ACTION,
                    priority = PlanningPriority.NORMAL,
                    status = InboxItemStatus.PENDING,
                    created_at = 1_700_000_000_000L,
                    updated_at = 1_700_000_000_000L,
                    related_entity_type = InboxItemRelatedEntityType.PLAN,
                    related_entity_id = null,
                ),
            )

            try {
                repository.observeInboxItemsInGarden(gardenId, includeClosed = true).first()
                fail("Expected IllegalArgumentException; mapper accepted inconsistent pair")
            } catch (expected: IllegalArgumentException) {
                // Expected.
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

    private fun sampleInboxItem(): InboxItem =
        InboxItem(
            id = "inboxitem_00000000-0000-0000-0000-000000000001",
            gardenId = gardenId,
            itemType = InboxItemType.ACTION,
            priority = PlanningPriority.HIGH,
            status = InboxItemStatus.PENDING,
            createdAt = 1_700_000_000_000L,
            updatedAt = 1_700_000_000_000L,
            title = "Water the tomato bed",
            description = "Check soil moisture first.",
            relatedEntityType = null,
            relatedEntityId = null,
            dueAt = 1_700_003_600_000L,
            mutedUntil = null,
        )
}
