package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.dao.GardenDao
import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.repository.GardenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GardenRepositoryImpl @Inject constructor(
    private val gardenDao: GardenDao,
) : GardenRepository {

    override fun observeGardens(): Flow<List<Garden>> =
        gardenDao.observeAll().map { rows -> rows.map { it.toDomain() } }

    override fun observeGarden(id: String): Flow<Garden?> =
        gardenDao.observeById(id).map { it?.toDomain() }

    override suspend fun getGarden(id: String): Garden? =
        gardenDao.getById(id)?.toDomain()

    override suspend fun insert(garden: Garden) =
        gardenDao.insert(garden.toEntity())
}