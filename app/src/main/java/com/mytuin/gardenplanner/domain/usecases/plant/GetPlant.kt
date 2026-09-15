package com.mytuin.gardenplanner.domain.usecases.plant

import com.mytuin.gardenplanner.domain.model.plant.Plant
import com.mytuin.gardenplanner.domain.repository.PlantRepository
import javax.inject.Inject

/**
 * Look up a single Plant by id.
 *
 * PHASE_0_PROJECT_FOUNDATION §14 (Composable → ViewModel → Use case →
 * Repository). Read use cases take primitive ids (A34).
 *
 * Returns null when no Plant exists for the given id. Null is a valid
 * result, not an error (CORE_VOCABULARIES §4).
 */
class GetPlant @Inject constructor(
    private val repository: PlantRepository,
) {
    suspend operator fun invoke(id: String): Plant? = repository.getPlant(id)
}