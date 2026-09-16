package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.Garden
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for Garden.
 *
 * V1_TECHNICAL_ARCHITECTURE §9. Interface in the domain; implementation
 * in data/. Read methods mirror PlantRepository (A43=A).
 *
 * List queries return Flow; point lookups return suspend (A24).
 *
 * No uniqueness constraint on Garden name (A45; V1_DATABASE_SCHEMA §11
 * does not require it; DATA_MODEL §4 says renaming does not change
 * identity).
 */
interface GardenRepository {

    fun observeGardens(): Flow<List<Garden>>

    fun observeGarden(id: String): Flow<Garden?>

    suspend fun getGarden(id: String): Garden?

    suspend fun insert(garden: Garden)
}