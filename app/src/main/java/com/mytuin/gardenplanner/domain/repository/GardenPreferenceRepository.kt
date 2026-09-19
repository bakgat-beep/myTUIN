package com.mytuin.gardenplanner.domain.repository

import com.mytuin.gardenplanner.domain.model.garden.GardenPlantPreference
import com.mytuin.gardenplanner.domain.model.garden.GardenPreference
import com.mytuin.gardenplanner.domain.vocabulary.GardenPlantPreferenceKind
import com.mytuin.gardenplanner.domain.vocabulary.GardenPreferenceKey
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for garden-affecting preferences.
 *
 * DEC-042, V1_DATABASE_SCHEMA §48. A171=b, this is the Room side.
 * The DataStore side is a separate step.
 *
 * Writes pre-check that the referenced Garden (and Plant, where
 * applicable) exists, so a missing parent throws NotFoundError rather
 * than an ambiguous constraint failure. The foreign keys remain as
 * backstops.
 *
 * No production consumer yet. Recommendation engine arrives later.
 */
interface GardenPreferenceRepository {
    fun observePreferences(gardenId: String): Flow<List<GardenPreference>>

    suspend fun getPreferences(gardenId: String): List<GardenPreference>

    suspend fun getPreference(
        gardenId: String,
        key: GardenPreferenceKey,
    ): GardenPreference?

    suspend fun setPreference(preference: GardenPreference)

    suspend fun clearPreference(
        gardenId: String,
        key: GardenPreferenceKey,
    )

    fun observePlantPreferences(
        gardenId: String,
        kind: GardenPlantPreferenceKind,
    ): Flow<List<GardenPlantPreference>>

    suspend fun getPlantPreferences(
        gardenId: String,
        kind: GardenPlantPreferenceKind,
    ): List<GardenPlantPreference>

    suspend fun addPlantPreference(preference: GardenPlantPreference)

    suspend fun removePlantPreference(
        gardenId: String,
        plantId: String,
        kind: GardenPlantPreferenceKind,
    )
}
