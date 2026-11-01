package com.mytuin.gardenplanner.ui.garden

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytuin.gardenplanner.domain.model.garden.Area
import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpace
import com.mytuin.gardenplanner.domain.model.garden.SpatialObject
import com.mytuin.gardenplanner.domain.repository.GardenRepository
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveAreasInGarden
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveGrowingSpacesInGarden
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveSpatialObjectsInGarden
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Step 9f: the Garden map observes one garden (the first active
 * garden returned by the repository), plus its areas, growing spaces
 * and spatial objects, and exposes them as a single GardenMapUiState.
 *
 * The choice of "first garden" matches the POC and Q5's "one garden,
 * no in-map switcher" scope. Multi-garden selection is a Home-screen
 * concern and remains unimplemented.
 *
 * The ViewModel is where domain entities stop: it maps Garden /
 * Area / GrowingSpace / SpatialObject into MapShape values, which
 * carry only geometry plus a semantic kind. MapLibre never sees
 * domain models.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class GardenMapViewModel
    @Inject
    constructor(
        gardenRepository: GardenRepository,
        private val observeGrowingSpacesInGarden: ObserveGrowingSpacesInGarden,
        private val observeSpatialObjectsInGarden: ObserveSpatialObjectsInGarden,
        private val observeAreasInGarden: ObserveAreasInGarden,
    ) : ViewModel() {
        private val baseFlow: Flow<GardenMapUiState> =
            gardenRepository
                .observeGardens()
                .map { gardens -> gardens.firstOrNull() }
                .flatMapLatest { garden ->
                    if (garden == null) {
                        flowOf<GardenMapUiState>(GardenMapUiState.NoGarden)
                    } else {
                        gardenFlow(garden)
                    }
                }

        val uiState: StateFlow<GardenMapUiState> =
            baseFlow
                .catch { emit(GardenMapUiState.Error) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = GardenMapUiState.Loading,
                )

        private fun gardenFlow(garden: Garden): Flow<GardenMapUiState> =
            combine(
                observeAreasInGarden(garden.id),
                observeSpatialObjectsInGarden(garden.id),
                observeGrowingSpacesInGarden(garden.id),
            ) { areas, objects, spaces ->
                buildState(garden, areas, objects, spaces)
            }

        private fun buildState(
            garden: Garden,
            areas: List<Area>,
            objects: List<SpatialObject>,
            spaces: List<GrowingSpace>,
        ): GardenMapUiState {
            val shapes = mutableListOf<MapShape>()
            areas.forEach { area ->
                val geometry = area.geometry ?: return@forEach
                shapes += MapShape(area.id, MapShapeKind.Area, geometry)
            }
            objects.forEach { obj ->
                shapes += MapShape(obj.id, MapShapeKind.SpatialObject(obj.objectType), obj.geometry)
            }
            spaces.forEach { space ->
                val geometry = space.geometry ?: return@forEach
                shapes += MapShape(space.id, MapShapeKind.GrowingSpace(space.spaceType), geometry)
            }
            if (shapes.isEmpty()) return GardenMapUiState.NoGeometries
            return GardenMapUiState.Ready(
                gardenName = garden.name,
                origin = originOf(garden),
                shapes = shapes,
                viewport = MapViewport.union(shapes),
            )
        }

        private fun originOf(garden: Garden): Wgs84 =
            Wgs84(
                latitude = garden.latitude ?: FALLBACK_LATITUDE,
                longitude = garden.longitude ?: FALLBACK_LONGITUDE,
            )

        private companion object {
            const val FALLBACK_LATITUDE = -43.5321
            const val FALLBACK_LONGITUDE = 172.6362
        }
    }
