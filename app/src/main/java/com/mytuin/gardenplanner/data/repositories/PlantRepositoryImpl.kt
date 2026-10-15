package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.dao.CultivarDao
import com.mytuin.gardenplanner.data.dao.PlantAliasDao
import com.mytuin.gardenplanner.data.dao.PlantDao
import com.mytuin.gardenplanner.data.dao.PlantMultiValueDao
import com.mytuin.gardenplanner.data.dao.PlantNoteDao
import com.mytuin.gardenplanner.data.dao.PlantNumericFactDao
import com.mytuin.gardenplanner.data.dao.PlantProblemRelationshipDao
import com.mytuin.gardenplanner.data.dao.PlantRelationshipDao
import com.mytuin.gardenplanner.data.dao.PlantSourceDao
import com.mytuin.gardenplanner.data.dao.PlantTemporalFactDao
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
        private val plantNumericFactDao: PlantNumericFactDao,
        private val plantTemporalFactDao: PlantTemporalFactDao,
        private val plantNoteDao: PlantNoteDao,
        private val plantRelationshipDao: PlantRelationshipDao,
        private val plantProblemRelationshipDao: PlantProblemRelationshipDao,
        private val plantSourceDao: PlantSourceDao,
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

        override fun observeNumericFactsForPlant(plantId: String): Flow<List<PlantNumericFact>> =
            plantNumericFactDao
                .observeForPlant(plantId)
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeTemporalFactsForPlant(plantId: String): Flow<List<PlantTemporalFact>> =
            plantTemporalFactDao
                .observeForPlant(plantId)
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeNotesForPlant(plantId: String): Flow<List<PlantNote>> =
            plantNoteDao
                .observeForPlant(plantId)
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeRelationshipsForPlant(plantId: String): Flow<List<PlantRelationship>> =
            plantRelationshipDao
                .observeForPlant(plantId)
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeProblemRelationshipsForPlant(plantId: String): Flow<List<PlantProblemRelationship>> =
            plantProblemRelationshipDao
                .observeForPlant(plantId)
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeSourcesForPlant(plantId: String): Flow<List<PlantSource>> =
            plantSourceDao
                .observeForPlant(plantId)
                .map { rows -> rows.map { it.toDomain() } }
    }
