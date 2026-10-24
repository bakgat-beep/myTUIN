package com.mytuin.gardenplanner.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.ui.theme.MyTuinTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

/**
 * Compose tests for HomeContent (stateless).
 *
 * D9a-2: stateless target, no ViewModel or Hilt dependency.
 *
 * Accessibility baseline (Step 9a requirement): CTA button meets 48dp
 * minimum touch target.
 */
class HomeContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun no_garden_state_shows_title_description_and_cta() {
        composeRule.setContent {
            MyTuinTheme {
                HomeContent(
                    uiState = HomeUiState.NoGarden,
                    onNavigateToCreateGarden = {},
                )
            }
        }

        composeRule.onNodeWithText("Welcome to myTUIN").assertIsDisplayed()
        composeRule
            .onNodeWithText(
                "Create your first garden to start tracking your plants, activities, and harvests.",
            ).assertIsDisplayed()
        composeRule.onNodeWithText("Create your first garden").assertIsDisplayed()
    }

    @Test
    fun no_garden_cta_invokes_navigation_callback() {
        var clicks = 0
        composeRule.setContent {
            MyTuinTheme {
                HomeContent(
                    uiState = HomeUiState.NoGarden,
                    onNavigateToCreateGarden = { clicks += 1 },
                )
            }
        }

        composeRule.onNodeWithText("Create your first garden").performClick()
        assertEquals(1, clicks)
    }

    @Test
    fun no_garden_cta_meets_minimum_touch_target() {
        composeRule.setContent {
            MyTuinTheme {
                HomeContent(
                    uiState = HomeUiState.NoGarden,
                    onNavigateToCreateGarden = {},
                )
            }
        }

        composeRule
            .onNodeWithText("Create your first garden")
            .assertHeightIsAtLeast(48.dp)
    }

    @Test
    fun ready_state_shows_garden_name_and_status() {
        composeRule.setContent {
            MyTuinTheme {
                HomeContent(
                    uiState = HomeUiState.Ready(sampleGarden()),
                    onNavigateToCreateGarden = {},
                )
            }
        }

        composeRule.onNodeWithText("Back Garden").assertIsDisplayed()
        composeRule.onNodeWithText("Draft").assertIsDisplayed()
        composeRule.onNodeWithText("Main vegetable plot").assertIsDisplayed()
    }

    @Test
    fun ready_state_shows_all_status_labels() {
        val currentGarden = mutableStateOf(sampleGarden())
        composeRule.setContent {
            MyTuinTheme {
                HomeContent(
                    uiState = HomeUiState.Ready(currentGarden.value),
                    onNavigateToCreateGarden = {},
                )
            }
        }

        listOf(
            RecordStatus.DRAFT to "Draft",
            RecordStatus.ACTIVE to "Active",
            RecordStatus.INACTIVE to "Inactive",
            RecordStatus.DEPRECATED to "Deprecated",
            RecordStatus.ARCHIVED to "Archived",
        ).forEach { (status, label) ->
            composeRule.runOnIdle {
                currentGarden.value = sampleGarden().copy(status = status)
            }
            composeRule.onNodeWithText(label).assertIsDisplayed()
        }
    }

    @Test
    fun ready_state_hides_description_when_blank() {
        composeRule.setContent {
            MyTuinTheme {
                HomeContent(
                    uiState = HomeUiState.Ready(sampleGarden().copy(description = null)),
                    onNavigateToCreateGarden = {},
                )
            }
        }

        composeRule.onNodeWithText("Back Garden").assertIsDisplayed()
        // No description node
        composeRule.onNodeWithText("Main vegetable plot").assertDoesNotExist()
    }

    @Test
    fun error_state_shows_message() {
        composeRule.setContent {
            MyTuinTheme {
                HomeContent(
                    uiState = HomeUiState.Error,
                    onNavigateToCreateGarden = {},
                )
            }
        }

        composeRule.onNodeWithText("We couldn't load your garden.").assertIsDisplayed()
    }

    private fun sampleGarden(): Garden =
        Garden(
            id = "garden_test_0001",
            name = "Back Garden",
            description = "Main vegetable plot",
            countryCode = null,
            region = null,
            locality = null,
            latitude = null,
            longitude = null,
            timezone = null,
            hemisphere = Hemisphere.UNKNOWN,
            status = RecordStatus.DRAFT,
            createdAt = 1_700_000_000_000L,
            updatedAt = 1_700_000_000_000L,
        )
}
