package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.repository.GardenRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Archive a Garden.
 *
 * PHASE_0_PROJECT_FOUNDATION §30 item 11, §31 (Data).
 * V1_DATABASE_SCHEMA.md §8: important garden records are archived,
 * never physically deleted. CORE_ARCHITECTURE.md §16, §71.
 *
 * Archiving sets status = ARCHIVED and updates updated_at. It does
 * not delete anything. Every foreign key to the garden stays valid;
 * every history row remains linked.
 *
 * No cascade (A236=a). Archived gardens may still contain active
 * growing spaces. A garden's status is its own.
 *
 * No confirmation requirement (A233). Archiving destroys nothing.
 *
 * A239: a missing garden throws NotFoundError from the repository.
 */
class ArchiveGarden
    @Inject
    constructor(
        private val repository: GardenRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(gardenId: String) {
            repository.archive(id = gardenId, archivedAt = clock.nowMillis())
        }
    }
