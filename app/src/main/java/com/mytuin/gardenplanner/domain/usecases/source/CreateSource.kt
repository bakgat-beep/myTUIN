package com.mytuin.gardenplanner.domain.usecases.source

import com.mytuin.gardenplanner.domain.identifiers.IdGenerator
import com.mytuin.gardenplanner.domain.model.garden.NewSource
import com.mytuin.gardenplanner.domain.model.garden.Source
import com.mytuin.gardenplanner.domain.repository.SourceRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Create a new Source.
 *
 * Assigns id and createdAt. `status` is taken from the input,
 * defaulting to UNVERIFIED.
 *
 * Returns the new Source's id.
 */
class CreateSource
    @Inject
    constructor(
        private val repository: SourceRepository,
        private val idGenerator: IdGenerator,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(input: NewSource): String {
            val source =
                Source(
                    id = idGenerator.newSourceId(),
                    sourceType = input.sourceType,
                    status = input.status,
                    createdAt = clock.nowMillis(),
                    title = input.title,
                    authorOrOrganisation = input.authorOrOrganisation,
                    publicationDate = input.publicationDate,
                    accessDate = input.accessDate,
                    url = input.url,
                    geographicScope = input.geographicScope,
                    hemisphere = input.hemisphere,
                    context = input.context,
                    notes = input.notes,
                )
            repository.insert(source)
            return source.id
        }
    }
