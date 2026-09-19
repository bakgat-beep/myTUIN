package com.mytuin.gardenplanner.testdoubles

import android.app.Application

/**
 * Minimal Application for Robolectric tests.
 *
 * The production GardenPlannerApplication calls MapLibre.getInstance
 * in onCreate, which loads native libmaplibre.so. That library exists
 * only on Android device/emulator targets; on the JVM it throws
 * UnsatisfiedLinkError. Robolectric loads the real Application by
 * default, so without this override every src/test test that touches
 * the database fails.
 *
 * Selected via app/src/test/resources/robolectric.properties. See
 * https://robolectric.org/configuring/ for the mechanism.
 *
 * A139=a: src/test is JVM. MapLibre is a UI-layer concern and does
 * not belong in a unit test environment.
 */
class TestApplication : Application()
