package com.mytuin.gardenplanner.ui.garden

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.ui.theme.MyTuinTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GardenMapEditingTest {
    @get:Rule
    val composeRule: ComposeContentTestRule = createComposeRule()

    private fun sampleShape(): MapShape =
        MapShape(
            id = "gs_1",
            kind = MapShapeKind.GrowingSpace(GrowingSpaceType.RAISED_BED),
            geometry =
                Geometry.Polygon(
                    listOf(
                        Coordinate(0.0, 0.0),
                        Coordinate(3.0, 0.0),
                        Coordinate(3.0, 1.0),
                        Coordinate(0.0, 1.0),
                        Coordinate(0.0, 0.0),
                    ),
                ),
        )

    @Test
    fun map_with_selection_renders_without_crashing() {
        val shapes = listOf(sampleShape())
        val viewport = MapViewport.union(shapes)
        composeRule.setContent {
            MyTuinTheme {
                GardenMapCanvas(
                    uiState =
                        GardenMapUiState.Ready(
                            gardenName = "Test Garden",
                            origin = Wgs84(latitude = -43.5321, longitude = 172.6362),
                            shapes = shapes,
                            viewport = viewport,
                        ),
                    editState = GardenMapEditUiState(selectedShapeId = "gs_1"),
                    onSelectShape = {},
                    onClearSelection = {},
                    onSaveGeometry = { _, _ -> },
                    onArchiveSelected = {},
                    onUndoSelected = {},
                )
            }
        }
        composeRule
            .onNodeWithText("Delete", substring = false)
            .assertExists()
        composeRule
            .onNodeWithText("Select", substring = false)
            .assertExists()
        composeRule
            .onNodeWithText("Draw", substring = false)
            .assertExists()
    }

    @Test
    fun tapping_draw_chip_enters_draw_mode() {
        val shapes = listOf(sampleShape())
        val viewport = MapViewport.union(shapes)
        composeRule.setContent {
            MyTuinTheme {
                GardenMapCanvas(
                    uiState =
                        GardenMapUiState.Ready(
                            gardenName = "Test Garden",
                            origin = Wgs84(latitude = -43.5321, longitude = 172.6362),
                            shapes = shapes,
                            viewport = viewport,
                        ),
                    editState = GardenMapEditUiState(selectedShapeId = "gs_1"),
                    onSelectShape = {},
                    onClearSelection = {},
                    onSaveGeometry = { _, _ -> },
                    onArchiveSelected = {},
                    onUndoSelected = {},
                )
            }
        }
        composeRule.onNodeWithText("Draw").performClick()
        composeRule
            .onNodeWithText("Save outline", substring = false)
            .assertExists()
        composeRule
            .onNodeWithText("Clear", substring = false)
            .assertExists()
        composeRule
            .onNodeWithText("Cancel", substring = false)
            .assertExists()
    }
}
