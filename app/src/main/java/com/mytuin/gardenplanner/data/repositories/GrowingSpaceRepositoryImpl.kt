package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.dao.GrowingSpaceDao
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.repository.GrowingSpaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GrowingSpaceRepositoryImpl @Inject constructor(
    private val growingSpaceDao: GrowingSpaceDao,
) : GrowingSpaceRepository {

    override fun observeGrowingSpacesInGarden(gardenId: String): Flow<List<GrowingSpace>> =
        growingSpaceDao.observeForGarden(gardenId)
            .map { rows -> rows.map { it.toDomain() } }

    override fun observeGrowingSpace(id: String): Flow<GrowingSpace?> =
        growingSpaceDao.observeById(id).map { it?.toDomain() }

    override suspend fun getGrowingSpace(id: String): GrowingSpace? =
        growingSpaceDao.getById(id)?.toDomain()

    override suspend fun insert(growingSpace: GrowingSpace) =
        growingSpaceDao.insert(growingSpace.toEntity())
}