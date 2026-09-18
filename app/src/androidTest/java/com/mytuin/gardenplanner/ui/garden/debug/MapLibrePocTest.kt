package com.mytuin.gardenplanner.ui.garden.debug

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.ui.theme.MyTuinTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Compose test for MapLibrePoc (A159=c).
 *
 * The map view itself cannot be asserted from a JVM-side Compose test:
 * MapLibre renders natively, and its content is not in the Compose
 * semantics tree. What this test proves is that the composable
 * renders without crashing and that the empty-state path is reached
 * when no garden exists.
 *
 * The substantive proof of the coordinate pipeline is
 * MetresToWgs84Test in src/test.
 *
 * JUnit 4 per A139=a. Compose UI tests remain on JUnit 4 because
 * createComposeRule returns a JUnit 4 TestRule.
 */
@RunWith(AndroidJUnit4::class)
class MapLibrePocTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun poc_renders_without_crashing() {
        composeRule.setContent {
            MyTuinTheme {
                MapLibrePoc()
            }
        }
        // If we reached this point without an exception, the composable
        // and its MapView inflated successfully.
    }

    @Test
    fun poc_shows_empty_state_text_when_no_garden_exists() {
        composeRule.setContent {
            MyTuinTheme {
                MapLibrePoc()
            }
        }
        // On a clean test device with no seeded Garden rows, the PoC
        // renders its empty-state message. If a Garden exists from a
        // previous test, this assertion will not fire; that is a
        // device-state dependency, not a PoC defect. The MapLibrePocTest
        // does not create gardens and the app's production database is
        // not touched by tests.
        composeRule.onNodeWithText("Create a garden to see it on the map.")
            .assertExists()
    }
}