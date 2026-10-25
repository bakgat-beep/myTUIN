package com.mytuin.gardenplanner.ui.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.mytuin.gardenplanner.BuildConfig
import com.mytuin.gardenplanner.data.database.GARDEN_DATABASE_VERSION
import com.mytuin.gardenplanner.ui.navigation.MyTuinNavHost
import com.mytuin.gardenplanner.ui.theme.MyTuinTheme
import org.junit.Rule
import org.junit.Test

/**
 * The diagnostic footer is rendered at shell level (Step 9a) so it is
 * visible wherever the user navigates. This test asserts it is
 * visible on Home with the real app version and schema version.
 *
 * A83: connectedDebugAndroidTest runs against the debug variant, so
 * BuildConfig.DEBUG is true and the footer is rendered.
 */
class DiagnosticsOnHomeTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shell_shows_diagnostics_footer_with_real_values_in_debug() {
        composeRule.setContent {
            MyTuinTheme {
                MyTuinNavHost(homeScreenContent = { _, _ -> })
            }
        }

        composeRule
            .onNodeWithText(BuildConfig.VERSION_NAME, substring = true)
            .assertIsDisplayed()

        composeRule
            .onNodeWithText(GARDEN_DATABASE_VERSION.toString(), substring = true)
            .assertIsDisplayed()
    }
}
