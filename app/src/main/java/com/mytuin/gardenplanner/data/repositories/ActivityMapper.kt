package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.data.entities.ActivityEntity
import com.mytuin.gardenplanner.domain.model.garden.Activity
import com.mytuin.gardenplanner.domain.model.garden.ActivityDetail
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.FeedingMethod
import com.mytuin.gardenplanner.domain.vocabulary.PlantingMethod
import com.mytuin.gardenplanner.domain.vocabulary.PruningMethod
import com.mytuin.gardenplanner.domain.vocabulary.SoilWorkMethod
import com.mytuin.gardenplanner.domain.vocabulary.WateringMethod

/**
 * Mapping between ActivityEntity and Activity domain model.
 *
 * Enforces two invariants on the persistence boundary:
 *
 *   1. quantity and unit are paired: both null or both non-null.
 *   2. exactly the subtype column matching activity_type may be set;
 *      all others must be null.
 *
 * Both invariants are also enforced at the type level in the domain:
 * quantity/unit are separate fields but always constructed together;
 * the subtype lives inside the ActivityDetail sealed hierarchy.
 * The mapper is the bridge.
 */

private const val COLUMN_PLANTING = "planting_method"
private const val COLUMN_WATERING = "watering_method"
private const val COLUMN_FEEDING = "feeding_method"
private const val COLUMN_PRUNING = "pruning_method"
private const val COLUMN_SOIL_WORK = "soil_work_method"

fun ActivityEntity.toDomain(): Activity =
    Activity(
        id = id,
        gardenId = garden_id,
        activityType = activity_type,
        occurredAt = occurred_at,
        createdAt = created_at,
        areaId = area_id,
        growingSpaceId = growing_space_id,
        spatialObjectId = spatial_object_id,
        plantInstanceId = plant_instance_id,
        quantity = quantity,
        unit = unit,
        detail =
            detailFromEntity(
                type = activity_type,
                plantingMethod = planting_method,
                wateringMethod = watering_method,
                feedingMethod = feeding_method,
                pruningMethod = pruning_method,
                soilWorkMethod = soil_work_method,
            ),
        dataOrigin = data_origin,
        status = status,
        notes = notes,
    )

fun Activity.toEntity(): ActivityEntity {
    require((quantity == null) == (unit == null)) {
        "quantity and unit must both be set or both be null; " +
            "got quantity=$quantity, unit=$unit"
    }

    val columns = detailToColumns(activityType, detail)

    return ActivityEntity(
        id = id,
        garden_id = gardenId,
        activity_type = activityType,
        occurred_at = occurredAt,
        created_at = createdAt,
        area_id = areaId,
        growing_space_id = growingSpaceId,
        spatial_object_id = spatialObjectId,
        plant_instance_id = plantInstanceId,
        quantity = quantity,
        unit = unit,
        planting_method = columns.planting,
        watering_method = columns.watering,
        feeding_method = columns.feeding,
        pruning_method = columns.pruning,
        soil_work_method = columns.soilWork,
        data_origin = dataOrigin,
        status = status,
        notes = notes,
    )
}

private data class SubtypeColumns(
    val planting: PlantingMethod? = null,
    val watering: WateringMethod? = null,
    val feeding: FeedingMethod? = null,
    val pruning: PruningMethod? = null,
    val soilWork: SoilWorkMethod? = null,
)

/**
 * Returns the column name that may carry a value for [type], or null
 * if [type] has no subtype. All other subtype columns must be null.
 */
private fun expectedSubtypeColumnFor(type: ActivityType): String? =
    when (type) {
        ActivityType.PLANTING -> COLUMN_PLANTING
        ActivityType.WATERING -> COLUMN_WATERING
        ActivityType.FEEDING -> COLUMN_FEEDING
        ActivityType.PRUNING -> COLUMN_PRUNING
        ActivityType.SOIL_WORK -> COLUMN_SOIL_WORK
        else -> null
    }

private fun detailFromEntity(
    type: ActivityType,
    plantingMethod: PlantingMethod?,
    wateringMethod: WateringMethod?,
    feedingMethod: FeedingMethod?,
    pruningMethod: PruningMethod?,
    soilWorkMethod: SoilWorkMethod?,
): ActivityDetail {
    val expectedColumn = expectedSubtypeColumnFor(type)
    val offending =
        listOf(
            COLUMN_PLANTING to plantingMethod,
            COLUMN_WATERING to wateringMethod,
            COLUMN_FEEDING to feedingMethod,
            COLUMN_PRUNING to pruningMethod,
            COLUMN_SOIL_WORK to soilWorkMethod,
        ).firstOrNull { (name, value) -> value != null && name != expectedColumn }
    require(offending == null) {
        "Activity type $type must not carry ${offending?.first}"
    }

    return when (type) {
        ActivityType.PLANTING -> ActivityDetail.Planting(plantingMethod)
        ActivityType.WATERING -> ActivityDetail.Watering(wateringMethod)
        ActivityType.FEEDING -> ActivityDetail.Feeding(feedingMethod)
        ActivityType.PRUNING -> ActivityDetail.Pruning(pruningMethod)
        ActivityType.SOIL_WORK -> ActivityDetail.SoilWork(soilWorkMethod)
        else -> ActivityDetail.Plain
    }
}

private fun detailToColumns(
    type: ActivityType,
    detail: ActivityDetail,
): SubtypeColumns =
    when (type) {
        ActivityType.PLANTING -> {
            require(detail is ActivityDetail.Planting) {
                "Activity type $type requires ActivityDetail.Planting; got ${detail::class.simpleName}"
            }
            SubtypeColumns(planting = detail.method)
        }
        ActivityType.WATERING -> {
            require(detail is ActivityDetail.Watering) {
                "Activity type $type requires ActivityDetail.Watering; got ${detail::class.simpleName}"
            }
            SubtypeColumns(watering = detail.method)
        }
        ActivityType.FEEDING -> {
            require(detail is ActivityDetail.Feeding) {
                "Activity type $type requires ActivityDetail.Feeding; got ${detail::class.simpleName}"
            }
            SubtypeColumns(feeding = detail.method)
        }
        ActivityType.PRUNING -> {
            require(detail is ActivityDetail.Pruning) {
                "Activity type $type requires ActivityDetail.Pruning; got ${detail::class.simpleName}"
            }
            SubtypeColumns(pruning = detail.method)
        }
        ActivityType.SOIL_WORK -> {
            require(detail is ActivityDetail.SoilWork) {
                "Activity type $type requires ActivityDetail.SoilWork; got ${detail::class.simpleName}"
            }
            SubtypeColumns(soilWork = detail.method)
        }
        else -> {
            require(detail == ActivityDetail.Plain) {
                "Activity type $type requires ActivityDetail.Plain; got ${detail::class.simpleName}"
            }
            SubtypeColumns()
        }
    }
