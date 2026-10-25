package com.mytuin.gardenplanner.ui.garden

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.mytuin.gardenplanner.domain.model.garden.NewGardenLocation
import com.mytuin.gardenplanner.domain.model.location.Country
import com.mytuin.gardenplanner.ui.theme.MyTuinTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

class EditGardenLocationContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    private val countries =
        listOf(
            Country("AU", "Australia"),
            Country("NZ", "New Zealand"),
            Country("GB", "United Kingdom"),
        )

    @Test
    fun renders_all_field_labels() {
        composeRule.setContent {
            MyTuinTheme {
                EditGardenLocationContent(
                    initialLocation = NewGardenLocation(),
                    countries = countries,
                    isSaving = false,
                    saveError = false,
                    onSave = {},
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("Edit location").assertIsDisplayed()
        composeRule.onNodeWithText("Country").assertIsDisplayed()
        composeRule.onNodeWithText("Region").assertIsDisplayed()
        composeRule.onNodeWithText("Locality").assertIsDisplayed()
        composeRule.onNodeWithText("Latitude").assertIsDisplayed()
    }

    @Test
    fun blank_form_saves_successfully_with_empty_location() {
        var saved: NewGardenLocation? = null
        composeRule.setContent {
            MyTuinTheme {
                EditGardenLocationContent(
                    initialLocation = NewGardenLocation(),
                    countries = countries,
                    isSaving = false,
                    saveError = false,
                    onSave = { saved = it },
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("Save").performScrollTo().performClick()
        assertNotNull(saved)
        assertEquals(null, saved!!.countryCode)
        assertEquals(null, saved!!.latitude)
    }

    @Test
    fun invalid_latitude_shows_error_and_does_not_save() {
        var saved: NewGardenLocation? = null
        composeRule.setContent {
            MyTuinTheme {
                EditGardenLocationContent(
                    initialLocation = NewGardenLocation(latitude = 100.0),
                    countries = countries,
                    isSaving = false,
                    saveError = false,
                    onSave = { saved = it },
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("Save").performScrollTo().performClick()
        composeRule
            .onNodeWithText("Latitude must be between -90 and 90")
            .performScrollTo()
            .assertIsDisplayed()
        assertEquals(null, saved)
    }

    @Test
    fun invalid_longitude_shows_error_and_does_not_save() {
        var saved: NewGardenLocation? = null
        composeRule.setContent {
            MyTuinTheme {
                EditGardenLocationContent(
                    initialLocation = NewGardenLocation(longitude = 200.0),
                    countries = countries,
                    isSaving = false,
                    saveError = false,
                    onSave = { saved = it },
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("Save").performScrollTo().performClick()
        composeRule
            .onNodeWithText("Longitude must be between -180 and 180")
            .performScrollTo()
            .assertIsDisplayed()
        assertEquals(null, saved)
    }

    @Test
    fun cancel_invokes_callback() {
        var cancels = 0
        composeRule.setContent {
            MyTuinTheme {
                EditGardenLocationContent(
                    initialLocation = NewGardenLocation(),
                    countries = countries,
                    isSaving = false,
                    saveError = false,
                    onSave = {},
                    onCancel = { cancels += 1 },
                )
            }
        }
        composeRule.onNodeWithText("Cancel").performScrollTo().performClick()
        assertEquals(1, cancels)
    }

    @Test
    fun pick_country_button_opens_dialog() {
        composeRule.setContent {
            MyTuinTheme {
                EditGardenLocationContent(
                    initialLocation = NewGardenLocation(),
                    countries = countries,
                    isSaving = false,
                    saveError = false,
                    onSave = {},
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("Pick country").performScrollTo().performClick()
        composeRule.onNodeWithText("Select country").assertIsDisplayed()
    }

    @Test
    fun selecting_country_in_picker_sets_country_code() {
        var saved: NewGardenLocation? = null
        composeRule.setContent {
            MyTuinTheme {
                EditGardenLocationContent(
                    initialLocation = NewGardenLocation(),
                    countries = countries,
                    isSaving = false,
                    saveError = false,
                    onSave = { saved = it },
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("Pick country").performScrollTo().performClick()
        composeRule.onNodeWithText("Australia (AU)").performClick()
        composeRule.onNodeWithText("Save").performScrollTo().performClick()
        assertEquals("AU", saved?.countryCode)
    }

    @Test
    fun save_error_shown_when_present() {
        composeRule.setContent {
            MyTuinTheme {
                EditGardenLocationContent(
                    initialLocation = NewGardenLocation(),
                    countries = countries,
                    isSaving = false,
                    saveError = true,
                    onSave = {},
                    onCancel = {},
                )
            }
        }
        composeRule
            .onNodeWithText("We couldn't save that change. Try again.")
            .performScrollTo()
            .assertIsDisplayed()
    }
}
