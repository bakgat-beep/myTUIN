package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.dao.CultivarDao
import com.mytuin.gardenplanner.data.dao.PlantAliasDao
import com.mytuin.gardenplanner.data.dao.PlantDao
import com.mytuin.gardenplanner.data.dao.PlantMultiValueDao
import com.mytuin.gardenplanner.domain.model.plant.Cultivar
import com.mytuin.gardenplanner.domain.model.plant.Plant
import com.mytuin.gardenplanner.domain.model.plant.PlantAlias
import com.mytuin.gardenplanner.domain.model.plant.PlantMultiValue
import com.mytuin.gardenplanner.domain.repository.PlantRepository
import com.mytuin.gardenplanner.domain.vocabulary.PlantMultiValueKind
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlantRepositoryImpl
    @Inject
    constructor(
        private val plantDao: PlantDao,
        private val plantAliasDao: PlantAliasDao,
        private val cultivarDao: CultivarDao,
        private val plantMultiValueDao: PlantMultiValueDao,
    ) : PlantRepository {
        override fun observePlants(): Flow<List<Plant>> = plantDao.observeAll().map { rows -> rows.map { it.toDomain() } }

        override suspend fun getPlant(id: String): Plant? = plantDao.getById(id)?.toDomain()

        override fun observeAliasesForPlant(plantId: String): Flow<List<PlantAlias>> =
            plantAliasDao
                .observeForPlant(plantId)
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeCultivarsForPlant(plantId: String): Flow<List<Cultivar>> =
            cultivarDao
                .observeForPlant(plantId)
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeAllMultiValuesForPlant(plantId: String): Flow<List<PlantMultiValue>> =
            plantMultiValueDao
                .observeForPlant(plantId)
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeMultiValuesForPlant(
            plantId: String,
            kind: PlantMultiValueKind,
        ): Flow<List<PlantMultiValue>> =
            plantMultiValueDao
                .observeForPlantByKind(plantId, kind)
                .map { rows -> rows.map { it.toDomain() } }
    }
