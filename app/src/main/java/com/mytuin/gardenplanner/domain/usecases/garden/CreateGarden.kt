package com.mytuin.gardenplanner.domain.usecases.garden

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
 * The use case assigns id, createdAt, updatedAt and status (A44=A;
 * repository takes a fully-formed domain model). Status is DRAFT
 * (D5=B, A41).
 *
 * Returns the new Garden's id (A38=A, non-null).
 *
 * No transaction: a single-row insert does not need one (A42). The
 * transaction pattern arrives with the first multi-row write.
 */
class CreateGarden @Inject constructor(
    private val repository: GardenRepository,
    private val idGenerator: IdGenerator,
    private val clock: Clock,
) {
    suspend operator fun invoke(newGarden: NewGarden): String {
        val now = clock.nowMillis()
        val garden = Garden(
            id = idGenerator.newGardenId(),
            name = newGarden.name,
            description = newGarden.description,
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