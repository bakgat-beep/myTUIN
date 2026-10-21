package com.mytuin.gardenplanner.domain.usecases.inbox

import com.mytuin.gardenplanner.domain.model.garden.NewInboxItem
import com.mytuin.gardenplanner.domain.vocabulary.InboxItemRelatedEntityType
import com.mytuin.gardenplanner.domain.vocabulary.InboxItemStatus
import com.mytuin.gardenplanner.domain.vocabulary.InboxItemType
import com.mytuin.gardenplanner.domain.vocabulary.PlanningPriority
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeIdGenerator
import com.mytuin.gardenplanner.testdoubles.FakeInboxItemRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CreateInboxItemTest {
    private val repository = FakeInboxItemRepository()
    private val idGenerator = FakeIdGenerator(nextInboxItemIdValue = "inboxitem_test_0001")
    private val clock = FakeClock(nowMillisValue = 1_700_000_000_000L)

    private val createInboxItem =
        CreateInboxItem(
            repository = repository,
            idGenerator = idGenerator,
            clock = clock,
        )

    @Test
    fun invoke_returns_the_generated_id() =
        runBlocking {
            val id = createInboxItem(minimalInput())
            assertEquals("inboxitem_test_0001", id)
        }

    @Test
    fun invoke_persists_with_assigned_id_status_and_timestamps() =
        runBlocking {
            createInboxItem(minimalInput())

            val items = repository.snapshot()
            assertEquals(1, items.size)
            val item = items.first()

            assertEquals("inboxitem_test_0001", item.id)
            assertEquals("garden_test_0001", item.gardenId)
            assertEquals(InboxItemType.ACTION, item.itemType)
            assertEquals(PlanningPriority.HIGH, item.priority)
            assertEquals(InboxItemStatus.PENDING, item.status)
            assertEquals(1_700_000_000_000L, item.createdAt)
            assertEquals(1_700_000_000_000L, item.updatedAt)
        }

    @Test
    fun invoke_defaults_muted_until_to_null() =
        runBlocking {
            createInboxItem(minimalInput())
            assertNull(repository.snapshot().first().mutedUntil)
        }

    @Test
    fun invoke_preserves_related_entity_pairing() =
        runBlocking {
            createInboxItem(
                minimalInput().copy(
                    relatedEntityType = InboxItemRelatedEntityType.PLAN,
                    relatedEntityId = "plan_test_0001",
                ),
            )
            val item = repository.snapshot().first()
            assertEquals(InboxItemRelatedEntityType.PLAN, item.relatedEntityType)
            assertEquals("plan_test_0001", item.relatedEntityId)
        }

    @Test
    fun invoke_preserves_title_description_and_dueAt() =
        runBlocking {
            createInboxItem(
                minimalInput().copy(
                    title = "Water the tomato bed",
                    description = "Check soil moisture first.",
                    dueAt = 1_700_003_600_000L,
                ),
            )
            val item = repository.snapshot().first()
            assertEquals("Water the tomato bed", item.title)
            assertEquals("Check soil moisture first.", item.description)
            assertEquals(1_700_003_600_000L, item.dueAt)
        }

    private fun minimalInput(): NewInboxItem =
        NewInboxItem(
            gardenId = "garden_test_0001",
            itemType = InboxItemType.ACTION,
            priority = PlanningPriority.HIGH,
        )
}
