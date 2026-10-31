package com.mytuin.gardenplanner.ui.garden

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.ui.theme.MyTuinTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class SpatialObjectDetailContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loading_state_shows_progress_indicator_only() {
        composeRule.setContent {
            MyTuinTheme {
                SpatialObjectDetailContent(
                    uiState = SpatialObjectDetailUiState(isLoading = true),
                    onEdit = {},
                    onArchive = {},
                    onRestore = {},
                )
            }
        }
        composeRule.onNodeWithText("Geometry").assertDoesNotExist()
    }

    @Test
    fun error_state_shows_message() {
        composeRule.setContent {
            MyTuinTheme {
                SpatialObjectDetailContent(
                    uiState = SpatialObjectDetailUiState(isLoading = false, loadError = true),
                    onEdit = {},
                    onArchive = {},
                    onRestore = {},
                )
            }
        }
        composeRule.onNodeWithText("We couldn't load this garden object.").assertIsDisplayed()
    }

    @Test
    fun loaded_state_shows_type_and_status_line() {
        composeRule.setContent {
            MyTuinTheme {
                SpatialObjectDetailContent(
                    uiState =
                        SpatialObjectDetailUiState(
                            isLoading = false,
                            spatialObject = sampleObject(),
                        ),
                    onEdit = {},
                    onArchive = {},
                    onRestore = {},
                )
            }
        }
        composeRule.onNodeWithText("Fence · Active").assertIsDisplayed()
    }

    @Test
    fun description_rendered_when_present() {
        composeRule.setContent {
            MyTuinTheme {
                SpatialObjectDetailContent(
                    uiState =
                        SpatialObjectDetailUiState(
                            isLoading = false,
                            spatialObject = sampleObject().copy(description = "Timber boundary"),
                        ),
                    onEdit = {},
                    onArchive = {},
                    onRestore = {},
                )
            }
        }
        composeRule.onNodeWithText("Description").assertIsDisplayed()
        composeRule.onNodeWithText("Timber boundary").assertIsDisplayed()
    }

    @Test
    fun description_hidden_when_null() {
        composeRule.setContent {
            MyTuinTheme {
                SpatialObjectDetailContent(
                    uiState =
                        SpatialObjectDetailUiState(
                            isLoading = false,
                            spatialObject = sampleObject().copy(description = null),
                        ),
                    onEdit = {},
                    onArchive = {},
                    onRestore = {},
                )
            }
        }
        composeRule.onNodeWithText("Description").assertDoesNotExist()
    }

    @Test
    fun geometry_section_shows_polygon_label() {
        composeRule.setContent {
            MyTuinTheme {
                SpatialObjectDetailContent(
                    uiState =
                        SpatialObjectDetailUiState(
                            isLoading = false,
                            spatialObject = sampleObject(),
                        ),
                    onEdit = {},
                    onArchive = {},
                    onRestore = {},
                )
            }
        }
        composeRule.onNodeWithText("Geometry").assertIsDisplayed()
        composeRule.onNodeWithText("Polygon").assertIsDisplayed()
    }

    @Test
    fun notes_rendered_when_present() {
        composeRule.setContent {
            MyTuinTheme {
                SpatialObjectDetailContent(
                    uiState =
                        SpatialObjectDetailUiState(
                            isLoading = false,
                            spatialObject = sampleObject().copy(notes = "Replace in autumn"),
                        ),
                    onEdit = {},
                    onArchive = {},
                    onRestore = {},
                )
            }
        }
        composeRule.onNodeWithText("Notes").assertIsDisplayed()
        composeRule.onNodeWithText("Replace in autumn").assertIsDisplayed()
    }

    @Test
    fun edit_button_invokes_callback() {
        var edits = 0
        composeRule.setContent {
            MyTuinTheme {
                SpatialObjectDetailContent(
                    uiState =
                        SpatialObjectDetailUiState(
                            isLoading = false,
                            spatialObject = sampleObject(),
                        ),
                    onEdit = { edits += 1 },
                    onArchive = {},
                    onRestore = {},
                )
            }
        }
        composeRule.onNodeWithText("Edit").performScrollTo().performClick()
        assertEquals(1, edits)
    }

    @Test
    fun active_object_shows_archive_button_and_hides_restore() {
        composeRule.setContent {
            MyTuinTheme {
                SpatialObjectDetailContent(
                    uiState =
                        SpatialObjectDetailUiState(
                            isLoading = false,
                            spatialObject = sampleObject().copy(status = RecordStatus.ACTIVE),
                        ),
                    onEdit = {},
                    onArchive = {},
                    onRestore = {},
                )
            }
        }
        composeRule.onNodeWithText("Archive").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Restore").assertDoesNotExist()
    }

    @Test
    fun archived_object_shows_restore_button_and_hides_archive() {
        composeRule.setContent {
            MyTuinTheme {
                SpatialObjectDetailContent(
                    uiState =
                        SpatialObjectDetailUiState(
                            isLoading = false,
                            spatialObject = sampleObject().copy(status = RecordStatus.ARCHIVED),
                        ),
                    onEdit = {},
                    onArchive = {},
                    onRestore = {},
                )
            }
        }
        composeRule.onNodeWithText("Restore").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Archive").assertDoesNotExist()
    }

    @Test
    fun tapping_archive_opens_confirmation_dialog() {
        composeRule.setContent {
            MyTuinTheme {
                SpatialObjectDetailContent(
                    uiState =
                        SpatialObjectDetailUiState(
                            isLoading = false,
                            spatialObject = sampleObject(),
                        ),
                    onEdit = {},
                    onArchive = {},
                    onRestore = {},
                )
            }
        }
        composeRule.onNodeWithText("Archive").performScrollTo().performClick()
        composeRule.onNodeWithText("Archive this garden object?").assertIsDisplayed()
        composeRule
            .onNodeWithText("This object will be hidden from the list. Historical records remain.")
            .assertIsDisplayed()
    }

    @Test
    fun confirming_archive_dialog_invokes_onArchive() {
        var archives = 0
        composeRule.setContent {
            MyTuinTheme {
                SpatialObjectDetailContent(
                    uiState =
                        SpatialObjectDetailUiState(
                            isLoading = false,
                            spatialObject = sampleObject(),
                        ),
                    onEdit = {},
                    onArchive = { archives += 1 },
                    onRestore = {},
                )
            }
        }
        composeRule.onNodeWithText("Archive").performScrollTo().performClick()
        composeRule.onNodeWithText("Archive this garden object?").assertIsDisplayed()
        // Disambiguate the screen button from the dialog's confirm button.
        composeRule.onAllNodesWithText("Archive").onLast().performClick()
        assertEquals(1, archives)
    }

    @Test
    fun cancelling_archive_dialog_does_not_invoke_onArchive() {
        var archives = 0
        composeRule.setContent {
            MyTuinTheme {
                SpatialObjectDetailContent(
                    uiState =
                        SpatialObjectDetailUiState(
                            isLoading = false,
                            spatialObject = sampleObject(),
                        ),
                    onEdit = {},
                    onArchive = { archives += 1 },
                    onRestore = {},
                )
            }
        }
        composeRule.onNodeWithText("Archive").performScrollTo().performClick()
        composeRule.onNodeWithText("Cancel").performClick()
        assertEquals(0, archives)
        composeRule.onNodeWithText("Archive this garden object?").assertDoesNotExist()
    }

    @Test
    fun tapping_restore_invokes_onRestore_directly() {
        var restores = 0
        composeRule.setContent {
            MyTuinTheme {
                SpatialObjectDetailContent(
                    uiState =
                        SpatialObjectDetailUiState(
                            isLoading = false,
                            spatialObject = sampleObject().copy(status = RecordStatus.ARCHIVED),
                        ),
                    onEdit = {},
                    onArchive = {},
                    onRestore = { restores += 1 },
                )
            }
        }
        composeRule.onNodeWithText("Restore").performScrollTo().performClick()
        assertEquals(1, restores)
    }

    private fun sampleObject(): SpatialObject =
        SpatialObject(
            id = "spatialobject_test_0001",
            gardenId = "garden_test_0001",
            objectType = InfrastructureType.FENCE,
            geometry =
                Geometry.Polygon(
                    listOf(
                        Coordinate(0.0, 0.0),
                        Coordinate(3.0, 0.0),
                        Coordinate(3.0, 2.0),
                        Coordinate(0.0, 2.0),
                        Coordinate(0.0, 0.0),
                    ),
                ),
            status = RecordStatus.ACTIVE,
            name = "Back fence",
            description = null,
            areaId = null,
            notes = null,
            createdAt = 1_700_000_000_000L,
            updatedAt = 1_700_000_000_000L,
        )
}
