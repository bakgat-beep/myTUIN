package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.SoilRecord
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for SoilRecord.
 *
 * Standard pattern for a state record with RecordStatus.
 * Archive/restore follow Area, SpatialObject and PlantInstance.
 */
interface SoilRecordRepository {
    fun observeSoilRecordsInGarden(
        gardenId: String,
        includeArchived: Boolean = false,
    ): Flow<List<SoilRecord>>

    fun observeSoilRecord(id: String): Flow<SoilRecord?>

    suspend fun getSoilRecord(id: String): SoilRecord?

    suspend fun insert(soilRecord: SoilRecord)

    suspend fun archive(
        id: String,
        archivedAt: Long,
    )

    suspend fun restore(
        id: String,
        restoredAt: Long,
    )
}
