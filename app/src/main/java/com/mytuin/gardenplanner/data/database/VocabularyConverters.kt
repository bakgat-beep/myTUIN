package com.mytuin.gardenplanner.data.database

import androidx.room.TypeConverter
import com.mytuin.gardenplanner.domain.vocabulary.AreaType
import com.mytuin.gardenplanner.domain.vocabulary.GardenPlantPreferenceKind
import com.mytuin.gardenplanner.domain.vocabulary.GardenPreferenceKey
import com.mytuin.gardenplanner.domain.vocabulary.GardenPriority
import com.mytuin.gardenplanner.domain.vocabulary.GeometryType
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.domain.vocabulary.PlantAliasType
import com.mytuin.gardenplanner.domain.vocabulary.PlantLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus

class VocabularyConverters {
    @TypeConverter
    fun hemisphereToId(value: Hemisphere): String = value.id

    @TypeConverter
    fun idToHemisphere(id: String): Hemisphere =
        Hemisphere.fromId(id)
            ?: error("Unknown Hemisphere id stored in database: '$id'")

    @TypeConverter
    fun recordStatusToId(value: RecordStatus): String = value.id

    @TypeConverter
    fun idToRecordStatus(id: String): RecordStatus =
        RecordStatus.fromId(id)
            ?: error("Unknown RecordStatus id stored in database: '$id'")

    @TypeConverter
    fun plantLifecycleToId(value: PlantLifecycle): String = value.id

    @TypeConverter
    fun idToPlantLifecycle(id: String): PlantLifecycle =
        PlantLifecycle.fromId(id)
            ?: error("Unknown PlantLifecycle id stored in database: '$id'")

    @TypeConverter
    fun plantAliasTypeToId(value: PlantAliasType): String = value.id

    @TypeConverter
    fun idToPlantAliasType(id: String): PlantAliasType =
        PlantAliasType.fromId(id)
            ?: error("Unknown PlantAliasType id stored in database: '$id'")

    @TypeConverter
    fun growingSpaceTypeToId(value: GrowingSpaceType): String = value.id

    @TypeConverter
    fun idToGrowingSpaceType(id: String): GrowingSpaceType =
        GrowingSpaceType.fromId(id)
            ?: error("Unknown GrowingSpaceType id stored in database: '$id'")

    @TypeConverter
    fun geometryTypeToId(value: GeometryType): String = value.id

    @TypeConverter
    fun idToGeometryType(id: String): GeometryType =
        GeometryType.fromId(id)
            ?: error("Unknown GeometryType id stored in database: '$id'")

    @TypeConverter
    fun gardenPriorityToId(value: GardenPriority): String = value.id

    @TypeConverter
    fun idToGardenPriority(id: String): GardenPriority =
        GardenPriority.fromId(id)
            ?: error("Unknown GardenPriority id stored in database: '$id'")

    @TypeConverter
    fun gardenPreferenceKeyToId(value: GardenPreferenceKey): String = value.id

    @TypeConverter
    fun idToGardenPreferenceKey(id: String): GardenPreferenceKey =
        GardenPreferenceKey.fromId(id)
            ?: error("Unknown GardenPreferenceKey id stored in database: '$id'")

    @TypeConverter
    fun gardenPlantPreferenceKindToId(value: GardenPlantPreferenceKind): String = value.id

    @TypeConverter
    fun idToGardenPlantPreferenceKind(id: String): GardenPlantPreferenceKind =
        GardenPlantPreferenceKind.fromId(id)
            ?: error("Unknown GardenPlantPreferenceKind id stored in database: '$id'")

    @TypeConverter
    fun areaTypeToId(value: AreaType): String = value.id

    @TypeConverter
    fun idToAreaType(id: String): AreaType =
        AreaType.fromId(id)
            ?: error("Unknown AreaType id stored in database: '$id'")

    @TypeConverter
    fun infrastructureTypeToId(value: InfrastructureType): String = value.id

    @TypeConverter
    fun idToInfrastructureType(id: String): InfrastructureType =
        InfrastructureType.fromId(id)
            ?: error("Unknown InfrastructureType id stored in database: '$id'")
}
