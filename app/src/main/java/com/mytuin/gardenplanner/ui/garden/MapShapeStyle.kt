package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType

private const val FILL_OPACITY_NONE = 0.0f
private const val FILL_OPACITY_VERY_LIGHT = 0.20f
private const val FILL_OPACITY_LIGHT = 0.25f
private const val FILL_OPACITY_MEDIUM = 0.30f
private const val FILL_OPACITY_STRONG = 0.35f
private const val FILL_OPACITY_HEAVY = 0.40f

private const val STROKE_WIDTH_THIN = 1.0f
private const val STROKE_WIDTH_NORMAL = 2.0f
private const val STROKE_WIDTH_THICK = 3.0f

/**
 * Visual style for a MapShape (Step 9f).
 *
 * Colours are hex strings (MapLibre accepts "#RRGGBB" directly). The
 * palette follows V1_VISUAL_DESIGN_SPECIFICATION §5-7 (Forest, Sage,
 * Ochre, Clay, Stone) so the map and the rest of the app agree.
 *
 * Fill opacity and stroke width values are extracted to named
 * constants at the top of the file so that the shape-style table
 * reads as a table rather than as a list of magic numbers. The
 * `when` blocks are exhaustive over GrowingSpaceType and
 * InfrastructureType so the compiler catches a new vocabulary value
 * that lacks a colour; a Map lookup would lose that check. For that
 * reason this file is excluded from CyclomaticComplexMethod in
 * detekt.yml, alongside GrowingSpaceLabels.kt and
 * SpatialObjectLabels.kt.
 */
data class ShapeStyle(
    val fillColor: String,
    val fillOpacity: Float,
    val strokeColor: String,
    val strokeWidth: Float,
)

object MapShapeStyle {
    fun forKind(kind: MapShapeKind): ShapeStyle =
        when (kind) {
            is MapShapeKind.GrowingSpace -> forGrowingSpace(kind.type)
            is MapShapeKind.SpatialObject -> forSpatialObject(kind.type)
            MapShapeKind.Area -> areaStyle
        }

    private val areaStyle =
        ShapeStyle(
            fillColor = "#F2F0E9",
            fillOpacity = FILL_OPACITY_LIGHT,
            strokeColor = "#DDD9CE",
            strokeWidth = STROKE_WIDTH_THIN,
        )

    private val growingSpaceDefault =
        ShapeStyle(
            fillColor = "#7D9A82",
            fillOpacity = FILL_OPACITY_STRONG,
            strokeColor = "#356859",
            strokeWidth = STROKE_WIDTH_NORMAL,
        )

    private val spatialObjectLine =
        ShapeStyle(
            fillColor = "#2D302C",
            fillOpacity = FILL_OPACITY_NONE,
            strokeColor = "#2D302C",
            strokeWidth = STROKE_WIDTH_THICK,
        )

    private val spatialObjectPolygon =
        ShapeStyle(
            fillColor = "#9A978D",
            fillOpacity = FILL_OPACITY_LIGHT,
            strokeColor = "#2D302C",
            strokeWidth = STROKE_WIDTH_NORMAL,
        )

    private fun forGrowingSpace(type: GrowingSpaceType): ShapeStyle =
        when (type) {
            GrowingSpaceType.GARDEN_BED,
            GrowingSpaceType.RAISED_BED,
            GrowingSpaceType.IN_GROUND_BED,
            -> ShapeStyle("#8A6A4A", FILL_OPACITY_MEDIUM, "#5A4330", STROKE_WIDTH_NORMAL)

            GrowingSpaceType.CONTAINER,
            GrowingSpaceType.POT,
            GrowingSpaceType.PLANTER,
            -> ShapeStyle("#A95645", FILL_OPACITY_MEDIUM, "#6D2E23", STROKE_WIDTH_NORMAL)

            GrowingSpaceType.GREENHOUSE,
            GrowingSpaceType.POLYTUNNEL,
            -> ShapeStyle("#60788A", FILL_OPACITY_MEDIUM, "#3A4954", STROKE_WIDTH_NORMAL)

            GrowingSpaceType.ORCHARD,
            GrowingSpaceType.LAWN,
            -> ShapeStyle("#557A55", FILL_OPACITY_MEDIUM, "#356859", STROKE_WIDTH_NORMAL)

            GrowingSpaceType.ROW,
            GrowingSpaceType.BORDER,
            GrowingSpaceType.VERTICAL_SPACE,
            GrowingSpaceType.NURSERY_AREA,
            GrowingSpaceType.MIXED_AREA,
            GrowingSpaceType.OTHER,
            -> growingSpaceDefault
        }

    private fun forSpatialObject(type: InfrastructureType): ShapeStyle =
        when (type) {
            InfrastructureType.FENCE,
            InfrastructureType.WALL,
            -> spatialObjectLine

            InfrastructureType.PATH,
            InfrastructureType.DRAIN,
            -> ShapeStyle("#9A978D", FILL_OPACITY_VERY_LIGHT, "#6E6B62", STROKE_WIDTH_NORMAL)

            InfrastructureType.BUILDING,
            InfrastructureType.SHED,
            InfrastructureType.GATE,
            -> ShapeStyle("#7A7670", FILL_OPACITY_HEAVY, "#2D302C", STROKE_WIDTH_NORMAL)

            InfrastructureType.WATER_SOURCE,
            InfrastructureType.IRRIGATION,
            -> ShapeStyle("#60788A", FILL_OPACITY_MEDIUM, "#3A4954", STROKE_WIDTH_NORMAL)

            InfrastructureType.COMPOST_AREA,
            InfrastructureType.STORAGE_AREA,
            -> ShapeStyle("#8A6A4A", FILL_OPACITY_LIGHT, "#5A4330", STROKE_WIDTH_NORMAL)

            InfrastructureType.GREENHOUSE_STRUCTURE,
            InfrastructureType.SUPPORT_STRUCTURE,
            InfrastructureType.TRELLIS,
            InfrastructureType.SHADE_STRUCTURE,
            InfrastructureType.LIGHTING,
            InfrastructureType.UTILITY,
            InfrastructureType.OTHER,
            -> spatialObjectPolygon
        }
}
