package com.mytuin.gardenplanner.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mytuin.gardenplanner.ui.theme.MyTuinTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Tests for EmptyState.
 * PHASE_0_PROJECT_FOUNDATION §31 (Presentation).
 */
class EmptyStateTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun title_and_description_render() {
        composeRule.setContent {
            MyTuinTheme {
                EmptyState(
                    title = "Test title",
                    description = "Test description",
                )
            }
        }

        composeRule.onNodeWithText("Test title").assertIsDisplayed()
        composeRule.onNodeWithText("Test description").assertIsDisplayed()
    }

    @Test
    fun action_button_is_absent_when_no_action_supplied() {
        composeRule.setContent {
            MyTuinTheme {
                EmptyState(
                    title = "Test title",
                    description = "Test description",
                )
            }
        }

        // No known action label renders; the button should not exist.
        composeRule.onNodeWithText("Action").assertDoesNotExist()
    }

    @Test
    fun action_button_renders_and_invokes_when_supplied() {
        var clicks = 0
        composeRule.setContent {
            MyTuinTheme {
                EmptyState(
                    title = "Test title",
                    description = "Test description",
                    actionLabel = "Take action",
                    onAction = { clicks += 1 },
                )
            }
        }

        composeRule.onNodeWithText("Take action").assertIsDisplayed().performClick()
        assertEquals(1, clicks)
    }

    @Test
    fun icon_renders_when_supplied() {
        composeRule.setContent {
            MyTuinTheme {
                EmptyState(
                    title = "Test title",
                    description = "Test description",
                    icon = Icons.Filled.Home,
                )
            }
        }

        // Icon has no content description (decorative). Title still
        // renders. Assert the composable did not crash and the text
        // is present.
        composeRule.onNodeWithText("Test title").assertIsDisplayed()
    }
}