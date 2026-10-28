package com.mytuin.gardenplanner.ui.garden

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.ui.theme.MyTuinTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class GardenDetailContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loading_state_shows_progress_indicator_only() {
        composeRule.setContent {
            MyTuinTheme {
                GardenDetailContent(
                    uiState = GardenDetailUiState(isLoading = true),
                    onEditLocation = {},
                    onNavigateToGrowingSpaces = {},
                    onNavigateToSpatialObjects = {},
                )
            }
        }
        composeRule.onNodeWithText("Country").assertDoesNotExist()
    }

    @Test
    fun error_state_shows_message() {
        composeRule.setContent {
            MyTuinTheme {
                GardenDetailContent(
                    uiState = GardenDetailUiState(isLoading = false, loadError = true),
                    onEditLocation = {},
                    onNavigateToGrowingSpaces = {},
                    onNavigateToSpatialObjects = {},
                )
            }
        }
        composeRule.onNodeWithText("We couldn't load this garden.").assertIsDisplayed()
    }

    @Test
    fun loaded_state_shows_all_field_labels() {
        composeRule.setContent {
            MyTuinTheme {
                GardenDetailContent(
                    uiState =
                        GardenDetailUiState(
                            isLoading = false,
                            garden = sampleGarden(),
                        ),
                    onEditLocation = {},
                    onNavigateToGrowingSpaces = {},
                    onNavigateToSpatialObjects = {},
                )
            }
        }
        composeRule.onNodeWithText("Location").assertIsDisplayed()
        composeRule.onNodeWithText("Country").assertIsDisplayed()
        composeRule.onNodeWithText("Region").assertIsDisplayed()
        composeRule.onNodeWithText("Locality").assertIsDisplayed()
        composeRule.onNodeWithText("Latitude").assertIsDisplayed()
        composeRule.onNodeWithText("Longitude").assertIsDisplayed()
        composeRule.onNodeWithText("Timezone").assertIsDisplayed()
        composeRule.onNodeWithText("Hemisphere").assertIsDisplayed()
    }

    @Test
    fun populated_location_fields_render_their_values() {
        composeRule.setContent {
            MyTuinTheme {
                GardenDetailContent(
                    uiState = GardenDetailUiState(isLoading = false, garden = sampleGarden()),
                    onEditLocation = {},
                    onNavigateToGrowingSpaces = {},
                    onNavigateToSpatialObjects = {},
                )
            }
        }
        composeRule.onNodeWithText("NZ").assertIsDisplayed()
        composeRule.onNodeWithText("Canterbury").assertIsDisplayed()
        composeRule.onNodeWithText("Christchurch").assertIsDisplayed()
        composeRule.onNodeWithText("Pacific/Auckland").assertIsDisplayed()
        composeRule.onNodeWithText("Southern").assertIsDisplayed()
    }

    @Test
    fun edit_button_invokes_callback() {
        var edits = 0
        composeRule.setContent {
            MyTuinTheme {
                GardenDetailContent(
                    uiState = GardenDetailUiState(isLoading = false, garden = sampleGarden()),
                    onEditLocation = { edits += 1 },
                    onNavigateToGrowingSpaces = {},
                    onNavigateToSpatialObjects = {},
                )
            }
        }
        composeRule.onNodeWithText("Edit location").performScrollTo().performClick()
        assertEquals(1, edits)
    }

    @Test
    fun view_growing_spaces_button_invokes_callback() {
        var navigations = 0
        composeRule.setContent {
            MyTuinTheme {
                GardenDetailContent(
                    uiState = GardenDetailUiState(isLoading = false, garden = sampleGarden()),
                    onEditLocation = {},
                    onNavigateToGrowingSpaces = { navigations += 1 },
                    onNavigateToSpatialObjects = {},
                )
            }
        }
        composeRule
            .onNodeWithText("View growing spaces")
            .performScrollTo()
            .performClick()
        assertEquals(1, navigations)
    }

    private fun sampleGarden(): Garden =
        Garden(
            id = "garden_test_0001",
            name = "Back Garden",
            description = null,
            countryCode = "NZ",
            region = "Canterbury",
            locality = "Christchurch",
            latitude = -43.5321,
            longitude = 172.6362,
            timezone = "Pacific/Auckland",
            hemisphere = Hemisphere.SOUTHERN,
            status = RecordStatus.DRAFT,
            createdAt = 1_700_000_000_000L,
            updatedAt = 1_700_000_000_000L,
        )
}
