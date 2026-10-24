package com.mytuin.gardenplanner.ui.garden

import com.mytuin.gardenplanner.domain.usecases.garden.CreateGarden
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeGardenRepository
import com.mytuin.gardenplanner.testdoubles.FakeIdGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CreateGardenViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: FakeGardenRepository
    private lateinit var viewModel: CreateGardenViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = FakeGardenRepository()
        val createGarden =
            CreateGarden(
                repository = repository,
                idGenerator = FakeIdGenerator(nextGardenIdValue = "garden_test_0001"),
                clock = FakeClock(nowMillisValue = 1_700_000_000_000L),
            )
        viewModel = CreateGardenViewModel(createGarden)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initial_state_has_empty_fields_and_no_errors() {
        val state = viewModel.uiState.value
        assertEquals("", state.name)
        assertEquals("", state.description)
        assertNull(state.nameError)
        assertNull(state.saveError)
        assertNull(state.savedGardenId)
        assertEquals(false, state.isSaving)
    }

    @Test
    fun onNameChange_updates_name_and_clears_nameError() {
        viewModel.onSave()
        assertEquals(CreateGardenUiState.NameError.BLANK, viewModel.uiState.value.nameError)
        viewModel.onNameChange("Garden")
        assertEquals("Garden", viewModel.uiState.value.name)
        assertNull(viewModel.uiState.value.nameError)
    }

    @Test
    fun onDescriptionChange_updates_description() {
        viewModel.onDescriptionChange("Main plot")
        assertEquals("Main plot", viewModel.uiState.value.description)
    }

    @Test
    fun onSave_with_blank_name_sets_nameError_and_does_not_call_repository() =
        runTest(testDispatcher) {
            viewModel.onSave()
            advanceUntilIdle()

            assertEquals(CreateGardenUiState.NameError.BLANK, viewModel.uiState.value.nameError)
            assertEquals(0, repository.snapshot().size)
            assertNull(viewModel.uiState.value.savedGardenId)
        }

    @Test
    fun onSave_with_valid_name_saves_and_sets_savedGardenId() =
        runTest(testDispatcher) {
            viewModel.onNameChange("Back Garden")
            viewModel.onSave()
            advanceUntilIdle()

            assertEquals(1, repository.snapshot().size)
            assertEquals("garden_test_0001", viewModel.uiState.value.savedGardenId)
            assertEquals(false, viewModel.uiState.value.isSaving)
            assertNull(viewModel.uiState.value.nameError)
            assertNull(viewModel.uiState.value.saveError)
        }

    @Test
    fun onSaveHandled_clears_savedGardenId() =
        runTest(testDispatcher) {
            viewModel.onNameChange("Back Garden")
            viewModel.onSave()
            advanceUntilIdle()
            assertEquals("garden_test_0001", viewModel.uiState.value.savedGardenId)

            viewModel.onSaveHandled()

            assertNull(viewModel.uiState.value.savedGardenId)
        }
}
