package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpaceHistory
import com.mytuin.gardenplanner.domain.model.garden.SpatialObjectHistory
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType

/**
 * Immutable timeline of garden history (Step 9i).
 *
 * Pure logic. No Android, no Compose, no MapLibre. Consumes domain
 * history rows and produces MapShape values at a requested instant.
 *
 * The reconstruction rule:
 *   - Before an entity's createdAt, it does not exist → not rendered.
 *   - If a history row covers [createdAt, now], its geometry is what
 *     the entity looked like at that instant.
 *   - If no history row covers (i.e., the entity was never edited
 *     after the queried instant), the current geometry is correct.
 *
 * The current geometry is never written to history (DEC-041: history
 * captures the PRIOR state). The fallback to current is therefore
 * not a hack; it is the correct answer when no subsequent edit
 * exists.
 *
 * minTime = earliest createdAt across all entities
 * maxTime = "now" captured at build time
 *
 * shapesAt does NOT clamp t. The caller (GardenMapHistoryViewModel)
 * is responsible for clamping to [minTime, maxTime]; the timeline's
 * contract is the simpler "shapes that existed at t", which means
 * shapesAt(minTime - 1) correctly returns nothing because no entity
 * existed before minTime.
 *
 * viewport is the union of every geometry across all time, precomputed
 * so scrubbing does not jiggle the camera.
 */
class GardenHistoryTimeline private constructor(
    val minTime: Long,
    val maxTime: Long,
    val hasAnyHistory: Boolean,
    val viewport: MapViewport,
    private val spaceSnapshots: List<GrowingSpaceHistorySnapshot>,
    private val objectSnapshots: List<SpatialObjectHistorySnapshot>,
) {
    /**
     * The shapes that existed at [t], without clamping.
     * Entities that did not yet exist at [t] are omitted.
     */
    fun shapesAt(t: Long): List<MapShape> {
        val result = mutableListOf<MapShape>()
        spaceSnapshots.forEach { snapshot ->
            if (t < snapshot.createdAt) return@forEach
            val geometry = spaceGeometryAt(snapshot, t) ?: return@forEach
            result += MapShape(snapshot.id, MapShapeKind.GrowingSpace(snapshot.type), geometry)
        }
        objectSnapshots.forEach { snapshot ->
            if (t < snapshot.createdAt) return@forEach
            val geometry = objectGeometryAt(snapshot, t)
            result += MapShape(snapshot.id, MapShapeKind.SpatialObject(snapshot.type), geometry)
        }
        return result
    }

    private fun spaceGeometryAt(
        snapshot: GrowingSpaceHistorySnapshot,
        t: Long,
    ): Geometry? {
        val row = snapshot.history.firstOrNull { t >= it.validFrom && t < it.validTo }
        return row?.geometry ?: snapshot.currentGeometry
    }

    private fun objectGeometryAt(
        snapshot: SpatialObjectHistorySnapshot,
        t: Long,
    ): Geometry {
        val row = snapshot.history.firstOrNull { t >= it.validFrom && t < it.validTo }
        return row?.geometry ?: snapshot.currentGeometry
    }

    companion object {
        /**
         * Returns null if no entity has ever had a geometry to render.
         * In that case the caller should surface the "no shapes" empty
         * state, not a timeline.
         */
        fun build(
            now: Long,
            spaces: List<GrowingSpaceHistorySnapshot>,
            objects: List<SpatialObjectHistorySnapshot>,
        ): GardenHistoryTimeline? {
            val allShapesAcrossTime = buildAllShapesAcrossTime(spaces, objects)
            if (allShapesAcrossTime.isEmpty()) return null
            val earliest =
                (spaces.map { it.createdAt } + objects.map { it.createdAt }).minOrNull()
                    ?: return null
            val maxTime = maxOf(now, earliest)
            val hasHistory =
                spaces.any { it.history.isNotEmpty() } ||
                    objects.any { it.history.isNotEmpty() }
            return GardenHistoryTimeline(
                minTime = earliest,
                maxTime = maxTime,
                hasAnyHistory = hasHistory,
                viewport = MapViewport.union(allShapesAcrossTime),
                spaceSnapshots = spaces,
                objectSnapshots = objects,
            )
        }

        private fun buildAllShapesAcrossTime(
            spaces: List<GrowingSpaceHistorySnapshot>,
            objects: List<SpatialObjectHistorySnapshot>,
        ): List<MapShape> {
            val result = mutableListOf<MapShape>()
            spaces.forEach { snapshot ->
                snapshot.currentGeometry?.let {
                    result += MapShape(snapshot.id, MapShapeKind.GrowingSpace(snapshot.type), it)
                }
                snapshot.history.forEach { row ->
                    row.geometry?.let {
                        result += MapShape(snapshot.id, MapShapeKind.GrowingSpace(snapshot.type), it)
                    }
                }
            }
            objects.forEach { snapshot ->
                result += MapShape(snapshot.id, MapShapeKind.SpatialObject(snapshot.type), snapshot.currentGeometry)
                snapshot.history.forEach { row ->
                    result += MapShape(snapshot.id, MapShapeKind.SpatialObject(snapshot.type), row.geometry)
                }
            }
            return result
        }
    }
}

data class GrowingSpaceHistorySnapshot(
    val id: String,
    val type: GrowingSpaceType,
    val createdAt: Long,
    val currentGeometry: Geometry?,
    val history: List<GrowingSpaceHistory>,
)

data class SpatialObjectHistorySnapshot(
    val id: String,
    val type: InfrastructureType,
    val createdAt: Long,
    val currentGeometry: Geometry,
    val history: List<SpatialObjectHistory>,
)
