package com.mytuin.gardenplanner.ui

import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import com.mytuin.gardenplanner.ui.navigation.MyTuinNavHost
import com.mytuin.gardenplanner.ui.theme.MyTuinTheme
import org.junit.Rule
import org.junit.Test

/**
 * Shell-level accessibility checks.
 *
 * PHASE_0_PROJECT_FOUNDATION §27, §31 (Presentation).
 * V1_VISUAL_DESIGN_SPECIFICATION §58, §59.
 *
 * A196 fallback applied: Compose BOM 2025.01.00 maps to Compose UI
 * 1.7.x, which does not have assertTouchHeightIsAtLeast /
 * assertTouchWidthIsAtLeast (those landed in Compose UI 1.8.0).
 * This file uses assertHeightIsAtLeast / assertWidthIsAtLeast, which
 * measure the node's layout bounds rather than its expanded touch
 * bounds.
 *
 * That is a weaker guarantee than the touch-target version: a
 * component could theoretically expand its touch target without
 * expanding its layout. For the FAB (56 dp) and Material 3
 * NavigationBarItem (fills the 80 dp bar), layout size and touch
 * target coincide, so the weaker assertion still catches the
 * regression this test exists to catch.
 *
 * When the Compose BOM is upgraded to 2025.02.00 or later (Compose UI
 * 1.8+), swap to the assertTouch* variants. One import change and two
 * call-site changes, in this file only.
 */
class ShellAccessibilityTest {
    @get:Rule
    val composeRule = createComposeRule()

    private fun setShell() {
        composeRule.setContent {
            MyTuinTheme {
                MyTuinNavHost()
            }
        }
    }

    @Test
    fun fab_meets_minimum_size() {
        setShell()
        composeRule
            .onNodeWithContentDescription("Add")
            .assertHeightIsAtLeast(48.dp)
            .assertWidthIsAtLeast(48.dp)
    }

    @Test
    fun each_navigation_destination_meets_minimum_size() {
        setShell()
        listOf("Home", "Garden", "Plants", "Planner", "Inbox").forEach { label ->
            composeRule
                .onAllNodesWithText(label)
                .onFirst()
                .assertHeightIsAtLeast(48.dp)
                .assertWidthIsAtLeast(48.dp)
        }
    }

    @Test
    fun fab_has_a_content_description_for_screen_readers() {
        setShell()
        composeRule.onNodeWithContentDescription("Add").assertIsDisplayed()
    }

    @Test
    fun home_screen_renders_an_empty_state() {
        setShell()
        composeRule.onNodeWithText("Your garden at a glance").assertIsDisplayed()
    }
}
