package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.repository.AreaRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Archive an Area.
 *
 * Mirrors ArchiveGarden. Sets status = ARCHIVED and updated_at. The
 * row is preserved; every FK to the Area stays valid.
 *
 * A missing Area throws NotFoundError from the repository.
 */
class ArchiveArea
    @Inject
    constructor(
        private val repository: AreaRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(areaId: String) {
            repository.archive(id = areaId, archivedAt = clock.nowMillis())
        }
    }
