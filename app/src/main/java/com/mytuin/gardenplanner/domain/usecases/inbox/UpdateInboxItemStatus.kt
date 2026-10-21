package com.mytuin.gardenplanner.domain.usecases.inbox

import com.mytuin.gardenplanner.domain.repository.InboxItemRepository
import com.mytuin.gardenplanner.domain.time.Clock
import com.mytuin.gardenplanner.domain.vocabulary.InboxItemStatus
import javax.inject.Inject

/**
 * Transition an InboxItem's status. Typically called with DISMISSED
 * or COMPLETED to close the item, or back to PENDING to re-open it.
 */
class UpdateInboxItemStatus
    @Inject
    constructor(
        private val repository: InboxItemRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(
            inboxItemId: String,
            status: InboxItemStatus,
        ) {
            repository.updateStatus(
                id = inboxItemId,
                status = status,
                updatedAt = clock.nowMillis(),
            )
        }
    }
