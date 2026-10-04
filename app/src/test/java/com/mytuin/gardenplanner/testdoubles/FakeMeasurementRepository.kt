package com.mytuin.gardenplanner.testdoubles

import com.mytuin.gardenplanner.domain.model.garden.Measurement
import com.mytuin.gardenplanner.domain.repository.MeasurementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class FakeMeasurementRepository : MeasurementRepository {
    private val store = MutableStateFlow<List<Measurement>>(emptyList())

    override fun observeMeasurementsInGarden(gardenId: String): Flow<List<Measurement>> =
        store.map { rows -> rows.filter { it.gardenId == gardenId } }

    override fun observeMeasurementsForPlantInstance(plantInstanceId: String): Flow<List<Measurement>> =
        store.map { rows -> rows.filter { it.plantInstanceId == plantInstanceId } }

    override fun observeMeasurementsInGrowingSpace(growingSpaceId: String): Flow<List<Measurement>> =
        store.map { rows -> rows.filter { it.growingSpaceId == growingSpaceId } }

    override fun observeMeasurement(id: String): Flow<Measurement?> = store.map { rows -> rows.firstOrNull { it.id == id } }

    override suspend fun getMeasurement(id: String): Measurement? = store.value.firstOrNull { it.id == id }

    override suspend fun insert(measurement: Measurement) {
        store.value = store.value + measurement
    }

    fun snapshot(): List<Measurement> = store.value
}
