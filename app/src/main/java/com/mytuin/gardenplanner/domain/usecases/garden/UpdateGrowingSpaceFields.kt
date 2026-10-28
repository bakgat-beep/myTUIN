package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpaceFields
import com.mytuin.gardenplanner.domain.repository.GrowingSpaceRepository
import com.mytuin.gardenplanner.domain.time.Clock
import javax.inject.Inject

/**
 * Update the mutable scalar fields of an existing GrowingSpace.
 *
 * Guards (D8 (a)):
 *   - name must not be blank after trim
 *   - any dimension that is supplied must be strictly positive
 *
 * Geometry and status are intentionally not touched. The repository
 * DAO query updates only the scalar columns.
 *
 * A missing growing space throws NotFoundError from the repository.
 */
class UpdateGrowingSpaceFields
    @Inject
    constructor(
        private val repository: GrowingSpaceRepository,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(
            growingSpaceId: String,
            fields: GrowingSpaceFields,
        ) {
            val trimmedName = fields.name.trim()
            if (trimmedName.isEmpty()) {
                throw ValidationError(
                    field = "name",
                    reason = "Name must not be blank",
                )
            }
            requirePositive(fields.lengthMetres, "length")
            requirePositive(fields.widthMetres, "width")
            requirePositive(fields.heightMetres, "height")
            requirePositive(fields.diameterMetres, "diameter")
            requirePositive(fields.areaSquareMetres, "area")
            requirePositive(fields.volumeCubicMetres, "volume")

            repository.updateFields(
                id = growingSpaceId,
                fields =
                    fields.copy(
                        name = trimmedName,
                        description = fields.description?.trim()?.ifBlank { null },
                        notes = fields.notes?.trim()?.ifBlank { null },
                    ),
                updatedAt = clock.nowMillis(),
            )
        }

        private fun requirePositive(
            value: Double?,
            field: String,
        ) {
            if (value != null && value <= 0.0) {
                throw ValidationError(
                    field = field,
                    reason = "Value must be greater than zero",
                )
            }
        }
    }
