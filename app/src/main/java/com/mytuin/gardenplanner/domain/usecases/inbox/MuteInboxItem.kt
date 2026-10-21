package com.mytuin.gardenplanner.domain.usecases.inbox

import com.mytuin.gardenplanner.domain.repository.InboxItemRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Mute or unmute an InboxItem.
 *
 * §53: mute means "do not bother me about this for now". It is not a
 * status change; the item remains pending. Passing null for
 * mutedUntil clears any existing mute.
 */
class MuteInboxItem
    @Inject
    constructor(
        private val repository: InboxItemRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(
            inboxItemId: String,
            mutedUntil: Long?,
        ) {
            repository.mute(
                id = inboxItemId,
                mutedUntil = mutedUntil,
                updatedAt = clock.nowMillis(),
            )
        }
    }
