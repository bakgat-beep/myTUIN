package com.mytuin.gardenplanner.ui.garden

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
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
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class EditSpatialObjectContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun renders_prepopulated_fields() {
        composeRule.setContent {
            MyTuinTheme {
                EditSpatialObjectContent(
                    spatialObject = sampleObject(),
                    areas = emptyList(),
                    isSaving = false,
                    saveError = false,
                    onSave = {},
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("Back fence").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Fence").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun current_geometry_line_shows_geometry_type() {
        composeRule.setContent {
            MyTuinTheme {
                EditSpatialObjectContent(
                    spatialObject = sampleObject(),
                    areas = emptyList(),
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
    fun edit_form_does_not_show_rectangle_fields() {
        composeRule.setContent {
            MyTuinTheme {
                EditSpatialObjectContent(
                    spatialObject = sampleObject(),
                    areas = emptyList(),
                    isSaving = false,
                    saveError = false,
                    onSave = {},
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("X1 (m)").assertDoesNotExist()
        composeRule.onNodeWithText("Y1 (m)").assertDoesNotExist()
        composeRule.onNodeWithText("X2 (m)").assertDoesNotExist()
        composeRule.onNodeWithText("Y2 (m)").assertDoesNotExist()
    }

    @Test
    fun save_with_no_edits_emits_current_fields() {
        var captured: SpatialObjectFormData? = null
        composeRule.setContent {
            MyTuinTheme {
                EditSpatialObjectContent(
                    spatialObject = sampleObject(),
                    areas = emptyList(),
                    isSaving = false,
                    saveError = false,
                    onSave = { captured = it },
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("Save").performScrollTo().performClick()
        assertNotNull(captured)
        val edit = captured as SpatialObjectFormData.ForEdit
        assertEquals(InfrastructureType.FENCE, edit.fields.objectType)
        assertEquals("Back fence", edit.fields.name)
    }

    @Test
    fun save_with_blank_name_emits_null_name() {
        var captured: SpatialObjectFormData? = null
        composeRule.setContent {
            MyTuinTheme {
                EditSpatialObjectContent(
                    spatialObject = sampleObject().copy(name = ""),
                    areas = emptyList(),
                    isSaving = false,
                    saveError = false,
                    onSave = { captured = it },
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("Save").performScrollTo().performClick()
        assertNotNull(captured)
        assertNull((captured as SpatialObjectFormData.ForEdit).fields.name)
    }

    @Test
    fun save_error_shown_when_present() {
        composeRule.setContent {
            MyTuinTheme {
                EditSpatialObjectContent(
                    spatialObject = sampleObject(),
                    areas = emptyList(),
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
                EditSpatialObjectContent(
                    spatialObject = sampleObject(),
                    areas = emptyList(),
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
