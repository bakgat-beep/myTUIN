package com.mytuin.gardenplanner.ui.garden

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.ui.theme.MyTuinTheme
import org.junit.Rule
import org.junit.Test

class GrowingSpaceDetailContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun loading_state_shows_progress_indicator_only() {
        composeRule.setContent {
            MyTuinTheme {
                GrowingSpaceDetailContent(uiState = GrowingSpaceDetailUiState(isLoading = true))
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
                )
            }
        }
        composeRule.onNodeWithText("Dimensions").assertDoesNotExist()
    }

    @Test
    fun geometry_section_shows_polygon_label_when_geometry_present() {
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
                            space = sampleSpace().copy(notes = "South edge of the vegetable garden"),
                        ),
                )
            }
        }
        composeRule.onNodeWithText("Notes").assertIsDisplayed()
        composeRule.onNodeWithText("South edge of the vegetable garden").assertIsDisplayed()
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
