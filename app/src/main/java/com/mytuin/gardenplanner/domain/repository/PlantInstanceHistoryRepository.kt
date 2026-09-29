package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.PlantInstanceHistory
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for PlantInstanceHistory.
 *
 * DEC-041. History rows are read-only from the domain's perspective;
 * they are created by PlantInstanceRepository.updateLocation inside a
 * transaction, not by direct calls from use cases.
 */
interface PlantInstanceHistoryRepository {
    fun observeHistoryFor(plantInstanceId: String): Flow<List<PlantInstanceHistory>>

    suspend fun getHistoryFor(plantInstanceId: String): List<PlantInstanceHistory>

    /**
     * The location that was effective at [atMillis], or null if no
     * history row covers that instant. Answers DEC-041's question:
     * "where was this plant on a given date?"
     */
    suspend fun getHistoryAsOf(
        plantInstanceId: String,
        atMillis: Long,
    ): PlantInstanceHistory?
}
