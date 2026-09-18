package com.mytuin.gardenplanner.ui.garden.debug

import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.math.abs

/**
 * Tests for MetresToWgs84 (A159=b).
 *
 * Pure arithmetic, no MapLibre, no Android. JUnit 5 per A145=a.
 *
 * The conversion is an equirectangular approximation. Tests assert
 * round-trip identity and known-value correctness at garden scale.
 */
class MetresToWgs84Test {

    private val origin = Wgs84(latitude = -43.5321, longitude = 172.6362)

    @Test
    fun round_trip_is_identity_within_floating_point_tolerance() {
        val coordinates = listOf(
            Coordinate(0.0, 0.0),
            Coordinate(1.0, 0.0),
            Coordinate(0.0, 1.0),
            Coordinate(10.0, 10.0),
            Coordinate(-5.0, 3.5),
        )
        coordinates.forEach { coordinate ->
            val wgs = MetresToWgs84.toWgs84(origin, coordinate)
            val back = MetresToWgs84.toCoordinate(origin, wgs)
            assertEquals(coordinate.x, back.x, 1e-9, "x for $coordinate")
            assertEquals(coordinate.y, back.y, 1e-9, "y for $coordinate")
        }
    }

    @Test
    fun one_metre_north_is_approximately_eight_point_nine_e_micro_degrees() {
        val wgs = MetresToWgs84.toWgs84(origin, Coordinate(0.0, 1.0))
        val deltaLat = wgs.latitude - origin.latitude
        assertEquals(1.0 / 111_320.0, deltaLat, 1e-12)
    }

    @Test
    fun one_metre_east_scales_with_cosine_of_latitude() {
        val wgs = MetresToWgs84.toWgs84(origin, Coordinate(1.0, 0.0))
        val deltaLon = wgs.longitude - origin.longitude
        val expected = 1.0 / (111_320.0 * Math.cos(Math.toRadians(origin.latitude)))
        assertEquals(expected, deltaLon, 1e-12)
        // Sanity: at 43.5 degrees south, one degree of longitude is
        // materially shorter than one degree of latitude.
        assert(abs(deltaLon) > abs(1.0 / 111_320.0))
    }

    @Test
    fun a_ten_metre_square_maps_to_a_square_in_wgs84() {
        val corner = MetresToWgs84.toWgs84(origin, Coordinate(10.0, 10.0))
        val back = MetresToWgs84.toCoordinate(origin, corner)
        assertEquals(10.0, back.x, 1e-9)
        assertEquals(10.0, back.y, 1e-9)
    }
}