package com.mytuin.gardenplanner.ui.garden

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.repository.GardenRepository
import com.mytuin.gardenplanner.domain.repository.GrowingSpaceHistoryRepository
import com.mytuin.gardenplanner.domain.repository.SpatialObjectHistoryRepository
import com.mytuin.gardenplanner.domain.time.Clock
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveGrowingSpacesInGarden
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveSpatialObjectsInGarden
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * Garden history ViewModel (Step 9i).
 *
 * Observes the first garden's growing spaces and spatial objects
 * (archived included — Q4), loads each entity's history once per
 * emission, and builds a GardenHistoryTimeline. Exposes the timeline
 * inside Ready plus a separate requested-time channel that drives
 * per-instant reconstruction.
 *
 * The requested time is `null` until the user scrubs. When null, the
 * effective time is the timeline's maxTime ("now"), which means on
 * first emission the user sees the current state of the garden and
 * can scrub backwards from there.
 *
 * "Jump to now" is expressed as `setTime(Long.MAX_VALUE)` internally
 * because clamping handles the ceiling — this keeps the ViewModel
 * free of a redundant method.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class GardenMapHistoryViewModel
    @Inject
    constructor(
        gardenRepository: GardenRepository,
        private val observeGrowingSpacesInGarden: ObserveGrowingSpacesInGarden,
        private val observeSpatialObjectsInGarden: ObserveSpatialObjectsInGarden,
        private val growingSpaceHistoryRepository: GrowingSpaceHistoryRepository,
        private val spatialObjectHistoryRepository: SpatialObjectHistoryRepository,
        private val clock: Clock,
    ) : ViewModel() {
        private val requestedTime = MutableStateFlow<Long?>(null)

        private val baseState: Flow<GardenMapHistoryUiState> =
            gardenRepository
                .observeGardens()
                .map { gardens -> gardens.firstOrNull() }
                .flatMapLatest { garden ->
                    if (garden == null) {
                        flowOf<GardenMapHistoryUiState>(GardenMapHistoryUiState.NoGarden)
                    } else {
                        gardenFlow(garden)
                    }
                }

        val uiState: StateFlow<GardenMapHistoryUiState> =
            combine(baseState, requestedTime) { base, time ->
                when (base) {
                    is GardenMapHistoryUiState.Ready -> {
                        val t =
                            (time ?: base.timeline.maxTime)
                                .coerceIn(base.timeline.minTime, base.timeline.maxTime)
                        base.copy(
                            currentTime = t,
                            shapes = base.timeline.shapesAt(t),
                        )
                    }
                    else -> base
                }
            }.catch { emit(GardenMapHistoryUiState.Error) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    initialValue = GardenMapHistoryUiState.Loading,
                )

        fun setTime(t: Long) {
            requestedTime.value = t
        }

        fun jumpToNow() {
            requestedTime.value = Long.MAX_VALUE
        }

        private fun gardenFlow(garden: Garden): Flow<GardenMapHistoryUiState> =
            combine(
                observeGrowingSpacesInGarden(garden.id, includeArchived = true),
                observeSpatialObjectsInGarden(garden.id, includeArchived = true),
            ) { spaces, objects ->
                val spaceSnapshots =
                    spaces.map { space ->
                        GrowingSpaceHistorySnapshot(
                            id = space.id,
                            type = space.spaceType,
                            createdAt = space.createdAt,
                            currentGeometry = space.geometry,
                            history = growingSpaceHistoryRepository.getHistoryForSpace(space.id),
                        )
                    }
                val objectSnapshots =
                    objects.map { obj ->
                        SpatialObjectHistorySnapshot(
                            id = obj.id,
                            type = obj.objectType,
                            createdAt = obj.createdAt,
                            currentGeometry = obj.geometry,
                            history = spatialObjectHistoryRepository.getHistoryForObject(obj.id),
                        )
                    }
                val timeline = GardenHistoryTimeline.build(clock.nowMillis(), spaceSnapshots, objectSnapshots)
                when {
                    timeline == null -> GardenMapHistoryUiState.NoShapes
                    !timeline.hasAnyHistory -> GardenMapHistoryUiState.NoHistory
                    else ->
                        GardenMapHistoryUiState.Ready(
                            gardenName = garden.name,
                            origin = originOf(garden),
                            timeline = timeline,
                            currentTime = timeline.maxTime,
                            shapes = timeline.shapesAt(timeline.maxTime),
                            viewport = timeline.viewport,
                        )
                }
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
