package com.mytuin.gardenplanner.ui.garden.debug

import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import kotlin.math.cos

/**
 * Garden-relative metres <-> WGS84 conversion for the MapLibre proof of
 * concept.
 *
 * A149=c: the domain's Coordinate is metres from a garden datum
 * (A50b=(i)). MapLibre's GeoJsonSource expects WGS84 lon/lat. This
 * object is the only place that conversion happens (A157).
 *
 * A158=a: the datum is the Garden's existing latitude/longitude when
 * present; otherwise a fallback constant supplied by the caller.
 *
 * The conversion is a flat equirectangular approximation. Valid for
 * garden-scale distances (a few hundred metres at most). At 100 m from
 * the datum the error is on the order of centimetres. The real Garden
 * screen will need a decision about whether that is acceptable; for a
 * PoC it is.
 *
 * No MapLibre types. Pure arithmetic, testable in src/test.
 *
 * PHASE_0_PROJECT_FOUNDATION §22, §23; DEC-039.
 */
data class Wgs84(
    val latitude: Double,
    val longitude: Double,
)

object MetresToWgs84 {
    private const val METRES_PER_DEGREE_LATITUDE = 111_320.0

    fun toWgs84(
        origin: Wgs84,
        coordinate: Coordinate,
    ): Wgs84 {
        val latitude = origin.latitude + coordinate.y / METRES_PER_DEGREE_LATITUDE
        val longitude =
            origin.longitude +
                coordinate.x / metresPerDegreeLongitude(origin.latitude)
        return Wgs84(latitude, longitude)
    }

    fun toCoordinate(
        origin: Wgs84,
        point: Wgs84,
    ): Coordinate {
        val y = (point.latitude - origin.latitude) * METRES_PER_DEGREE_LATITUDE
        val x =
            (point.longitude - origin.longitude) *
                metresPerDegreeLongitude(origin.latitude)
        return Coordinate(x, y)
    }

    private fun metresPerDegreeLongitude(latitude: Double): Double = METRES_PER_DEGREE_LATITUDE * cos(Math.toRadians(latitude))
}
