package com.mytuin.gardenplanner.domain.usecases.source

import com.mytuin.gardenplanner.domain.repository.SourceRepository
import com.mytuin.gardenplanner.domain.vocabulary.SourceStatus
import javax.inject.Inject

/**
 * Change a Source's assessment status.
 *
 * SC8 (a), SC12: no timestamp parameter. Source has no updated_at
 * column; a status change is a metadata edit to reference data.
 */
class UpdateSourceStatus
    @Inject
    constructor(
        private val repository: SourceRepository,
    ) {
        suspend operator fun invoke(
            sourceId: String,
            status: SourceStatus,
        ) {
            repository.updateStatus(id = sourceId, status = status)
        }
    }
