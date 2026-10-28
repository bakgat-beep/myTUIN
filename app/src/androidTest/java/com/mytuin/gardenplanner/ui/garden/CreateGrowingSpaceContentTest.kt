package com.mytuin.gardenplanner.ui.garden

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.ui.theme.MyTuinTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CreateGrowingSpaceContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun renders_key_form_labels() {
        composeRule.setContent {
            MyTuinTheme {
                CreateGrowingSpaceContent(
                    seed = GrowingSpaceFormSeed(),
                    mode = GrowingSpaceFormMode.CREATE,
                    existingGeometryType = null,
                    isSaving = false,
                    saveError = false,
                    onSave = {},
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("Name").assertIsDisplayed()
        composeRule.onNodeWithText("Dimensions (optional)").assertIsDisplayed()
        composeRule.onNodeWithText("Save").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Cancel").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun blank_name_blocks_save_and_shows_error() {
        var captured: GrowingSpaceFormData? = null
        composeRule.setContent {
            MyTuinTheme {
                CreateGrowingSpaceContent(
                    seed = GrowingSpaceFormSeed(),
                    mode = GrowingSpaceFormMode.CREATE,
                    existingGeometryType = null,
                    isSaving = false,
                    saveError = false,
                    onSave = { captured = it },
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("Save").performScrollTo().performClick()
        composeRule.onNodeWithText("Enter a name").performScrollTo().assertIsDisplayed()
        assertNull(captured)
    }

    @Test
    fun valid_form_invokes_onSave_with_fields() {
        var captured: GrowingSpaceFormData? = null
        composeRule.setContent {
            MyTuinTheme {
                CreateGrowingSpaceContent(
                    seed = GrowingSpaceFormSeed(name = "Bed 1"),
                    mode = GrowingSpaceFormMode.CREATE,
                    existingGeometryType = null,
                    isSaving = false,
                    saveError = false,
                    onSave = { captured = it },
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("Save").performScrollTo().performClick()
        assertNotNull(captured)
        assertEquals("Bed 1", captured!!.fields.name)
        assertTrue(captured!!.geometryEdit is GeometryEdit.Unchanged)
    }

    @Test
    fun negative_length_blocks_save() {
        var captured: GrowingSpaceFormData? = null
        composeRule.setContent {
            MyTuinTheme {
                CreateGrowingSpaceContent(
                    seed = GrowingSpaceFormSeed(name = "Bed 1", lengthText = "-1"),
                    mode = GrowingSpaceFormMode.CREATE,
                    existingGeometryType = null,
                    isSaving = false,
                    saveError = false,
                    onSave = { captured = it },
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("Save").performScrollTo().performClick()
        composeRule
            .onNodeWithText("Enter a positive number", substring = true)
            .performScrollTo()
            .assertIsDisplayed()
        assertNull(captured)
    }

    @Test
    fun zero_width_blocks_save() {
        var captured: GrowingSpaceFormData? = null
        composeRule.setContent {
            MyTuinTheme {
                CreateGrowingSpaceContent(
                    seed = GrowingSpaceFormSeed(name = "Bed 1", widthText = "0"),
                    mode = GrowingSpaceFormMode.CREATE,
                    existingGeometryType = null,
                    isSaving = false,
                    saveError = false,
                    onSave = { captured = it },
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("Save").performScrollTo().performClick()
        composeRule
            .onNodeWithText("Enter a positive number", substring = true)
            .performScrollTo()
            .assertIsDisplayed()
        assertNull(captured)
    }

    @Test
    fun full_rectangle_produces_polygon_geometry() {
        var captured: GrowingSpaceFormData? = null
        composeRule.setContent {
            MyTuinTheme {
                CreateGrowingSpaceContent(
                    seed =
                        GrowingSpaceFormSeed(
                            name = "Bed 1",
                            rectX1 = "0",
                            rectY1 = "0",
                            rectX2 = "3",
                            rectY2 = "2",
                        ),
                    mode = GrowingSpaceFormMode.CREATE,
                    existingGeometryType = null,
                    isSaving = false,
                    saveError = false,
                    onSave = { captured = it },
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("Save").performScrollTo().performClick()
        assertNotNull(captured)
        val edit = captured!!.geometryEdit
        assertTrue(edit is GeometryEdit.Replace)
        val geometry = (edit as GeometryEdit.Replace).geometry
        assertTrue(geometry is Geometry.Polygon)
        val ring = (geometry as Geometry.Polygon).ring
        assertEquals(5, ring.size)
        assertEquals(Coordinate(0.0, 0.0), ring[0])
        assertEquals(Coordinate(3.0, 0.0), ring[1])
        assertEquals(Coordinate(3.0, 2.0), ring[2])
        assertEquals(Coordinate(0.0, 2.0), ring[3])
        assertEquals(Coordinate(0.0, 0.0), ring[4])
    }

    @Test
    fun partial_rectangle_blocks_save() {
        var captured: GrowingSpaceFormData? = null
        composeRule.setContent {
            MyTuinTheme {
                CreateGrowingSpaceContent(
                    seed =
                        GrowingSpaceFormSeed(
                            name = "Bed 1",
                            rectX1 = "0",
                            rectY1 = "0",
                        ),
                    mode = GrowingSpaceFormMode.CREATE,
                    existingGeometryType = null,
                    isSaving = false,
                    saveError = false,
                    onSave = { captured = it },
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("Save").performScrollTo().performClick()
        composeRule
            .onNodeWithText("Enter four valid corners", substring = true)
            .performScrollTo()
            .assertIsDisplayed()
        assertNull(captured)
    }

    @Test
    fun degenerate_rectangle_blocks_save() {
        var captured: GrowingSpaceFormData? = null
        composeRule.setContent {
            MyTuinTheme {
                CreateGrowingSpaceContent(
                    seed =
                        GrowingSpaceFormSeed(
                            name = "Bed 1",
                            rectX1 = "0",
                            rectY1 = "0",
                            rectX2 = "0",
                            rectY2 = "2",
                        ),
                    mode = GrowingSpaceFormMode.CREATE,
                    existingGeometryType = null,
                    isSaving = false,
                    saveError = false,
                    onSave = { captured = it },
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("Save").performScrollTo().performClick()
        composeRule
            .onNodeWithText("Enter four valid corners", substring = true)
            .performScrollTo()
            .assertIsDisplayed()
        assertNull(captured)
    }

    @Test
    fun save_error_shown_when_present() {
        composeRule.setContent {
            MyTuinTheme {
                CreateGrowingSpaceContent(
                    seed = GrowingSpaceFormSeed(name = "Bed 1"),
                    mode = GrowingSpaceFormMode.CREATE,
                    existingGeometryType = null,
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
                CreateGrowingSpaceContent(
                    seed = GrowingSpaceFormSeed(),
                    mode = GrowingSpaceFormMode.CREATE,
                    existingGeometryType = null,
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

    @Test
    fun buttons_disabled_while_saving() {
        composeRule.setContent {
            MyTuinTheme {
                CreateGrowingSpaceContent(
                    seed = GrowingSpaceFormSeed(name = "Bed 1"),
                    mode = GrowingSpaceFormMode.CREATE,
                    existingGeometryType = null,
                    isSaving = true,
                    saveError = false,
                    onSave = {},
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("Cancel").performScrollTo().assertIsNotEnabled()
    }
}
