package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.plant.Cultivar
import com.mytuin.gardenplanner.domain.model.plant.Plant
import com.mytuin.gardenplanner.domain.model.plant.PlantAlias
import com.mytuin.gardenplanner.domain.model.plant.PlantMultiValue
import com.mytuin.gardenplanner.domain.model.plant.PlantNote
import com.mytuin.gardenplanner.domain.model.plant.PlantNumericFact
import com.mytuin.gardenplanner.domain.model.plant.PlantTemporalFact
import com.mytuin.gardenplanner.domain.vocabulary.PlantMultiValueKind
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Plant knowledge.
 *
 * Step 4c-B adds reads for numeric facts, temporal facts and notes.
 * No write path in this step (P41C-8).
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

    fun observeNumericFactsForPlant(plantId: String): Flow<List<PlantNumericFact>>

    fun observeTemporalFactsForPlant(plantId: String): Flow<List<PlantTemporalFact>>

    fun observeNotesForPlant(plantId: String): Flow<List<PlantNote>>
}
