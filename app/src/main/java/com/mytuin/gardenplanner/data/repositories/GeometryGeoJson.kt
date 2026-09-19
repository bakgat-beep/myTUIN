package com.mytuin.gardenplanner.data.repositories

import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import org.json.JSONArray
import org.json.JSONObject

/**
 * GeoJSON (RFC 7946) serialisation for the domain Geometry type.
 *
 * A50=GeoJSON. Chosen over WKT because DEC-039 names MapLibre GL
 * Native as the V1 map technology, and MapLibre consumes GeoJSON
 * natively. Storing WKT would force a conversion on every map
 * interaction.
 *
 * RFC 7946 names its geometry types "Point", "LineString" and
 * "Polygon". Our canonical vocabulary ids are "point", "line" and
 * "polygon" (GARDEN_VOCABULARIES §7). This file bridges the two.
 *
 * Uses org.json (Android-provided since API 1). No new dependency.
 *
 * Polygons: encoder writes a single outer ring. Decoder rejects
 * polygons with holes rather than silently dropping them. V1 does
 * not model holes (step 5c notes).
 */
object GeometryGeoJson {
    fun encode(geometry: Geometry): String =
        when (geometry) {
            is Geometry.Point ->
                JSONObject()
                    .put("type", "Point")
                    .put("coordinates", encodeCoordinate(geometry.at))
                    .toString()

            is Geometry.LineString ->
                JSONObject()
                    .put("type", "LineString")
                    .put(
                        "coordinates",
                        JSONArray().apply {
                            geometry.vertices.forEach { put(encodeCoordinate(it)) }
                        },
                    ).toString()

            is Geometry.Polygon ->
                JSONObject()
                    .put("type", "Polygon")
                    .put(
                        "coordinates",
                        JSONArray().apply {
                            put(
                                JSONArray().apply {
                                    geometry.ring.forEach { put(encodeCoordinate(it)) }
                                },
                            )
                        },
                    ).toString()
        }

    fun decode(json: String): Geometry {
        val root = JSONObject(json)
        return when (val geoType = root.getString("type")) {
            "Point" ->
                Geometry.Point(
                    decodeCoordinate(root.getJSONArray("coordinates")),
                )

            "LineString" ->
                Geometry.LineString(
                    root.getJSONArray("coordinates").let { arr ->
                        (0 until arr.length()).map { i ->
                            decodeCoordinate(arr.getJSONArray(i))
                        }
                    },
                )

            "Polygon" -> {
                val rings = root.getJSONArray("coordinates")
                require(rings.length() == 1) {
                    "V1 does not support polygons with holes; " +
                        "found ${rings.length()} rings"
                }
                val outer = rings.getJSONArray(0)
                Geometry.Polygon(
                    (0 until outer.length()).map { i ->
                        decodeCoordinate(outer.getJSONArray(i))
                    },
                )
            }

            else -> error("Unsupported GeoJSON geometry type: '$geoType'")
        }
    }

    private fun encodeCoordinate(c: Coordinate): JSONArray = JSONArray().put(c.x).put(c.y)

    private fun decodeCoordinate(arr: JSONArray): Coordinate = Coordinate(x = arr.getDouble(0), y = arr.getDouble(1))
}
