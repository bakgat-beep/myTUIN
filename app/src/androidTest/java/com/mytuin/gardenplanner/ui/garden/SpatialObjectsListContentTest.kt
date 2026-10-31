package com.mytuin.gardenplanner.ui.garden

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.ui.theme.MyTuinTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SpatialObjectsListContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun ready_state_with_empty_list_shows_empty_state_with_default_description() {
        composeRule.setContent {
            MyTuinTheme {
                SpatialObjectsListContent(
                    uiState =
                        SpatialObjectsListUiState.Ready(
                            objects = emptyList(),
                            showArchived = false,
                        ),
                    onCreateObject = {},
                    onObjectClick = {},
                )
            }
        }
        composeRule.onNodeWithText("No garden objects yet").assertIsDisplayed()
        composeRule.onNodeWithText("Objects you create will appear here.").assertIsDisplayed()
        composeRule.onNodeWithText("Add your first garden object").assertIsDisplayed()
    }

    @Test
    fun ready_state_with_empty_list_and_show_archived_shows_alternative_description() {
        composeRule.setContent {
            MyTuinTheme {
                SpatialObjectsListContent(
                    uiState =
                        SpatialObjectsListUiState.Ready(
                            objects = emptyList(),
                            showArchived = true,
                        ),
                    onCreateObject = {},
                    onObjectClick = {},
                )
            }
        }
        composeRule.onNodeWithText("No garden objects yet").assertIsDisplayed()
        composeRule.onNodeWithText("No objects in this garden.").assertIsDisplayed()
    }

    @Test
    fun empty_state_cta_invokes_onCreateObject() {
        var creates = 0
        composeRule.setContent {
            MyTuinTheme {
                SpatialObjectsListContent(
                    uiState =
                        SpatialObjectsListUiState.Ready(
                            objects = emptyList(),
                            showArchived = false,
                        ),
                    onCreateObject = { creates += 1 },
                    onObjectClick = {},
                )
            }
        }
        composeRule.onNodeWithText("Add your first garden object").performClick()
        assertEquals(1, creates)
    }

    @Test
    fun error_state_shows_message() {
        composeRule.setContent {
            MyTuinTheme {
                SpatialObjectsListContent(
                    uiState = SpatialObjectsListUiState.Error,
                    onCreateObject = {},
                    onObjectClick = {},
                )
            }
        }
        composeRule.onNodeWithText("We couldn't load your garden objects.").assertIsDisplayed()
    }

    @Test
    fun ready_state_shows_rows_with_name_and_status() {
        composeRule.setContent {
            MyTuinTheme {
                SpatialObjectsListContent(
                    uiState =
                        SpatialObjectsListUiState.Ready(
                            objects =
                                listOf(
                                    sampleObject(
                                        id = "spatialobject_1",
                                        name = "Back fence",
                                        type = InfrastructureType.FENCE,
                                    ),
                                    sampleObject(
                                        id = "spatialobject_2",
                                        name = "Main path",
                                        type = InfrastructureType.PATH,
                                    ),
                                ),
                            showArchived = false,
                        ),
                    onCreateObject = {},
                    onObjectClick = {},
                )
            }
        }
        composeRule.onNodeWithText("Back fence").assertIsDisplayed()
        composeRule.onNodeWithText("Main path").assertIsDisplayed()
        composeRule.onNodeWithText("Fence", substring = true).assertIsDisplayed()
        composeRule.onNodeWithText("Path", substring = true).assertIsDisplayed()
    }

    @Test
    fun row_without_name_shows_type_as_title() {
        composeRule.setContent {
            MyTuinTheme {
                SpatialObjectsListContent(
                    uiState =
                        SpatialObjectsListUiState.Ready(
                            objects =
                                listOf(
                                    sampleObject(
                                        id = "spatialobject_1",
                                        name = null,
                                        type = InfrastructureType.FENCE,
                                    ),
                                ),
                            showArchived = false,
                        ),
                    onCreateObject = {},
                    onObjectClick = {},
                )
            }
        }
        // Type label appears once as the title, and NOT again as a
        // secondary line (the secondary line is suppressed when name
        // is null).
        composeRule.onAllNodesWithText("Fence").assertCountEquals(1)
    }

    @Test
    fun tapping_row_invokes_onObjectClick_with_id() {
        var capturedId: String? = null
        composeRule.setContent {
            MyTuinTheme {
                SpatialObjectsListContent(
                    uiState =
                        SpatialObjectsListUiState.Ready(
                            objects =
                                listOf(
                                    sampleObject(
                                        id = "spatialobject_1",
                                        name = "Back fence",
                                        type = InfrastructureType.FENCE,
                                    ),
                                ),
                            showArchived = false,
                        ),
                    onCreateObject = {},
                    onObjectClick = { capturedId = it },
                )
            }
        }
        composeRule.onNodeWithText("Back fence").performClick()
        assertEquals("spatialobject_1", capturedId)
    }

    @Test
    fun row_meets_minimum_touch_target() {
        composeRule.setContent {
            MyTuinTheme {
                SpatialObjectsListContent(
                    uiState =
                        SpatialObjectsListUiState.Ready(
                            objects =
                                listOf(
                                    sampleObject(
                                        id = "spatialobject_1",
                                        name = "Back fence",
                                        type = InfrastructureType.FENCE,
                                    ),
                                ),
                            showArchived = false,
                        ),
                    onCreateObject = {},
                    onObjectClick = {},
                )
            }
        }
        composeRule.onNodeWithText("Back fence").assertHeightIsAtLeast(48.dp)
    }

    private fun sampleObject(
        id: String,
        name: String?,
        type: InfrastructureType,
    ): SpatialObject =
        SpatialObject(
            id = id,
            gardenId = "garden_test_0001",
            objectType = type,
            geometry =
                Geometry.Polygon(
                    listOf(
                        Coordinate(0.0, 0.0),
                        Coordinate(1.0, 0.0),
                        Coordinate(1.0, 1.0),
                        Coordinate(0.0, 1.0),
                        Coordinate(0.0, 0.0),
                    ),
                ),
            status = RecordStatus.ACTIVE,
            name = name,
            description = null,
            areaId = null,
            notes = null,
            createdAt = 1_700_000_000_000L,
            updatedAt = 1_700_000_000_000L,
        )
}
