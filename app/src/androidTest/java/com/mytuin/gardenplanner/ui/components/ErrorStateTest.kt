package com.mytuin.gardenplanner.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.mytuin.gardenplanner.ui.theme.MyTuinTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Tests for ErrorState.
 * PHASE_0_PROJECT_FOUNDATION §31 (Presentation).
 */
class ErrorStateTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun message_renders() {
        composeRule.setContent {
            MyTuinTheme {
                ErrorState(message = "We couldn't save that change.")
            }
        }

        composeRule
            .onNodeWithText("We couldn't save that change.")
            .assertIsDisplayed()
    }

    @Test
    fun retry_button_is_absent_when_no_callback_supplied() {
        composeRule.setContent {
            MyTuinTheme {
                ErrorState(message = "Something went wrong.")
            }
        }

        composeRule.onNodeWithText("Retry").assertDoesNotExist()
    }

    @Test
    fun retry_button_renders_and_invokes_when_supplied() {
        var retries = 0
        composeRule.setContent {
            MyTuinTheme {
                ErrorState(
                    message = "Something went wrong.",
                    onRetry = { retries += 1 },
                )
            }
        }

        composeRule.onNodeWithText("Retry").assertIsDisplayed().performClick()
        assertEquals(1, retries)
    }

    @Test
    fun details_are_hidden_until_toggled() {
        composeRule.setContent {
            MyTuinTheme {
                ErrorState(
                    message = "Something went wrong.",
                    details = "Technical detail text.",
                )
            }
        }

        // Details are hidden by default.
        composeRule.onNodeWithText("Technical detail text.").assertDoesNotExist()

        // Toggle reveals them.
        composeRule.onNodeWithText("Details").performClick()
        composeRule.onNodeWithText("Technical detail text.").assertIsDisplayed()
    }

    @Test
    fun details_toggle_hides_again_on_second_press() {
        composeRule.setContent {
            MyTuinTheme {
                ErrorState(
                    message = "Something went wrong.",
                    details = "Technical detail text.",
                )
            }
        }

        composeRule.onNodeWithText("Details").performClick()
        composeRule.onNodeWithText("Technical detail text.").assertIsDisplayed()

        composeRule.onNodeWithText("Hide details").performClick()
        composeRule.onNodeWithText("Technical detail text.").assertDoesNotExist()
    }
}
