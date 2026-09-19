package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.data.dao.GardenPreferenceDao
import com.mytuin.gardenplanner.data.dao.PlantDao
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.GardenPlantPreference
import com.mytuin.gardenplanner.domain.model.garden.GardenPreference
import com.mytuin.gardenplanner.domain.repository.GardenPreferenceRepository
import com.mytuin.gardenplanner.domain.vocabulary.GardenPlantPreferenceKind
import com.mytuin.gardenplanner.domain.vocabulary.GardenPreferenceKey
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GardenPreferenceRepositoryImpl
    @Inject
    constructor(
        private val gardenPreferenceDao: GardenPreferenceDao,
        private val gardenDao: GardenDao,
        private val plantDao: PlantDao,
    ) : GardenPreferenceRepository {
        override fun observePreferences(gardenId: String): Flow<List<GardenPreference>> =
            gardenPreferenceDao
                .observePreferences(gardenId)
                .map { rows -> rows.map { it.toDomain() } }

        override suspend fun getPreferences(gardenId: String): List<GardenPreference> =
            gardenPreferenceDao.getPreferences(gardenId).map { it.toDomain() }

        override suspend fun getPreference(
            gardenId: String,
            key: GardenPreferenceKey,
        ): GardenPreference? = gardenPreferenceDao.getPreference(gardenId, key)?.toDomain()

        override suspend fun setPreference(preference: GardenPreference) {
            requireGardenExists(preference.gardenId)
            gardenPreferenceDao.upsert(preference.toEntity())
        }

        override suspend fun clearPreference(
            gardenId: String,
            key: GardenPreferenceKey,
        ) {
            requireGardenExists(gardenId)
            gardenPreferenceDao.deletePreference(gardenId, key)
        }

        override fun observePlantPreferences(
            gardenId: String,
            kind: GardenPlantPreferenceKind,
        ): Flow<List<GardenPlantPreference>> =
            gardenPreferenceDao
                .observePlantPreferences(gardenId, kind)
                .map { rows -> rows.map { it.toDomain() } }

        override suspend fun getPlantPreferences(
            gardenId: String,
            kind: GardenPlantPreferenceKind,
        ): List<GardenPlantPreference> =
            gardenPreferenceDao
                .getPlantPreferences(gardenId, kind)
                .map { it.toDomain() }

        override suspend fun addPlantPreference(preference: GardenPlantPreference) {
            requireGardenExists(preference.gardenId)
            plantDao.getById(preference.plantId)
                ?: throw NotFoundError("Plant", preference.plantId)
            gardenPreferenceDao.upsertPlantPreference(preference.toEntity())
        }

        override suspend fun removePlantPreference(
            gardenId: String,
            plantId: String,
            kind: GardenPlantPreferenceKind,
        ) {
            requireGardenExists(gardenId)
            gardenPreferenceDao.deletePlantPreference(gardenId, plantId, kind)
        }

        private suspend fun requireGardenExists(gardenId: String) {
            gardenDao.getById(gardenId) ?: throw NotFoundError("Garden", gardenId)
        }
    }
