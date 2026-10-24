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
import com.mytuin.gardenplanner.ui.home.HomeContent
import com.mytuin.gardenplanner.ui.home.HomeUiState
import com.mytuin.gardenplanner.ui.navigation.MyTuinNavHost
import com.mytuin.gardenplanner.ui.theme.MyTuinTheme
import org.junit.Rule
import org.junit.Test

class ShellAccessibilityTest {
    @get:Rule
    val composeRule = createComposeRule()

    private fun setShell() {
        composeRule.setContent {
            MyTuinTheme {
                MyTuinNavHost(
                    homeScreenContent = { _ ->
                        HomeContent(
                            uiState = HomeUiState.NoGarden,
                            onNavigateToCreateGarden = {},
                        )
                    },
                )
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
    fun home_screen_renders_the_no_garden_empty_state() {
        setShell()
        composeRule.onNodeWithText("Welcome to myTUIN").assertIsDisplayed()
    }
}
