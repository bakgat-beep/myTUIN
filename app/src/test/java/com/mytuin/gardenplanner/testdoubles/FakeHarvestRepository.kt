package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.Harvest
import com.mytuin.gardenplanner.domain.repository.HarvestRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * Fake repository for Harvest.
 *
 * The fake tracks archived ids internally, since Harvest has no
 * status field (HL11). Archive does not cascade to the linked
 * Activity in the fake: that is a data-layer concern tested by
 * HarvestRepositoryTest in androidTest.
 */
class FakeHarvestRepository : HarvestRepository {
    data class ArchiveCall(
        val id: String,
        val at: Long,
    )

    private val store = MutableStateFlow<List<Harvest>>(emptyList())
    private val archivedIds = mutableSetOf<String>()
    private val archiveCalls = mutableListOf<ArchiveCall>()

    override fun observeHarvestsInGarden(
        gardenId: String,
        includeArchived: Boolean,
    ): Flow<List<Harvest>> =
        store.map { rows ->
            rows
                .filter { it.gardenId == gardenId }
                .let { filtered ->
                    if (includeArchived) filtered else filtered.filter { it.id !in archivedIds }
                }
        }

    override fun observeHarvestsForPlantInstance(
        plantInstanceId: String,
        includeArchived: Boolean,
    ): Flow<List<Harvest>> =
        store.map { rows ->
            rows
                .filter { it.plantInstanceId == plantInstanceId }
                .let { filtered ->
                    if (includeArchived) filtered else filtered.filter { it.id !in archivedIds }
                }
        }

    override fun observeHarvestsInGrowingSpace(
        growingSpaceId: String,
        includeArchived: Boolean,
    ): Flow<List<Harvest>> =
        store.map { rows ->
            rows
                .filter { it.growingSpaceId == growingSpaceId }
                .let { filtered ->
                    if (includeArchived) filtered else filtered.filter { it.id !in archivedIds }
                }
        }

    override fun observeHarvest(id: String): Flow<Harvest?> = store.map { rows -> rows.firstOrNull { it.id == id } }

    override suspend fun getHarvest(id: String): Harvest? = store.value.firstOrNull { it.id == id }

    override suspend fun insert(harvest: Harvest) {
        store.value = store.value + harvest
    }

    override suspend fun archive(
        id: String,
        archivedAt: Long,
    ) {
        if (store.value.none { it.id == id }) {
            throw NotFoundError("Harvest", id)
        }
        archivedIds += id
        archiveCalls += ArchiveCall(id, archivedAt)
    }

    fun snapshot(): List<Harvest> = store.value

    fun archivedIds(): Set<String> = archivedIds.toSet()

    fun archiveCalls(): List<ArchiveCall> = archiveCalls.toList()
}
