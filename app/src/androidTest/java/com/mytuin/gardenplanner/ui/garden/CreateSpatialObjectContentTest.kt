package com.mytuin.gardenplanner.ui.garden

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.mytuin.gardenplanner.domain.model.garden.Area
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.vocabulary.AreaType
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import com.mytuin.gardenplanner.ui.theme.MyTuinTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CreateSpatialObjectContentTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun renders_key_form_labels() {
        composeRule.setContent {
            MyTuinTheme {
                CreateSpatialObjectContent(
                    seed = SpatialObjectFormSeed(),
                    mode = SpatialObjectFormMode.CREATE,
                    areas = emptyList(),
                    isSaving = false,
                    saveError = false,
                    onSave = {},
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("Name (optional)").assertIsDisplayed()
        composeRule.onNodeWithText("Description").assertIsDisplayed()
        composeRule.onNodeWithText("Area").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Geometry").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Save").performScrollTo().assertIsDisplayed()
        composeRule.onNodeWithText("Cancel").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun missing_geometry_blocks_save() {
        var captured: SpatialObjectFormData? = null
        composeRule.setContent {
            MyTuinTheme {
                CreateSpatialObjectContent(
                    seed = SpatialObjectFormSeed(),
                    mode = SpatialObjectFormMode.CREATE,
                    areas = emptyList(),
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
    fun valid_form_invokes_onSave_with_fields() {
        var captured: SpatialObjectFormData? = null
        composeRule.setContent {
            MyTuinTheme {
                CreateSpatialObjectContent(
                    seed =
                        SpatialObjectFormSeed(
                            objectType = InfrastructureType.FENCE,
                            name = "Back fence",
                            rectX1 = "0",
                            rectY1 = "0",
                            rectX2 = "3",
                            rectY2 = "2",
                        ),
                    mode = SpatialObjectFormMode.CREATE,
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
        val create = captured as SpatialObjectFormData.ForCreate
        assertEquals(InfrastructureType.FENCE, create.objectType)
        assertEquals("Back fence", create.name)
        assertTrue(create.geometry is Geometry.Polygon)
    }

    @Test
    fun blank_name_is_accepted_as_null() {
        var captured: SpatialObjectFormData? = null
        composeRule.setContent {
            MyTuinTheme {
                CreateSpatialObjectContent(
                    seed =
                        SpatialObjectFormSeed(
                            name = "",
                            rectX1 = "0",
                            rectY1 = "0",
                            rectX2 = "1",
                            rectY2 = "1",
                        ),
                    mode = SpatialObjectFormMode.CREATE,
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
        assertNull((captured as SpatialObjectFormData.ForCreate).name)
    }

    @Test
    fun partial_rectangle_blocks_save() {
        var captured: SpatialObjectFormData? = null
        composeRule.setContent {
            MyTuinTheme {
                CreateSpatialObjectContent(
                    seed =
                        SpatialObjectFormSeed(
                            rectX1 = "0",
                            rectY1 = "0",
                        ),
                    mode = SpatialObjectFormMode.CREATE,
                    areas = emptyList(),
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
        var captured: SpatialObjectFormData? = null
        composeRule.setContent {
            MyTuinTheme {
                CreateSpatialObjectContent(
                    seed =
                        SpatialObjectFormSeed(
                            rectX1 = "0",
                            rectY1 = "0",
                            rectX2 = "0",
                            rectY2 = "2",
                        ),
                    mode = SpatialObjectFormMode.CREATE,
                    areas = emptyList(),
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
    fun full_rectangle_produces_polygon_geometry() {
        var captured: SpatialObjectFormData? = null
        composeRule.setContent {
            MyTuinTheme {
                CreateSpatialObjectContent(
                    seed =
                        SpatialObjectFormSeed(
                            rectX1 = "0",
                            rectY1 = "0",
                            rectX2 = "3",
                            rectY2 = "2",
                        ),
                    mode = SpatialObjectFormMode.CREATE,
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
        val geometry = (captured as SpatialObjectFormData.ForCreate).geometry
        val ring = (geometry as Geometry.Polygon).ring
        assertEquals(5, ring.size)
        assertEquals(Coordinate(0.0, 0.0), ring[0])
        assertEquals(Coordinate(3.0, 0.0), ring[1])
        assertEquals(Coordinate(3.0, 2.0), ring[2])
        assertEquals(Coordinate(0.0, 2.0), ring[3])
        assertEquals(Coordinate(0.0, 0.0), ring[4])
    }

    @Test
    fun area_selection_propagates_to_saved_data() {
        val area = sampleArea("area_1", "Left bed")
        var captured: SpatialObjectFormData? = null
        composeRule.setContent {
            MyTuinTheme {
                CreateSpatialObjectContent(
                    seed =
                        SpatialObjectFormSeed(
                            rectX1 = "0",
                            rectY1 = "0",
                            rectX2 = "1",
                            rectY2 = "1",
                        ),
                    mode = SpatialObjectFormMode.CREATE,
                    areas = listOf(area),
                    isSaving = false,
                    saveError = false,
                    onSave = { captured = it },
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("No area").performScrollTo().performClick()
        composeRule.onNodeWithText("Left bed").performClick()
        composeRule.onNodeWithText("Save").performScrollTo().performClick()
        assertNotNull(captured)
        assertEquals("area_1", (captured as SpatialObjectFormData.ForCreate).areaId)
    }

    @Test
    fun save_error_shown_when_present() {
        composeRule.setContent {
            MyTuinTheme {
                CreateSpatialObjectContent(
                    seed =
                        SpatialObjectFormSeed(
                            rectX1 = "0",
                            rectY1 = "0",
                            rectX2 = "1",
                            rectY2 = "1",
                        ),
                    mode = SpatialObjectFormMode.CREATE,
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
                CreateSpatialObjectContent(
                    seed = SpatialObjectFormSeed(),
                    mode = SpatialObjectFormMode.CREATE,
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

    @Test
    fun buttons_disabled_while_saving() {
        composeRule.setContent {
            MyTuinTheme {
                CreateSpatialObjectContent(
                    seed =
                        SpatialObjectFormSeed(
                            rectX1 = "0",
                            rectY1 = "0",
                            rectX2 = "1",
                            rectY2 = "1",
                        ),
                    mode = SpatialObjectFormMode.CREATE,
                    areas = emptyList(),
                    isSaving = true,
                    saveError = false,
                    onSave = {},
                    onCancel = {},
                )
            }
        }
        composeRule.onNodeWithText("Cancel").performScrollTo().assertIsNotEnabled()
    }

    private fun sampleArea(
        id: String,
        name: String,
    ): Area =
        Area(
            id = id,
            gardenId = "garden_test_0001",
            name = name,
            areaType = AreaType.entries.first(),
            description = null,
            geometry = null,
            status = null,
            createdAt = 1_700_000_000_000L,
            updatedAt = 1_700_000_000_000L,
        )
}
