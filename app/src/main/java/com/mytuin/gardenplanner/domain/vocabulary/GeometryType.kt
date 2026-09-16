package com.mytuin.gardenplanner.domain.vocabulary

/**
 * GeometryType.
 *
 * GARDEN_VOCABULARIES.md §7. Three values: point, line, polygon.
 *
 * The canonical ids are the vocabulary's lowercase snake_case values.
 * The GeoJSON type names ("Point", "LineString", "Polygon") are a
 * separate concern handled by GeometryGeoJson in the data layer.
 * DEC-039: the domain stays independent of the map library's
 * naming.
 */
enum class GeometryType(override val id: String) : VocabularyValue {
    POINT("point"),
    LINE("line"),
    POLYGON("polygon"),
    ;

    companion object {
        fun fromId(id: String): GeometryType? =
            entries.firstOrNull { it.id == id }
    }
}