package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.Observation
import com.mytuin.gardenplanner.domain.repository.ObservationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/**
 * Fake repository for Observation.
 *
 * The fake tracks archived ids internally, since Observation has no
 * status field (O5). Archive does not cascade to the linked Activity
 * in the fake: that is a data-layer concern tested by
 * ObservationRepositoryTest in androidTest.
 */
class FakeObservationRepository : ObservationRepository {
    data class ArchiveCall(
        val id: String,
        val at: Long,
    )

    private val store = MutableStateFlow<List<Observation>>(emptyList())
    private val archivedIds = mutableSetOf<String>()
    private val archiveCalls = mutableListOf<ArchiveCall>()

    override fun observeObservationsInGarden(
        gardenId: String,
        includeArchived: Boolean,
    ): Flow<List<Observation>> =
        store.map { rows ->
            rows
                .filter { it.gardenId == gardenId }
                .let { filtered ->
                    if (includeArchived) filtered else filtered.filter { it.id !in archivedIds }
                }
        }

    override fun observeObservationsForPlantInstance(
        plantInstanceId: String,
        includeArchived: Boolean,
    ): Flow<List<Observation>> =
        store.map { rows ->
            rows
                .filter { it.plantInstanceId == plantInstanceId }
                .let { filtered ->
                    if (includeArchived) filtered else filtered.filter { it.id !in archivedIds }
                }
        }

    override fun observeObservationsInGrowingSpace(
        growingSpaceId: String,
        includeArchived: Boolean,
    ): Flow<List<Observation>> =
        store.map { rows ->
            rows
                .filter { it.growingSpaceId == growingSpaceId }
                .let { filtered ->
                    if (includeArchived) filtered else filtered.filter { it.id !in archivedIds }
                }
        }

    override fun observeObservation(id: String): Flow<Observation?> = store.map { rows -> rows.firstOrNull { it.id == id } }

    override suspend fun getObservation(id: String): Observation? = store.value.firstOrNull { it.id == id }

    override suspend fun insert(observation: Observation) {
        store.value = store.value + observation
    }

    override suspend fun archive(
        id: String,
        archivedAt: Long,
    ) {
        if (store.value.none { it.id == id }) {
            throw NotFoundError("Observation", id)
        }
        archivedIds += id
        archiveCalls += ArchiveCall(id, archivedAt)
    }

    fun snapshot(): List<Observation> = store.value

    fun archivedIds(): Set<String> = archivedIds.toSet()

    fun archiveCalls(): List<ArchiveCall> = archiveCalls.toList()
}
