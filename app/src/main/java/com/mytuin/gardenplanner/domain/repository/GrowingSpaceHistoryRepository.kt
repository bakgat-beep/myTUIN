package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.GrowingSpaceHistory
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for GrowingSpaceHistory.
 *
 * DEC-041. History rows are read-only from the domain's perspective;
 * they are created by GrowingSpaceRepository.updateGeometry inside a
 * transaction, not by direct calls from use cases.
 */
interface GrowingSpaceHistoryRepository {

    fun observeHistoryForSpace(growingSpaceId: String): Flow<List<GrowingSpaceHistory>>

    suspend fun getHistoryForSpace(growingSpaceId: String): List<GrowingSpaceHistory>

    /**
     * The state that was effective at [atMillis], or null if no history
     * row covers that instant. Answers DEC-041's question:
     * "what did this space look like when the 2025 crop was planted?"
     */
    suspend fun getHistoryAsOf(
        growingSpaceId: String,
        atMillis: Long,
    ): GrowingSpaceHistory?
}