package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.plant.RotationGroup
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for RotationGroup.
 *
 * P41E-5: read-only in this step. No write path.
 *
 * System groups (garden_id null) are available to every garden.
 * User-defined groups are scoped to the garden that created them.
 * `observeAvailableGroupsForGarden` returns both.
 */
interface RotationGroupRepository {
    fun observeSystemGroups(): Flow<List<RotationGroup>>

    fun observeGroupsForGarden(gardenId: String): Flow<List<RotationGroup>>

    fun observeAvailableGroupsForGarden(gardenId: String): Flow<List<RotationGroup>>

    fun observeGroup(id: String): Flow<RotationGroup?>

    suspend fun getGroup(id: String): RotationGroup?
}
