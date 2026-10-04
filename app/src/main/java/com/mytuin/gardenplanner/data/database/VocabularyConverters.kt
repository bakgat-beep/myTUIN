package com.mytuin.gardenplanner.data.database

import androidx.room.TypeConverter
import com.mytuin.gardenplanner.domain.vocabulary.ActivityQuantityUnit
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.AreaType
import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.DataOrigin
import com.mytuin.gardenplanner.domain.vocabulary.FeedingMethod
import com.mytuin.gardenplanner.domain.vocabulary.GardenPlantPreferenceKind
import com.mytuin.gardenplanner.domain.vocabulary.GardenPreferenceKey
import com.mytuin.gardenplanner.domain.vocabulary.GardenPriority
import com.mytuin.gardenplanner.domain.vocabulary.GeometryType
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.domain.vocabulary.MeasurementProperty
import com.mytuin.gardenplanner.domain.vocabulary.MeasurementUnit
import com.mytuin.gardenplanner.domain.vocabulary.ObservationType
import com.mytuin.gardenplanner.domain.vocabulary.PlantAliasType
import com.mytuin.gardenplanner.domain.vocabulary.PlantInstanceLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.PlantLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.PlantingMethod
import com.mytuin.gardenplanner.domain.vocabulary.PlantingStockType
import com.mytuin.gardenplanner.domain.vocabulary.PruningMethod
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.domain.vocabulary.SoilWorkMethod
import com.mytuin.gardenplanner.domain.vocabulary.WateringMethod

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
    fun plantInstanceLifecycleToId(value: PlantInstanceLifecycle): String = value.id

    @TypeConverter
    fun idToPlantInstanceLifecycle(id: String): PlantInstanceLifecycle =
        PlantInstanceLifecycle.fromId(id)
            ?: error("Unknown PlantInstanceLifecycle id stored in database: '$id'")

    @TypeConverter
    fun plantingStockTypeToId(value: PlantingStockType): String = value.id

    @TypeConverter
    fun idToPlantingStockType(id: String): PlantingStockType =
        PlantingStockType.fromId(id)
            ?: error("Unknown PlantingStockType id stored in database: '$id'")

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

    @TypeConverter
    fun activityTypeToId(value: ActivityType): String = value.id

    @TypeConverter
    fun idToActivityType(id: String): ActivityType =
        ActivityType.fromId(id)
            ?: error("Unknown ActivityType id stored in database: '$id'")

    @TypeConverter
    fun plantingMethodToId(value: PlantingMethod): String = value.id

    @TypeConverter
    fun idToPlantingMethod(id: String): PlantingMethod =
        PlantingMethod.fromId(id)
            ?: error("Unknown PlantingMethod id stored in database: '$id'")

    @TypeConverter
    fun wateringMethodToId(value: WateringMethod): String = value.id

    @TypeConverter
    fun idToWateringMethod(id: String): WateringMethod =
        WateringMethod.fromId(id)
            ?: error("Unknown WateringMethod id stored in database: '$id'")

    @TypeConverter
    fun feedingMethodToId(value: FeedingMethod): String = value.id

    @TypeConverter
    fun idToFeedingMethod(id: String): FeedingMethod =
        FeedingMethod.fromId(id)
            ?: error("Unknown FeedingMethod id stored in database: '$id'")

    @TypeConverter
    fun pruningMethodToId(value: PruningMethod): String = value.id

    @TypeConverter
    fun idToPruningMethod(id: String): PruningMethod =
        PruningMethod.fromId(id)
            ?: error("Unknown PruningMethod id stored in database: '$id'")

    @TypeConverter
    fun soilWorkMethodToId(value: SoilWorkMethod): String = value.id

    @TypeConverter
    fun idToSoilWorkMethod(id: String): SoilWorkMethod =
        SoilWorkMethod.fromId(id)
            ?: error("Unknown SoilWorkMethod id stored in database: '$id'")

    @TypeConverter
    fun activityQuantityUnitToId(value: ActivityQuantityUnit): String = value.id

    @TypeConverter
    fun idToActivityQuantityUnit(id: String): ActivityQuantityUnit =
        ActivityQuantityUnit.fromId(id)
            ?: error("Unknown ActivityQuantityUnit id stored in database: '$id'")

    @TypeConverter
    fun dataOriginToId(value: DataOrigin): String = value.id

    @TypeConverter
    fun idToDataOrigin(id: String): DataOrigin =
        DataOrigin.fromId(id)
            ?: error("Unknown DataOrigin id stored in database: '$id'")

    @TypeConverter
    fun observationTypeToId(value: ObservationType): String = value.id

    @TypeConverter
    fun idToObservationType(id: String): ObservationType =
        ObservationType.fromId(id)
            ?: error("Unknown ObservationType id stored in database: '$id'")

    @TypeConverter
    fun confidenceToId(value: Confidence): String = value.id

    @TypeConverter
    fun idToConfidence(id: String): Confidence =
        Confidence.fromId(id)
            ?: error("Unknown Confidence id stored in database: '$id'")

    @TypeConverter
    fun measurementPropertyToId(value: MeasurementProperty): String = value.id

    @TypeConverter
    fun idToMeasurementProperty(id: String): MeasurementProperty =
        MeasurementProperty.fromId(id)
            ?: error("Unknown MeasurementProperty id stored in database: '$id'")

    @TypeConverter
    fun measurementUnitToId(value: MeasurementUnit): String = value.id

    @TypeConverter
    fun idToMeasurementUnit(id: String): MeasurementUnit =
        MeasurementUnit.fromId(id)
            ?: error("Unknown MeasurementUnit id stored in database: '$id'")
}
