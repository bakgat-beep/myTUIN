package com.mytuin.gardenplanner.domain.usecases.inbox

import com.mytuin.gardenplanner.domain.identifiers.IdGenerator
import com.mytuin.gardenplanner.domain.model.garden.InboxItem
import com.mytuin.gardenplanner.domain.model.garden.NewInboxItem
import com.mytuin.gardenplanner.domain.repository.InboxItemRepository
import com.mytuin.gardenplanner.domain.time.Clock
import com.mytuin.gardenplanner.domain.vocabulary.InboxItemStatus
import javax.inject.Inject

class CreateInboxItem
    @Inject
    constructor(
        private val repository: InboxItemRepository,
        private val idGenerator: IdGenerator,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(input: NewInboxItem): String {
            val now = clock.nowMillis()
            val item =
                InboxItem(
                    id = idGenerator.newInboxItemId(),
                    gardenId = input.gardenId,
                    itemType = input.itemType,
                    priority = input.priority,
                    status = InboxItemStatus.PENDING,
                    createdAt = now,
                    updatedAt = now,
                    title = input.title,
                    description = input.description,
                    relatedEntityType = input.relatedEntityType,
                    relatedEntityId = input.relatedEntityId,
                    dueAt = input.dueAt,
                    mutedUntil = null,
                )
            repository.insert(item)
            return item.id
        }
    }
