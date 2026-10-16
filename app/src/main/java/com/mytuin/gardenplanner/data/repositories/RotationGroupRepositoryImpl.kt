package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.dao.RotationGroupDao
import com.mytuin.gardenplanner.domain.model.plant.RotationGroup
import com.mytuin.gardenplanner.domain.repository.RotationGroupRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RotationGroupRepositoryImpl
    @Inject
    constructor(
        private val rotationGroupDao: RotationGroupDao,
    ) : RotationGroupRepository {
        override fun observeSystemGroups(): Flow<List<RotationGroup>> =
            rotationGroupDao
                .observeSystemGroups()
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeGroupsForGarden(gardenId: String): Flow<List<RotationGroup>> =
            rotationGroupDao
                .observeGroupsForGarden(gardenId)
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeAvailableGroupsForGarden(gardenId: String): Flow<List<RotationGroup>> =
            rotationGroupDao
                .observeAvailableForGarden(gardenId)
                .map { rows -> rows.map { it.toDomain() } }

        override fun observeGroup(id: String): Flow<RotationGroup?> = rotationGroupDao.observeById(id).map { it?.toDomain() }

        override suspend fun getGroup(id: String): RotationGroup? = rotationGroupDao.getById(id)?.toDomain()
    }
