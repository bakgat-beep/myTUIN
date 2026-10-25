package com.mytuin.gardenplanner.ui.garden

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.mytuin.gardenplanner.domain.model.garden.Garden
import com.mytuin.gardenplanner.domain.usecases.garden.ObserveGarden
import com.mytuin.gardenplanner.domain.usecases.garden.UpdateGardenLocation
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeCountryRepository
import com.mytuin.gardenplanner.testdoubles.FakeGardenRepository
import com.mytuin.gardenplanner.ui.navigation.Routes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GardenDetailViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val gardenId = "garden_test_0001"
    private lateinit var repository: FakeGardenRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeGardenRepository()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun buildViewModel(): GardenDetailViewModel {
        val handle = SavedStateHandle(mapOf(Routes.GARDEN_DETAIL_ARG_GARDEN_ID to gardenId))
        val update = UpdateGardenLocation(repository, FakeClock(nowMillisValue = 1_700_000_500_000L))
        return GardenDetailViewModel(handle, ObserveGarden(repository), update, FakeCountryRepository())
    }

    @Test
    fun loads_garden_and_emits_ready_state() =
        runTest(testDispatcher) {
            repository.insert(sampleGarden())
            val vm = buildViewModel()
            advanceUntilIdle()

            val state = vm.uiState.value
            assertEquals(false, state.isLoading)
            assertEquals(false, state.loadError)
            assertEquals("Back Garden", state.garden?.name)
        }

    @Test
    fun null_garden_sets_load_error() =
        runTest(testDispatcher) {
            val vm = buildViewModel()
            advanceUntilIdle()

            assertEquals(false, vm.uiState.value.isLoading)
            assertTrue(vm.uiState.value.loadError)
            assertNull(vm.uiState.value.garden)
        }

    @Test
    fun open_edit_sheet_toggles_state() =
        runTest(testDispatcher) {
            repository.insert(sampleGarden())
            val vm = buildViewModel()
            advanceUntilIdle()

            vm.openEditSheet()
            assertTrue(vm.uiState.value.isEditSheetOpen)

            vm.closeEditSheet()
            assertEquals(false, vm.uiState.value.isEditSheetOpen)
        }

    @Test
    fun save_location_updates_garden_and_emits_saved_event() =
        runTest(testDispatcher) {
            repository.insert(sampleGarden())
            val vm = buildViewModel()
            advanceUntilIdle()

            vm.openEditSheet()
            vm.saveLocation(
                com.mytuin.gardenplanner.domain.model.garden.NewGardenLocation(
                    countryCode = "AU",
                    hemisphere = Hemisphere.SOUTHERN,
                ),
            )
            advanceUntilIdle()

            assertEquals(false, vm.uiState.value.isEditSheetOpen)
            assertEquals(false, vm.uiState.value.isEditSaving)
            assertNotNull(vm.uiState.value.savedEvent)
            // Garden's persisted location updated
            assertEquals("AU", repository.snapshot().single().countryCode)
        }

    @Test
    fun onSavedEventHandled_clears_saved_event() =
        runTest(testDispatcher) {
            repository.insert(sampleGarden())
            val vm = buildViewModel()
            advanceUntilIdle()

            vm.openEditSheet()
            vm.saveLocation(
                com.mytuin.gardenplanner.domain.model.garden
                    .NewGardenLocation(countryCode = "AU"),
            )
            advanceUntilIdle()
            assertNotNull(vm.uiState.value.savedEvent)

            vm.onSavedEventHandled()
            assertNull(vm.uiState.value.savedEvent)
        }

    @Test
    fun undo_restores_prior_location() =
        runTest(testDispatcher) {
            repository.insert(
                sampleGarden().copy(countryCode = "NZ", hemisphere = Hemisphere.SOUTHERN),
            )
            val vm = buildViewModel()
            advanceUntilIdle()

            vm.openEditSheet()
            vm.saveLocation(
                com.mytuin.gardenplanner.domain.model.garden.NewGardenLocation(
                    countryCode = "AU",
                    hemisphere = Hemisphere.SOUTHERN,
                ),
            )
            advanceUntilIdle()
            assertEquals("AU", repository.snapshot().single().countryCode)

            vm.undoLocation()
            advanceUntilIdle()

            assertEquals("NZ", repository.snapshot().single().countryCode)
        }

    @Test
    fun uiState_emits_loading_then_ready_with_turbine() =
        runTest(testDispatcher) {
            repository.insert(sampleGarden())
            val vm = buildViewModel()

            vm.uiState.test {
                val first = awaitItem()
                assertTrue("First state should have isLoading true", first.isLoading)

                advanceUntilIdle()
                val second = awaitItem()
                assertEquals(false, second.isLoading)
                assertEquals("Back Garden", second.garden?.name)

                cancelAndIgnoreRemainingEvents()
            }
        }

    private fun sampleGarden(): Garden =
        Garden(
            id = gardenId,
            name = "Back Garden",
            description = null,
            countryCode = null,
            region = null,
            locality = null,
            latitude = null,
            longitude = null,
            timezone = null,
            hemisphere = Hemisphere.UNKNOWN,
            status = RecordStatus.DRAFT,
            createdAt = 1_700_000_000_000L,
            updatedAt = 1_700_000_000_000L,
        )
}
