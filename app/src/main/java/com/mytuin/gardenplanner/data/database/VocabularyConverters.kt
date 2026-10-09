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
import com.mytuin.gardenplanner.domain.vocabulary.GeographicScope
import com.mytuin.gardenplanner.domain.vocabulary.GeometryType
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.HarvestLossCause
import com.mytuin.gardenplanner.domain.vocabulary.HarvestLossSeverity
import com.mytuin.gardenplanner.domain.vocabulary.HarvestSizeCategory
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.domain.vocabulary.MeasurementProperty
import com.mytuin.gardenplanner.domain.vocabulary.MeasurementUnit
import com.mytuin.gardenplanner.domain.vocabulary.ObservationType
import com.mytuin.gardenplanner.domain.vocabulary.PlanTargetType
import com.mytuin.gardenplanner.domain.vocabulary.PlanningPriority
import com.mytuin.gardenplanner.domain.vocabulary.PlanningStatus
import com.mytuin.gardenplanner.domain.vocabulary.PlantAliasType
import com.mytuin.gardenplanner.domain.vocabulary.PlantInstanceLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.PlantLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.PlantingMethod
import com.mytuin.gardenplanner.domain.vocabulary.PlantingStockType
import com.mytuin.gardenplanner.domain.vocabulary.ProblemCategory
import com.mytuin.gardenplanner.domain.vocabulary.ProblemEvidenceDirection
import com.mytuin.gardenplanner.domain.vocabulary.ProblemSeverity
import com.mytuin.gardenplanner.domain.vocabulary.ProblemStatus
import com.mytuin.gardenplanner.domain.vocabulary.PruningMethod
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.domain.vocabulary.SoilWorkMethod
import com.mytuin.gardenplanner.domain.vocabulary.SourceStatus
import com.mytuin.gardenplanner.domain.vocabulary.SourceType
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

    @TypeConverter
    fun harvestSizeCategoryToId(value: HarvestSizeCategory): String = value.id

    @TypeConverter
    fun idToHarvestSizeCategory(id: String): HarvestSizeCategory =
        HarvestSizeCategory.fromId(id)
            ?: error("Unknown HarvestSizeCategory id stored in database: '$id'")

    @TypeConverter
    fun harvestLossCauseToId(value: HarvestLossCause): String = value.id

    @TypeConverter
    fun idToHarvestLossCause(id: String): HarvestLossCause =
        HarvestLossCause.fromId(id)
            ?: error("Unknown HarvestLossCause id stored in database: '$id'")

    @TypeConverter
    fun harvestLossSeverityToId(value: HarvestLossSeverity): String = value.id

    @TypeConverter
    fun idToHarvestLossSeverity(id: String): HarvestLossSeverity =
        HarvestLossSeverity.fromId(id)
            ?: error("Unknown HarvestLossSeverity id stored in database: '$id'")

    @TypeConverter
    fun problemCategoryToId(value: ProblemCategory): String = value.id

    @TypeConverter
    fun idToProblemCategory(id: String): ProblemCategory =
        ProblemCategory.fromId(id)
            ?: error("Unknown ProblemCategory id stored in database: '$id'")

    @TypeConverter
    fun problemSeverityToId(value: ProblemSeverity): String = value.id

    @TypeConverter
    fun idToProblemSeverity(id: String): ProblemSeverity =
        ProblemSeverity.fromId(id)
            ?: error("Unknown ProblemSeverity id stored in database: '$id'")

    @TypeConverter
    fun problemStatusToId(value: ProblemStatus): String = value.id

    @TypeConverter
    fun idToProblemStatus(id: String): ProblemStatus =
        ProblemStatus.fromId(id)
            ?: error("Unknown ProblemStatus id stored in database: '$id'")

    @TypeConverter
    fun problemEvidenceDirectionToId(value: ProblemEvidenceDirection): String = value.id

    @TypeConverter
    fun idToProblemEvidenceDirection(id: String): ProblemEvidenceDirection =
        ProblemEvidenceDirection.fromId(id)
            ?: error("Unknown ProblemEvidenceDirection id stored in database: '$id'")

    @TypeConverter
    fun planningStatusToId(value: PlanningStatus): String = value.id

    @TypeConverter
    fun idToPlanningStatus(id: String): PlanningStatus =
        PlanningStatus.fromId(id)
            ?: error("Unknown PlanningStatus id stored in database: '$id'")

    @TypeConverter
    fun planningPriorityToId(value: PlanningPriority): String = value.id

    @TypeConverter
    fun idToPlanningPriority(id: String): PlanningPriority =
        PlanningPriority.fromId(id)
            ?: error("Unknown PlanningPriority id stored in database: '$id'")

    @TypeConverter
    fun planTargetTypeToId(value: PlanTargetType): String = value.id

    @TypeConverter
    fun idToPlanTargetType(id: String): PlanTargetType =
        PlanTargetType.fromId(id)
            ?: error("Unknown PlanTargetType id stored in database: '$id'")

    @TypeConverter
    fun sourceTypeToId(value: SourceType): String = value.id

    @TypeConverter
    fun idToSourceType(id: String): SourceType =
        SourceType.fromId(id)
            ?: error("Unknown SourceType id stored in database: '$id'")

    @TypeConverter
    fun sourceStatusToId(value: SourceStatus): String = value.id

    @TypeConverter
    fun idToSourceStatus(id: String): SourceStatus =
        SourceStatus.fromId(id)
            ?: error("Unknown SourceStatus id stored in database: '$id'")

    @TypeConverter
    fun geographicScopeToId(value: GeographicScope): String = value.id

    @TypeConverter
    fun idToGeographicScope(id: String): GeographicScope =
        GeographicScope.fromId(id)
            ?: error("Unknown GeographicScope id stored in database: '$id'")
}
