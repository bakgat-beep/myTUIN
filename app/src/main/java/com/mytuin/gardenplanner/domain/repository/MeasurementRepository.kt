package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.Measurement
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Measurement.
 *
 * No archive or restore: Measurement has no status column (M7, M8).
 * The queries take no includeArchived parameter for the same reason.
 * Same shape as PlantInstanceHistoryRepository.
 */
interface MeasurementRepository {
    fun observeMeasurementsInGarden(gardenId: String): Flow<List<Measurement>>

    fun observeMeasurementsForPlantInstance(plantInstanceId: String): Flow<List<Measurement>>

    fun observeMeasurementsInGrowingSpace(growingSpaceId: String): Flow<List<Measurement>>

    fun observeMeasurement(id: String): Flow<Measurement?>

    suspend fun getMeasurement(id: String): Measurement?

    suspend fun insert(measurement: Measurement)
}
