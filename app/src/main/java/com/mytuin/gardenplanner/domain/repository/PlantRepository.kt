package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.plant.Cultivar
import com.mytuin.gardenplanner.domain.model.plant.Plant
import com.mytuin.gardenplanner.domain.model.plant.PlantAlias
import com.mytuin.gardenplanner.domain.model.plant.PlantMultiValue
import com.mytuin.gardenplanner.domain.model.plant.PlantNote
import com.mytuin.gardenplanner.domain.model.plant.PlantNumericFact
import com.mytuin.gardenplanner.domain.model.plant.PlantProblemRelationship
import com.mytuin.gardenplanner.domain.model.plant.PlantRelationship
import com.mytuin.gardenplanner.domain.model.plant.PlantSource
import com.mytuin.gardenplanner.domain.model.plant.PlantTemporalFact
import com.mytuin.gardenplanner.domain.vocabulary.PlantMultiValueKind
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Plant knowledge.
 *
 * Step 4d adds reads for plant-to-plant relationships,
 * plant-to-problem relationships, and plant-to-source links. No
 * write path in this step (P41D-9).
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

    fun observeRelationshipsForPlant(plantId: String): Flow<List<PlantRelationship>>

    fun observeProblemRelationshipsForPlant(plantId: String): Flow<List<PlantProblemRelationship>>

    fun observeSourcesForPlant(plantId: String): Flow<List<PlantSource>>
}
