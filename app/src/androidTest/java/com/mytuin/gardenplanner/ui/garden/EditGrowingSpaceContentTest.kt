package com.mytuin.gardenplanner.ui.garden

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class EditGrowingSpaceContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun renders_prepopulated_fields() {
        composeRule.setContent {
            MyTuinTheme {
                EditGrowingSpaceContent(
                    space = sampleSpace(),
                    isSaving = false,
                    saveError = false,
                    onSave = {},
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("Bed 2").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("3.0").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("1.2").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun current_geometry_line_shows_geometry_type() {
        composeRule.setContent {
            MyTuinTheme {
                EditGrowingSpaceContent(
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
                    isSaving = false,
                    saveError = false,
                    onSave = {},
                    onCancel = {},
                )
            }
        }
        composeRule
            .onNodeWithText("Current geometry: Polygon")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun current_geometry_line_shows_not_set_when_geometry_null() {
        composeRule.setContent {
            MyTuinTheme {
                EditGrowingSpaceContent(
                    space = sampleSpace().copy(geometry = null),
                    isSaving = false,
                    saveError = false,
                    onSave = {},
                    onCancel = {},
                )
            }
        }
        composeRule
            .onNodeWithText("Current geometry: Not set")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun save_without_geometry_change_emits_unchanged() {
        var captured: GrowingSpaceFormData? = null
        composeRule.setContent {
            MyTuinTheme {
                EditGrowingSpaceContent(
                    space = sampleSpace(),
                    isSaving = false,
                    saveError = false,
                    onSave = { captured = it },
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("Save").performScrollTo().performClick()
        assertNotNull(captured)
        assertEquals("Bed 2", captured!!.fields.name)
        assertTrue(captured!!.geometryEdit is GeometryEdit.Unchanged)
    }

    @Test
    fun save_with_blank_name_blocks_save() {
        var captured: GrowingSpaceFormData? = null
        composeRule.setContent {
            MyTuinTheme {
                EditGrowingSpaceContent(
                    space = sampleSpace().copy(name = ""),
                    isSaving = false,
                    saveError = false,
                    onSave = { captured = it },
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("Save").performScrollTo().performClick()
        composeRule.onNodeWithText("Enter a name").performScrollTo().assertIsDisplayed()
        assertEquals(null, captured)
    }

    @Test
    fun save_error_shown_when_present() {
        composeRule.setContent {
            MyTuinTheme {
                EditGrowingSpaceContent(
                    space = sampleSpace(),
                    isSaving = false,
                    saveError = true,
                    onSave = {},
                    onCancel = {},
                )
            }
        }
        composeRule
            .onNodeWithText("We couldn't save that change. Try again.")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun cancel_invokes_callback() {
        var cancels = 0
        composeRule.setContent {
            MyTuinTheme {
                EditGrowingSpaceContent(
                    space = sampleSpace(),
                    isSaving = false,
                    saveError = false,
                    onSave = {},
                    onCancel = { cancels += 1 },
                )
            }
        }
        composeRule.onNodeWithText("Cancel").performScrollTo().performClick()
        assertEquals(1, cancels)
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
