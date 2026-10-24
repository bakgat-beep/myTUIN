package com.mytuin.gardenplanner.ui.garden

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.mytuin.gardenplanner.ui.theme.MyTuinTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Compose tests for CreateGardenContent (stateless).
 *
 * D9a-2: stateless target, no ViewModel or Hilt dependency.
 */
class CreateGardenContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun initial_state_renders_fields_and_buttons() {
        composeRule.setContent {
            MyTuinTheme {
                CreateGardenContent(
                    uiState = CreateGardenUiState(),
                    onNameChange = {},
                    onDescriptionChange = {},
                    onSave = {},
                    onCancel = {},
                )
            }
        }

        composeRule.onNodeWithText("Create garden").assertIsDisplayed()
        composeRule.onNodeWithText("Garden name").assertIsDisplayed()
        composeRule.onNodeWithText("Description (optional)").assertIsDisplayed()
        composeRule.onNodeWithText("Save").assertIsDisplayed()
        composeRule.onNodeWithText("Cancel").assertIsDisplayed()
    }

    @Test
    fun typing_in_name_field_invokes_onNameChange() {
        var captured = ""
        composeRule.setContent {
            MyTuinTheme {
                CreateGardenContent(
                    uiState = CreateGardenUiState(),
                    onNameChange = { captured = it },
                    onDescriptionChange = {},
                    onSave = {},
                    onCancel = {},
                )
            }
        }

        composeRule.onNodeWithText("Garden name").performTextInput("My Garden")
        assertEquals("My Garden", captured)
    }

    @Test
    fun name_error_is_rendered_when_present() {
        composeRule.setContent {
            MyTuinTheme {
                CreateGardenContent(
                    uiState = CreateGardenUiState(nameError = CreateGardenUiState.NameError.BLANK),
                    onNameChange = {},
                    onDescriptionChange = {},
                    onSave = {},
                    onCancel = {},
                )
            }
        }

        composeRule.onNodeWithText("Enter a garden name").assertIsDisplayed()
    }

    @Test
    fun save_error_is_rendered_when_present() {
        composeRule.setContent {
            MyTuinTheme {
                CreateGardenContent(
                    uiState = CreateGardenUiState(saveError = CreateGardenUiState.SaveError.GENERIC),
                    onNameChange = {},
                    onDescriptionChange = {},
                    onSave = {},
                    onCancel = {},
                )
            }
        }

        composeRule.onNodeWithText("We couldn't save that change. Try again.").assertIsDisplayed()
    }

    @Test
    fun save_button_invokes_onSave() {
        var saves = 0
        composeRule.setContent {
            MyTuinTheme {
                CreateGardenContent(
                    uiState = CreateGardenUiState(name = "Garden"),
                    onNameChange = {},
                    onDescriptionChange = {},
                    onSave = { saves += 1 },
                    onCancel = {},
                )
            }
        }

        composeRule.onNodeWithText("Save").performClick()
        assertEquals(1, saves)
    }

    @Test
    fun cancel_button_invokes_onCancel() {
        var cancels = 0
        composeRule.setContent {
            MyTuinTheme {
                CreateGardenContent(
                    uiState = CreateGardenUiState(),
                    onNameChange = {},
                    onDescriptionChange = {},
                    onSave = {},
                    onCancel = { cancels += 1 },
                )
            }
        }

        composeRule.onNodeWithText("Cancel").performClick()
        assertEquals(1, cancels)
    }

    @Test
    fun buttons_disabled_while_saving() {
        composeRule.setContent {
            MyTuinTheme {
                CreateGardenContent(
                    uiState = CreateGardenUiState(name = "Garden", isSaving = true),
                    onNameChange = {},
                    onDescriptionChange = {},
                    onSave = {},
                    onCancel = {},
                )
            }
        }

        // Cancel and Save both bind enabled = !isSaving. Save's content
        // is a spinner while saving, so it has no "Save" text to select;
        // Cancel is the representative check.
        composeRule.onNodeWithText("Cancel").assertIsDisplayed().assertIsNotEnabled()
    }
}
