package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.model.garden.SpatialObjectFields
import com.mytuin.gardenplanner.domain.repository.SpatialObjectRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Update the mutable scalar fields of an existing SpatialObject
 * (Step 9e).
 *
 * No guards on name: SpatialObject.name is nullable by design (B4),
 * so blank is a valid value meaning "no name recorded" and is
 * stored as null. Same treatment for description and notes.
 *
 * Geometry and status are deliberately not touched: geometry is
 * immutable in 9e (would require a history decision, DEC-041 /
 * 9i); status changes go through archive/restore.
 *
 * A missing SpatialObject throws NotFoundError from the repository.
 * A missing Area throws ValidationError with field "area_id".
 */
class UpdateSpatialObjectFields
    @Inject
    constructor(
        private val repository: SpatialObjectRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(
            spatialObjectId: String,
            fields: SpatialObjectFields,
        ) {
            repository.updateFields(
                id = spatialObjectId,
                fields =
                    fields.copy(
                        name = fields.name?.trim()?.ifBlank { null },
                        description = fields.description?.trim()?.ifBlank { null },
                        notes = fields.notes?.trim()?.ifBlank { null },
                    ),
                updatedAt = clock.nowMillis(),
            )
        }
    }
