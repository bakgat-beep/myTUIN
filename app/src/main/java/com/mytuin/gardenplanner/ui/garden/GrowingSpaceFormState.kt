package com.mytuin.gardenplanner.ui.garden

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpaceFields
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType

private const val RECT_FIELD_COUNT = 4

@Stable
class GrowingSpaceFormState(
    seed: GrowingSpaceFormSeed,
    initialAttempted: Boolean = false,
) {
    var name by mutableStateOf(seed.name)
    var spaceType by mutableStateOf(seed.spaceType)
    var description by mutableStateOf(seed.description)
    var lengthText by mutableStateOf(seed.lengthText)
    var widthText by mutableStateOf(seed.widthText)
    var heightText by mutableStateOf(seed.heightText)
    var diameterText by mutableStateOf(seed.diameterText)
    var areaText by mutableStateOf(seed.areaText)
    var volumeText by mutableStateOf(seed.volumeText)
    var notes by mutableStateOf(seed.notes)
    var rectX1 by mutableStateOf(seed.rectX1)
    var rectY1 by mutableStateOf(seed.rectY1)
    var rectX2 by mutableStateOf(seed.rectX2)
    var rectY2 by mutableStateOf(seed.rectY2)
    var attempted by mutableStateOf(initialAttempted)

    val errors: GrowingSpaceFormErrors
        get() = if (attempted) validate() else GrowingSpaceFormErrors.NONE

    fun toFormData(): GrowingSpaceFormData? {
        if (!validate().isValid) return null
        return GrowingSpaceFormData(
            fields =
                GrowingSpaceFields(
                    name = name.trim(),
                    spaceType = spaceType,
                    description = description.trim().ifBlank { null },
                    lengthMetres = lengthText.trim().toDoubleOrNull(),
                    widthMetres = widthText.trim().toDoubleOrNull(),
                    heightMetres = heightText.trim().toDoubleOrNull(),
                    diameterMetres = diameterText.trim().toDoubleOrNull(),
                    areaSquareMetres = areaText.trim().toDoubleOrNull(),
                    volumeCubicMetres = volumeText.trim().toDoubleOrNull(),
                    notes = notes.trim().ifBlank { null },
                ),
            geometryEdit = computeGeometryEdit(),
        )
    }

    private fun validate(): GrowingSpaceFormErrors =
        GrowingSpaceFormErrors(
            nameBlank = name.isBlank(),
            lengthInvalid = dimensionInvalid(lengthText),
            widthInvalid = dimensionInvalid(widthText),
            heightInvalid = dimensionInvalid(heightText),
            diameterInvalid = dimensionInvalid(diameterText),
            areaInvalid = dimensionInvalid(areaText),
            volumeInvalid = dimensionInvalid(volumeText),
            geometryInvalid = !isGeometryValid(),
        )

    private fun dimensionInvalid(text: String): Boolean = text.isNotBlank() && (text.trim().toDoubleOrNull()?.let { it > 0.0 } != true)

    private fun isGeometryValid(): Boolean =
        when (rectFieldCount()) {
            0 -> true
            RECT_FIELD_COUNT -> {
                val rect = rectValues() ?: return false
                rect.x1 != rect.x2 && rect.y1 != rect.y2
            }
            else -> false
        }

    private fun computeGeometryEdit(): GeometryEdit {
        if (rectFieldCount() == 0) return GeometryEdit.Unchanged
        val rect = rectValues() ?: return GeometryEdit.Unchanged
        return GeometryEdit.Replace(
            Geometry.Polygon(
                listOf(
                    Coordinate(rect.x1, rect.y1),
                    Coordinate(rect.x2, rect.y1),
                    Coordinate(rect.x2, rect.y2),
                    Coordinate(rect.x1, rect.y2),
                    Coordinate(rect.x1, rect.y1),
                ),
            ),
        )
    }

    private fun rectFieldCount(): Int = listOf(rectX1, rectY1, rectX2, rectY2).count { it.isNotBlank() }

    private fun rectValues(): RectValues? {
        val values =
            listOf(rectX1, rectY1, rectX2, rectY2)
                .map { it.trim().toDoubleOrNull() }
        if (values.any { it == null }) return null
        val iterator = values.filterNotNull().iterator()
        return RectValues(iterator.next(), iterator.next(), iterator.next(), iterator.next())
    }

    private data class RectValues(
        val x1: Double,
        val y1: Double,
        val x2: Double,
        val y2: Double,
    )
}

private val GrowingSpaceFormStateSaver =
    listSaver<GrowingSpaceFormState, String>(
        save = { state ->
            listOf(
                state.name,
                state.spaceType.id,
                state.description,
                state.lengthText,
                state.widthText,
                state.heightText,
                state.diameterText,
                state.areaText,
                state.volumeText,
                state.notes,
                state.rectX1,
                state.rectY1,
                state.rectX2,
                state.rectY2,
                state.attempted.toString(),
            )
        },
        restore = { saved ->
            GrowingSpaceFormState(
                seed =
                    GrowingSpaceFormSeed(
                        name = saved[0],
                        spaceType = GrowingSpaceType.fromId(saved[1]) ?: GrowingSpaceType.GARDEN_BED,
                        description = saved[2],
                        lengthText = saved[3],
                        widthText = saved[4],
                        heightText = saved[5],
                        diameterText = saved[6],
                        areaText = saved[7],
                        volumeText = saved[8],
                        notes = saved[9],
                        rectX1 = saved[10],
                        rectY1 = saved[11],
                        rectX2 = saved[12],
                        rectY2 = saved[13],
                    ),
                initialAttempted = saved[14].toBooleanStrictOrNull() ?: false,
            )
        },
    )

@Composable
fun rememberGrowingSpaceFormState(
    seed: GrowingSpaceFormSeed,
    initialAttempted: Boolean = false,
): GrowingSpaceFormState =
    rememberSaveable(saver = GrowingSpaceFormStateSaver) {
        GrowingSpaceFormState(seed = seed, initialAttempted = initialAttempted)
    }
