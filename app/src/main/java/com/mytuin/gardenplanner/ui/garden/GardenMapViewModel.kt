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
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.InfrastructureType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

/**
 * Garden map ViewModel (Steps 9f, 9g, 9h).
 *
 * Observes one garden, builds MapShapes from its areas / growing
 * spaces / spatial objects, and applies the current MapFilterState
 * before emitting Ready. The filter state is a MutableStateFlow
 * combined into the shapes pipeline so filter toggles do not
 * restart the garden observation — the filter change flows through
 * the inner combine only.
 *
 * Filter state is deliberately not persisted (Q1a): it lives for the
 * life of the ViewModel. Process death resets it to default.
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
        private val _filterState = MutableStateFlow(MapFilterState())
        val filterState: StateFlow<MapFilterState> = _filterState.asStateFlow()

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

        fun setShowAreas(show: Boolean) {
            _filterState.update { it.copy(showAreas = show) }
        }

        fun setShowGrowingSpaces(show: Boolean) {
            _filterState.update { it.copy(showGrowingSpaces = show) }
        }

        fun setShowSpatialObjects(show: Boolean) {
            _filterState.update { it.copy(showSpatialObjects = show) }
        }

        fun toggleHiddenGrowingSpaceType(type: GrowingSpaceType) {
            _filterState.update {
                it.copy(
                    hiddenGrowingSpaceTypes =
                        if (type in it.hiddenGrowingSpaceTypes) {
                            it.hiddenGrowingSpaceTypes - type
                        } else {
                            it.hiddenGrowingSpaceTypes + type
                        },
                )
            }
        }

        fun toggleHiddenInfrastructureType(type: InfrastructureType) {
            _filterState.update {
                it.copy(
                    hiddenInfrastructureTypes =
                        if (type in it.hiddenInfrastructureTypes) {
                            it.hiddenInfrastructureTypes - type
                        } else {
                            it.hiddenInfrastructureTypes + type
                        },
                )
            }
        }

        fun resetFilters() {
            _filterState.value = MapFilterState()
        }

        private fun gardenFlow(garden: Garden): Flow<GardenMapUiState> =
            combine(
                observeAreasInGarden(garden.id),
                observeSpatialObjectsInGarden(garden.id),
                observeGrowingSpacesInGarden(garden.id),
                _filterState,
            ) { areas, objects, spaces, filters ->
                buildState(garden, areas, objects, spaces, filters)
            }

        private fun buildState(
            garden: Garden,
            areas: List<Area>,
            objects: List<SpatialObject>,
            spaces: List<GrowingSpace>,
            filters: MapFilterState,
        ): GardenMapUiState {
            val allShapes = mutableListOf<MapShape>()
            areas.forEach { area ->
                val geometry = area.geometry ?: return@forEach
                allShapes += MapShape(area.id, MapShapeKind.Area, geometry)
            }
            objects.forEach { obj ->
                allShapes += MapShape(obj.id, MapShapeKind.SpatialObject(obj.objectType), obj.geometry)
            }
            spaces.forEach { space ->
                val geometry = space.geometry ?: return@forEach
                allShapes += MapShape(space.id, MapShapeKind.GrowingSpace(space.spaceType), geometry)
            }
            if (allShapes.isEmpty()) return GardenMapUiState.NoGeometries

            val visibleShapes = allShapes.filter { filters.isShapeVisible(it) }
            if (visibleShapes.isEmpty()) return GardenMapUiState.AllLayersHidden

            return GardenMapUiState.Ready(
                gardenName = garden.name,
                origin = originOf(garden),
                shapes = visibleShapes,
                viewport = MapViewport.union(visibleShapes),
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
