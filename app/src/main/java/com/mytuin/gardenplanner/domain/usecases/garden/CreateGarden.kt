package com.mytuin.gardenplanner.domain.usecases.garden

import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.identifiers.IdGenerator
import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.model.garden.NewGarden
import com.mytuin.gardenplanner.domain.repository.GardenRepository
import com.mytuin.gardenplanner.domain.time.Clock
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import javax.inject.Inject

/**
 * Create a new Garden.
 *
 * D9a-3: the use case guards against a blank name. This makes the
 * invariant true for every caller, not just the UI. The ViewModel in
 * ui/garden/CreateGardenViewModel pre-checks so the field error
 * appears without a round-trip, but the use case is the ultimate
 * guard.
 *
 * Assigns id, createdAt, updatedAt and status (DRAFT). Trims the name
 * and description so a name of "  Garden  " stores as "Garden".
 *
 * Returns the new Garden's id.
 */
class CreateGarden
    @Inject
    constructor(
        private val repository: GardenRepository,
        private val idGenerator: IdGenerator,
        private val clock: Clock,
    ) {
        suspend operator fun invoke(newGarden: NewGarden): String {
            val trimmedName = newGarden.name.trim()
            if (trimmedName.isEmpty()) {
                throw ValidationError(
                    field = "name",
                    reason = "Name must not be blank",
                )
            }
            val now = clock.nowMillis()
            val garden =
                Garden(
                    id = idGenerator.newGardenId(),
                    name = trimmedName,
                    description = newGarden.description?.trim()?.ifBlank { null },
                    countryCode = newGarden.countryCode,
                    region = newGarden.region,
                    locality = newGarden.locality,
                    latitude = newGarden.latitude,
                    longitude = newGarden.longitude,
                    timezone = newGarden.timezone,
                    hemisphere = newGarden.hemisphere,
                    status = RecordStatus.DRAFT,
                    createdAt = now,
                    updatedAt = now,
                )
            repository.insert(garden)
            return garden.id
        }
    }
