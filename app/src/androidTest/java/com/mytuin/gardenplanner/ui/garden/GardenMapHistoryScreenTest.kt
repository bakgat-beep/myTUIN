package com.mytuin.gardenplanner.ui.garden

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.SpatialObjectHistory
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.ui.theme.MyTuinTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GardenMapHistoryScreenTest {
    @get:Rule
    val composeRule: ComposeContentTestRule = createComposeRule()

    private fun sampleGeometry() =
        Geometry.Polygon(
            listOf(
                Coordinate(0.0, 0.0),
                Coordinate(1.0, 0.0),
                Coordinate(1.0, 1.0),
                Coordinate(0.0, 1.0),
                Coordinate(0.0, 0.0),
            ),
        )

    private fun readyState(): GardenMapHistoryUiState.Ready {
        val snapshot =
            SpatialObjectHistorySnapshot(
                id = "so_1",
                type = InfrastructureType.FENCE,
                createdAt = 1_000L,
                currentGeometry = sampleGeometry(),
                history =
                    listOf(
                        SpatialObjectHistory(
                            id = "hist_1",
                            spatialObjectId = "so_1",
                            geometry = sampleGeometry(),
                            validFrom = 1_000L,
                            validTo = 5_000L,
                            recordedAt = 5_000L,
                            reason = null,
                        ),
                    ),
            )
        val timeline = GardenHistoryTimeline.build(10_000L, emptyList(), listOf(snapshot))!!
        return GardenMapHistoryUiState.Ready(
            gardenName = "Test Garden",
            origin = Wgs84(latitude = -43.5321, longitude = 172.6362),
            timeline = timeline,
            currentTime = timeline.maxTime,
            shapes = timeline.shapesAt(timeline.maxTime),
            viewport = timeline.viewport,
        )
    }

    @Test
    fun no_garden_state_shows_description() {
        composeRule.setContent {
            MyTuinTheme {
                GardenMapHistoryContent(
                    uiState = GardenMapHistoryUiState.NoGarden,
                    onTimeChange = {},
                    onJumpToNow = {},
                )
            }
        }
        composeRule
            .onNodeWithText("Create a garden to see its history.")
            .assertIsDisplayed()
    }

    @Test
    fun no_shapes_state_shows_description() {
        composeRule.setContent {
            MyTuinTheme {
                GardenMapHistoryContent(
                    uiState = GardenMapHistoryUiState.NoShapes,
                    onTimeChange = {},
                    onJumpToNow = {},
                )
            }
        }
        composeRule
            .onNodeWithText("Add a growing space or garden object to see changes over time.")
            .assertIsDisplayed()
    }

    @Test
    fun no_history_state_shows_description() {
        composeRule.setContent {
            MyTuinTheme {
                GardenMapHistoryContent(
                    uiState = GardenMapHistoryUiState.NoHistory,
                    onTimeChange = {},
                    onJumpToNow = {},
                )
            }
        }
        composeRule
            .onNodeWithText("Edit a shape on the map to start recording history.")
            .assertIsDisplayed()
    }

    @Test
    fun ready_state_shows_jump_to_now_button() {
        composeRule.setContent {
            MyTuinTheme {
                GardenMapHistoryContent(
                    uiState = readyState(),
                    onTimeChange = {},
                    onJumpToNow = {},
                )
            }
        }
        composeRule
            .onNodeWithText("Jump to now")
            .assertIsDisplayed()
    }
}
