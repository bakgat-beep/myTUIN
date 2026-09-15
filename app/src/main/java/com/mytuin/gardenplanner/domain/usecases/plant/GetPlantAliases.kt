package com.mytuin.gardenplanner.domain.usecases.plant

import com.mytuin.gardenplanner.domain.model.plant.PlantAlias
import com.mytuin.gardenplanner.domain.repository.PlantRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Observe the aliases for a given Plant.
 *
 * Returns Flow per A24/A33: list queries are reactive so callers see
 * updates when reference data changes.
 */
class GetPlantAliases @Inject constructor(
    private val repository: PlantRepository,
) {
    operator fun invoke(plantId: String): Flow<List<PlantAlias>> =
        repository.observeAliasesForPlant(plantId)
}