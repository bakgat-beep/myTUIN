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
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.ui.theme.MyTuinTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class GrowingSpaceDetailContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loading_state_shows_progress_indicator_only() {
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpaceDetailContent(
                    uiState = GrowingSpaceDetailUiState(isLoading = true),
                    onEdit = {},
                    onArchive = {},
                    onRestore = {},
                )
            }
        }
        composeRule.onNodeWithText("Dimensions").assertDoesNotExist()
    }

    @Test
    fun error_state_shows_message() {
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpaceDetailContent(
                    uiState = GrowingSpaceDetailUiState(isLoading = false, loadError = true),
                    onEdit = {},
                    onArchive = {},
                    onRestore = {},
                )
            }
        }
        composeRule.onNodeWithText("We couldn't load this growing space.").assertIsDisplayed()
    }

    @Test
    fun loaded_state_shows_type_and_status_line() {
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpaceDetailContent(
                    uiState =
                        GrowingSpaceDetailUiState(
                            isLoading = false,
                            space = sampleSpace(),
                        ),
                    onEdit = {},
                    onArchive = {},
                    onRestore = {},
                )
            }
        }
        composeRule.onNodeWithText("Raised bed · Active").assertIsDisplayed()
    }

    @Test
    fun description_rendered_when_present() {
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpaceDetailContent(
                    uiState =
                        GrowingSpaceDetailUiState(
                            isLoading = false,
                            space = sampleSpace().copy(description = "Main raised bed"),
                        ),
                    onEdit = {},
                    onArchive = {},
                    onRestore = {},
                )
            }
        }
        composeRule.onNodeWithText("Description").assertIsDisplayed()
        composeRule.onNodeWithText("Main raised bed").assertIsDisplayed()
    }

    @Test
    fun description_hidden_when_null() {
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpaceDetailContent(
                    uiState =
                        GrowingSpaceDetailUiState(
                            isLoading = false,
                            space = sampleSpace().copy(description = null),
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
    fun dimensions_section_renders_only_non_null_fields() {
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpaceDetailContent(
                    uiState =
                        GrowingSpaceDetailUiState(
                            isLoading = false,
                            space =
                                sampleSpace().copy(
                                    lengthMetres = 3.0,
                                    widthMetres = 1.2,
                                    heightMetres = null,
                                    diameterMetres = null,
                                    areaSquareMetres = 3.6,
                                    volumeCubicMetres = null,
                                ),
                        ),
                    onEdit = {},
                    onArchive = {},
                    onRestore = {},
                )
            }
        }
        composeRule.onNodeWithText("Dimensions").assertIsDisplayed()
        composeRule.onNodeWithText("Length: 3.0 m").assertIsDisplayed()
        composeRule.onNodeWithText("Width: 1.2 m").assertIsDisplayed()
        composeRule.onNodeWithText("Area: 3.6 m²").assertIsDisplayed()
        composeRule.onNodeWithText("Height", substring = true).assertDoesNotExist()
        composeRule.onNodeWithText("Diameter", substring = true).assertDoesNotExist()
        composeRule.onNodeWithText("Volume", substring = true).assertDoesNotExist()
    }

    @Test
    fun dimensions_section_hidden_when_all_null() {
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpaceDetailContent(
                    uiState =
                        GrowingSpaceDetailUiState(
                            isLoading = false,
                            space =
                                sampleSpace().copy(
                                    lengthMetres = null,
                                    widthMetres = null,
                                    heightMetres = null,
                                    diameterMetres = null,
                                    areaSquareMetres = null,
                                    volumeCubicMetres = null,
                                ),
                        ),
                    onEdit = {},
                    onArchive = {},
                    onRestore = {},
                )
            }
        }
        composeRule.onNodeWithText("Dimensions").assertDoesNotExist()
    }

    @Test
    fun geometry_section_shows_polygon_label() {
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpaceDetailContent(
                    uiState =
                        GrowingSpaceDetailUiState(
                            isLoading = false,
                            space =
                                sampleSpace().copy(
                                    geometry =
                                        Geometry.Polygon(
                                            listOf(
                                                Coordinate(0.0, 0.0),
                                                Coordinate(1.0, 0.0),
                                                Coordinate(1.0, 1.0),
                                                Coordinate(0.0, 0.0),
                                            ),
                                        ),
                                ),
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
    fun geometry_section_shows_not_set_when_null() {
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpaceDetailContent(
                    uiState =
                        GrowingSpaceDetailUiState(
                            isLoading = false,
                            space = sampleSpace().copy(geometry = null),
                        ),
                    onEdit = {},
                    onArchive = {},
                    onRestore = {},
                )
            }
        }
        composeRule.onNodeWithText("Not set").assertIsDisplayed()
    }

    @Test
    fun notes_rendered_when_present() {
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpaceDetailContent(
                    uiState =
                        GrowingSpaceDetailUiState(
                            isLoading = false,
                            space = sampleSpace().copy(notes = "South edge"),
                        ),
                    onEdit = {},
                    onArchive = {},
                    onRestore = {},
                )
            }
        }
        composeRule.onNodeWithText("Notes").assertIsDisplayed()
        composeRule.onNodeWithText("South edge").assertIsDisplayed()
    }

    @Test
    fun edit_button_invokes_callback() {
        var edits = 0
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpaceDetailContent(
                    uiState = GrowingSpaceDetailUiState(isLoading = false, space = sampleSpace()),
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
    fun active_space_shows_archive_button_and_hides_restore() {
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpaceDetailContent(
                    uiState =
                        GrowingSpaceDetailUiState(
                            isLoading = false,
                            space = sampleSpace().copy(status = RecordStatus.ACTIVE),
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
    fun archived_space_shows_restore_button_and_hides_archive() {
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpaceDetailContent(
                    uiState =
                        GrowingSpaceDetailUiState(
                            isLoading = false,
                            space = sampleSpace().copy(status = RecordStatus.ARCHIVED),
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
                GrowingSpaceDetailContent(
                    uiState = GrowingSpaceDetailUiState(isLoading = false, space = sampleSpace()),
                    onEdit = {},
                    onArchive = {},
                    onRestore = {},
                )
            }
        }
        composeRule.onNodeWithText("Archive").performScrollTo().performClick()
        composeRule.onNodeWithText("Archive this growing space?").assertIsDisplayed()
        composeRule
            .onNodeWithText("This space will be hidden from the list. Historical records remain.")
            .assertIsDisplayed()
    }

    @Test
    fun confirming_archive_dialog_invokes_onArchive() {
        var archives = 0
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpaceDetailContent(
                    uiState = GrowingSpaceDetailUiState(isLoading = false, space = sampleSpace()),
                    onEdit = {},
                    onArchive = { archives += 1 },
                    onRestore = {},
                )
            }
        }
        // Open the dialog.
        composeRule.onNodeWithText("Archive").performScrollTo().performClick()
        composeRule.onNodeWithText("Archive this growing space?").assertIsDisplayed()

        // The dialog's confirm button is also labeled "Archive". After
        // opening the dialog there are two nodes with "Archive": the
        // screen button and the dialog's confirm. OnLast targets the
        // dialog's.
        composeRule.onAllNodesWithText("Archive").onLast().performClick()
        assertEquals(1, archives)
    }

    @Test
    fun cancelling_archive_dialog_does_not_invoke_onArchive() {
        var archives = 0
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpaceDetailContent(
                    uiState = GrowingSpaceDetailUiState(isLoading = false, space = sampleSpace()),
                    onEdit = {},
                    onArchive = { archives += 1 },
                    onRestore = {},
                )
            }
        }
        composeRule.onNodeWithText("Archive").performScrollTo().performClick()
        composeRule.onNodeWithText("Cancel").performClick()
        assertEquals(0, archives)
        // Dialog is gone.
        composeRule.onNodeWithText("Archive this growing space?").assertDoesNotExist()
    }

    @Test
    fun tapping_restore_invokes_onRestore_directly() {
        var restores = 0
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpaceDetailContent(
                    uiState =
                        GrowingSpaceDetailUiState(
                            isLoading = false,
                            space = sampleSpace().copy(status = RecordStatus.ARCHIVED),
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

    private fun sampleSpace(): GrowingSpace =
        GrowingSpace(
            id = "growingspace_test_0001",
            gardenId = "garden_test_0001",
            name = "Bed 2",
            spaceType = GrowingSpaceType.RAISED_BED,
            status = RecordStatus.ACTIVE,
            geometry = null,
            lengthMetres = 3.0,
            widthMetres = 1.2,
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
