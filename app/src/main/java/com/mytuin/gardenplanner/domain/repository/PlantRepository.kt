package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.plant.Cultivar
import com.mytuin.gardenplanner.domain.model.plant.Plant
import com.mytuin.gardenplanner.domain.model.plant.PlantAlias
import com.mytuin.gardenplanner.domain.model.plant.PlantMultiValue
import com.mytuin.gardenplanner.domain.vocabulary.PlantMultiValueKind
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Plant knowledge.
 *
 * V1_TECHNICAL_ARCHITECTURE §9. The interface lives in the domain so
 * the application layer can depend on it without depending on Room
 * (A21). The implementation lives in data/ (A22).
 *
 * List queries return Flow; point lookups return suspend (A24;
 * V1_TECHNICAL_ARCHITECTURE §55).
 *
 * Step 4b adds multi-value classification reads. No write path in
 * this step (P41B-6); the write side arrives with the Plant Library
 * feature or the plant seed loader.
 */
interface PlantRepository {
    fun observePlants(): Flow<List<Plant>>

    suspend fun getPlant(id: String): Plant?

    fun observeAliasesForPlant(plantId: String): Flow<List<PlantAlias>>

    fun observeCultivarsForPlant(plantId: String): Flow<List<Cultivar>>

    fun observeAllMultiValuesForPlant(plantId: String): Flow<List<PlantMultiValue>>

    fun observeMultiValuesForPlant(
        plantId: String,
        kind: PlantMultiValueKind,
    ): Flow<List<PlantMultiValue>>
}
