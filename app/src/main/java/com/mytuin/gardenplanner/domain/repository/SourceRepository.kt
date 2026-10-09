package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.Source
import com.mytuin.gardenplanner.domain.vocabulary.SourceStatus
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Source.
 *
 * Sources are application-wide reference data, not garden-scoped
 * (SC1). No garden parameter on any query. No archive/restore: Source
 * has no RecordStatus column (SC3 (a)); its lifecycle is expressed by
 * SourceStatus.
 */
interface SourceRepository {
    fun observeAllSources(): Flow<List<Source>>

    fun observeSource(id: String): Flow<Source?>

    suspend fun getSource(id: String): Source?

    suspend fun insert(source: Source)

    suspend fun updateStatus(
        id: String,
        status: SourceStatus,
    )
}
