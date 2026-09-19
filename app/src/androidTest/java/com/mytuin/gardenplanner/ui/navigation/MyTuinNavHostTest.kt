package com.mytuin.gardenplanner.ui.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import com.mytuin.gardenplanner.ui.theme.MyTuinTheme
import org.junit.Rule
import org.junit.Test

class MyTuinNavHostTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shell_displays_all_five_navigation_destinations() {
        composeRule.setContent {
            MyTuinTheme { MyTuinNavHost() }
        }
        listOf("Home", "Garden", "Plants", "Planner", "Inbox").forEach { label ->
            composeRule.onAllNodesWithText(label).onFirst().assertIsDisplayed()
        }
    }

    @Test
    fun add_fab_opens_sheet_with_seven_primary_options() {
        composeRule.setContent {
            MyTuinTheme { MyTuinNavHost() }
        }
        composeRule.onNodeWithContentDescription("Add").performClick()
        listOf("Plant", "Water", "Harvest", "Observe", "Feed", "Prune", "Intervention")
            .forEach { option ->
                composeRule.onAllNodesWithText(option).onFirst().assertIsDisplayed()
            }
    }
}
