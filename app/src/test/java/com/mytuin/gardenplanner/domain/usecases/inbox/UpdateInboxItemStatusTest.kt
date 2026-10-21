package com.mytuin.gardenplanner.domain.usecases.inbox

import com.mytuin.gardenplanner.domain.model.garden.InboxItem
import com.mytuin.gardenplanner.domain.vocabulary.InboxItemStatus
import com.mytuin.gardenplanner.domain.vocabulary.InboxItemType
import com.mytuin.gardenplanner.domain.vocabulary.PlanningPriority
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeInboxItemRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UpdateInboxItemStatusTest {
    private val repository = FakeInboxItemRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_000_500_000L)
    private val useCase = UpdateInboxItemStatus(repository, clock)

    private val itemId = "inboxitem_test_0001"

    @Before
    fun setUp() =
        runBlocking {
            repository.insert(
                InboxItem(
                    id = itemId,
                    gardenId = "garden_test_0001",
                    itemType = InboxItemType.ACTION,
                    priority = PlanningPriority.HIGH,
                    status = InboxItemStatus.PENDING,
                    createdAt = 1_700_000_000_000L,
                    updatedAt = 1_700_000_000_000L,
                    title = null,
                    description = null,
                    relatedEntityType = null,
                    relatedEntityId = null,
                    dueAt = null,
                    mutedUntil = null,
                ),
            )
        }

    @Test
    fun invoke_dismisses_pending_item() =
        runBlocking {
            useCase(itemId, InboxItemStatus.DISMISSED)
            assertEquals(InboxItemStatus.DISMISSED, repository.snapshot().single().status)
        }

    @Test
    fun invoke_completes_pending_item() =
        runBlocking {
            useCase(itemId, InboxItemStatus.COMPLETED)
            assertEquals(InboxItemStatus.COMPLETED, repository.snapshot().single().status)
        }

    @Test
    fun invoke_supplies_updatedAt_from_clock() =
        runBlocking {
            useCase(itemId, InboxItemStatus.DISMISSED)
            assertEquals(1_700_000_500_000L, repository.snapshot().single().updatedAt)
        }
}
