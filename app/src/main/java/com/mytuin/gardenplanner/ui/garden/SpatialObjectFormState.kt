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
import com.mytuin.gardenplanner.domain.model.garden.SpatialObjectFields
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType

private const val RECT_FIELD_COUNT = 4

@Stable
class SpatialObjectFormState(
    val mode: SpatialObjectFormMode,
    seed: SpatialObjectFormSeed,
    initialAttempted: Boolean = false,
) {
    var objectType by mutableStateOf(seed.objectType)
    var name by mutableStateOf(seed.name)
    var description by mutableStateOf(seed.description)
    var notes by mutableStateOf(seed.notes)
    var areaId by mutableStateOf(seed.areaId)
    var rectX1 by mutableStateOf(seed.rectX1)
    var rectY1 by mutableStateOf(seed.rectY1)
    var rectX2 by mutableStateOf(seed.rectX2)
    var rectY2 by mutableStateOf(seed.rectY2)
    var attempted by mutableStateOf(initialAttempted)

    val errors: SpatialObjectFormErrors
        get() = if (attempted) validate() else SpatialObjectFormErrors.NONE

    fun toFormData(): SpatialObjectFormData? {
        if (!validate().isValid) return null
        return when (mode) {
            SpatialObjectFormMode.CREATE -> {
                val geometry = buildGeometry() ?: return null
                SpatialObjectFormData.ForCreate(
                    objectType = objectType,
                    geometry = geometry,
                    name = name.trim().ifBlank { null },
                    description = description.trim().ifBlank { null },
                    areaId = areaId?.takeIf { it.isNotBlank() },
                    notes = notes.trim().ifBlank { null },
                )
            }
            SpatialObjectFormMode.EDIT ->
                SpatialObjectFormData.ForEdit(
                    fields =
                        SpatialObjectFields(
                            objectType = objectType,
                            name = name.trim().ifBlank { null },
                            description = description.trim().ifBlank { null },
                            areaId = areaId?.takeIf { it.isNotBlank() },
                            notes = notes.trim().ifBlank { null },
                        ),
                )
        }
    }

    private fun validate(): SpatialObjectFormErrors =
        when (mode) {
            SpatialObjectFormMode.CREATE -> SpatialObjectFormErrors(geometryInvalid = !isGeometryValid())
            SpatialObjectFormMode.EDIT -> SpatialObjectFormErrors.NONE
        }

    private fun isGeometryValid(): Boolean =
        when (rectFieldCount()) {
            0 -> false
            RECT_FIELD_COUNT -> {
                val rect = rectValues() ?: return false
                rect.x1 != rect.x2 && rect.y1 != rect.y2
            }
            else -> false
        }

    private fun buildGeometry(): Geometry? {
        val rect = rectValues() ?: return null
        if (rect.x1 == rect.x2 || rect.y1 == rect.y2) return null
        return Geometry.Polygon(
            listOf(
                Coordinate(rect.x1, rect.y1),
                Coordinate(rect.x2, rect.y1),
                Coordinate(rect.x2, rect.y2),
                Coordinate(rect.x1, rect.y2),
                Coordinate(rect.x1, rect.y1),
            ),
        )
    }

    private fun rectFieldCount(): Int = listOf(rectX1, rectY1, rectX2, rectY2).count { it.isNotBlank() }

    // Detekt-safe shape: map/filter/iterator, no bare literals, no
    // index access, no wide destructuring (Rule 20). Matches
    // GrowingSpaceFormState.rectValues verbatim.
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

private val SpatialObjectFormStateSaver =
    listSaver<SpatialObjectFormState, String>(
        save = { state ->
            listOf(
                state.mode.name,
                state.objectType.id,
                state.name,
                state.description,
                state.notes,
                state.areaId ?: "",
                state.rectX1,
                state.rectY1,
                state.rectX2,
                state.rectY2,
                state.attempted.toString(),
            )
        },
        restore = { saved ->
            SpatialObjectFormState(
                mode = SpatialObjectFormMode.valueOf(saved[0]),
                seed =
                    SpatialObjectFormSeed(
                        objectType = InfrastructureType.fromId(saved[1]) ?: InfrastructureType.OTHER,
                        name = saved[2],
                        description = saved[3],
                        notes = saved[4],
                        areaId = saved[5].ifBlank { null },
                        rectX1 = saved[6],
                        rectY1 = saved[7],
                        rectX2 = saved[8],
                        rectY2 = saved[9],
                    ),
                initialAttempted = saved[10].toBooleanStrictOrNull() ?: false,
            )
        },
    )

@Composable
fun rememberSpatialObjectFormState(
    mode: SpatialObjectFormMode,
    seed: SpatialObjectFormSeed,
    initialAttempted: Boolean = false,
): SpatialObjectFormState =
    rememberSaveable(mode, saver = SpatialObjectFormStateSaver) {
        SpatialObjectFormState(mode = mode, seed = seed, initialAttempted = initialAttempted)
    }
