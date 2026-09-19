package com.mytuin.gardenplanner

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import org.maplibre.android.MapLibre

/**
 * Application entry point.
 *
 * MapLibre.getInstance is a one-time initialisation (step 6h, A152=a).
 * It registers the application context and connectivity state with the
 * MapLibre SDK. It must run before any MapView is constructed.
 *
 * PHASE_0_PROJECT_FOUNDATION §22; DEC-039.
 */
@HiltAndroidApp
class GardenPlannerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MapLibre.getInstance(this)
    }
}
