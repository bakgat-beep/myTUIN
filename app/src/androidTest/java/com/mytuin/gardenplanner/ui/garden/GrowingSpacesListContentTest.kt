package com.mytuin.gardenplanner.ui.garden

import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.ui.theme.MyTuinTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class GrowingSpacesListContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun no_spaces_state_shows_title_description_and_cta() {
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpacesListContent(
                    uiState = GrowingSpacesListUiState.NoSpaces,
                    onCreateSpace = {},
                    onSpaceClick = {},
                )
            }
        }
        composeRule.onNodeWithText("No growing spaces yet").assertIsDisplayed()
        composeRule
            .onNodeWithText("Growing spaces you create will appear here.")
            .assertIsDisplayed()
        composeRule.onNodeWithText("Add your first growing space").assertIsDisplayed()
    }

    @Test
    fun empty_state_cta_invokes_onCreateSpace() {
        var creates = 0
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpacesListContent(
                    uiState = GrowingSpacesListUiState.NoSpaces,
                    onCreateSpace = { creates += 1 },
                    onSpaceClick = {},
                )
            }
        }
        composeRule.onNodeWithText("Add your first growing space").performClick()
        assertEquals(1, creates)
    }

    @Test
    fun error_state_shows_message() {
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpacesListContent(
                    uiState = GrowingSpacesListUiState.Error,
                    onCreateSpace = {},
                    onSpaceClick = {},
                )
            }
        }
        composeRule.onNodeWithText("We couldn't load your growing spaces.").assertIsDisplayed()
    }

    @Test
    fun ready_state_shows_rows_with_name_type_and_status() {
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpacesListContent(
                    uiState =
                        GrowingSpacesListUiState.Ready(
                            listOf(
                                sampleSpace("growingspace_1", "Bed 1", GrowingSpaceType.RAISED_BED),
                                sampleSpace("growingspace_2", "Bed 2", GrowingSpaceType.POT),
                            ),
                        ),
                    onCreateSpace = {},
                    onSpaceClick = {},
                )
            }
        }
        composeRule.onNodeWithText("Bed 1").assertIsDisplayed()
        composeRule.onNodeWithText("Bed 2").assertIsDisplayed()
        composeRule.onNodeWithText("Raised bed", substring = true).assertIsDisplayed()
        composeRule.onNodeWithText("Pot", substring = true).assertIsDisplayed()
    }

    @Test
    fun ready_state_row_includes_dimension_summary_when_present() {
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpacesListContent(
                    uiState =
                        GrowingSpacesListUiState.Ready(
                            listOf(
                                sampleSpace(
                                    "growingspace_1",
                                    "Bed 1",
                                    GrowingSpaceType.RAISED_BED,
                                    length = 3.0,
                                    width = 1.2,
                                ),
                            ),
                        ),
                    onCreateSpace = {},
                    onSpaceClick = {},
                )
            }
        }
        composeRule.onNodeWithText("3.0 × 1.2 m", substring = true).assertIsDisplayed()
    }

    @Test
    fun tapping_row_invokes_onSpaceClick_with_id() {
        var capturedId: String? = null
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpacesListContent(
                    uiState =
                        GrowingSpacesListUiState.Ready(
                            listOf(sampleSpace("growingspace_1", "Bed 1", GrowingSpaceType.RAISED_BED)),
                        ),
                    onCreateSpace = {},
                    onSpaceClick = { capturedId = it },
                )
            }
        }
        composeRule.onNodeWithText("Bed 1").performClick()
        assertEquals("growingspace_1", capturedId)
    }

    @Test
    fun row_meets_minimum_touch_target() {
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpacesListContent(
                    uiState =
                        GrowingSpacesListUiState.Ready(
                            listOf(sampleSpace("growingspace_1", "Bed 1", GrowingSpaceType.RAISED_BED)),
                        ),
                    onCreateSpace = {},
                    onSpaceClick = {},
                )
            }
        }
        composeRule
            .onNodeWithText("Bed 1")
            .assertHeightIsAtLeast(48.dp)
    }

    private fun sampleSpace(
        id: String,
        name: String,
        type: GrowingSpaceType,
        length: Double? = null,
        width: Double? = null,
    ): GrowingSpace =
        GrowingSpace(
            id = id,
            gardenId = "garden_test_0001",
            name = name,
            spaceType = type,
            status = RecordStatus.ACTIVE,
            geometry = null,
            lengthMetres = length,
            widthMetres = width,
            heightMetres = null,
            diameterMetres = null,
            areaSquareMetres = null,
            volumeCubicMetres = null,
            description = null,
            areaId = null,
            notes = null,
            createdAt = 1_700_000_000_000L,
            updatedAt = 1_700_000_000_000L,
        )
}
