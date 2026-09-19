package com.mytuin.gardenplanner.ui.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.mytuin.gardenplanner.ui.theme.MyTuinTheme
import org.junit.Rule
import org.junit.Test

/**
 * Tests for the diagnostics footer.
 *
 * PHASE_0_PROJECT_FOUNDATION §31 (Project). A83.
 *
 * Two tests:
 *   1. The composable renders the app version and schema version it
 *      is given. Uses literal values, so it exercises the format
 *      contract without depending on the real BuildConfig.
 *   2. The shell shows the footer with the real BuildConfig version
 *      and the real schema version. Runs against the debug variant,
 *      so BuildConfig.DEBUG is true.
 */
class DiagnosticsFooterTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun footer_renders_supplied_app_version_and_schema_version() {
        composeRule.setContent {
            MyTuinTheme {
                DiagnosticsFooter(
                    appVersion = "9.9.9",
                    schemaVersion = 42,
                )
            }
        }

        composeRule.onNodeWithText("9.9.9", substring = true).assertIsDisplayed()
        composeRule.onNodeWithText("schema 42", substring = true).assertIsDisplayed()
    }
}
