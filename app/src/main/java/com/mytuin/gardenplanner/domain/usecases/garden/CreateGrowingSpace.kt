package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.identifiers.IdGenerator
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.model.garden.NewGrowingSpace
import com.mytuin.gardenplanner.domain.repository.GrowingSpaceRepository
import com.mytuin.gardenplanner.domain.time.Clock
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import javax.inject.Inject

/**
 * Create a new GrowingSpace.
 *
 * A59=A: named CreateGrowingSpace rather than the §6 example
 * AddGrowingSpace, for consistency with CreateGarden.
 *
 * Assigns id, status (ACTIVE, S1), createdAt, updatedAt. Returns the
 * new id.
 *
 * The garden_id foreign key is enforced by Room. If the caller passes
 * a gardenId that does not refer to an existing Garden, the repository
 * throws ValidationError (field = "garden_id"). A25=C: exceptions,
 * not a structured Result type. A98: the repository wraps low-level
 * exceptions; callers above this layer never see
 * SQLiteConstraintException.
 */
class CreateGrowingSpace @Inject constructor(
    private val repository: GrowingSpaceRepository,
    private val idGenerator: IdGenerator,
    private val clock: Clock,
) {
    suspend operator fun invoke(input: NewGrowingSpace): String {
        val now = clock.nowMillis()
        val growingSpace = GrowingSpace(
            id = idGenerator.newGrowingSpaceId(),
            gardenId = input.gardenId,
            name = input.name,
            spaceType = input.spaceType,
            status = RecordStatus.ACTIVE,
            geometry = input.geometry,
            lengthMetres = input.lengthMetres,
            widthMetres = input.widthMetres,
            heightMetres = input.heightMetres,
            diameterMetres = input.diameterMetres,
            areaSquareMetres = input.areaSquareMetres,
            volumeCubicMetres = input.volumeCubicMetres,
            description = input.description,
            notes = input.notes,
            createdAt = now,
            updatedAt = now,
        )
        repository.insert(growingSpace)
        return growingSpace.id
    }
}