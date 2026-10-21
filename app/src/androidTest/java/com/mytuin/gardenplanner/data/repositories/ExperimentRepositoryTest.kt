package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.database.GardenDatabaseFactory
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.Experiment
import com.mytuin.gardenplanner.domain.model.garden.ExperimentLink
import com.mytuin.gardenplanner.domain.model.garden.KnownDate
import com.mytuin.gardenplanner.domain.repository.ExperimentRepository
import com.mytuin.gardenplanner.domain.vocabulary.ExperimentLinkType
import com.mytuin.gardenplanner.domain.vocabulary.ExperimentStatus
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import java.time.LocalDate
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ExperimentRepositoryTest {
    private lateinit var db: GardenDatabase
    private lateinit var repository: ExperimentRepository

    private val gardenId = "garden_00000000-0000-0000-0000-000000000001"

    @Before
    fun setUp() =
        runBlocking {
            val context = ApplicationProvider.getApplicationContext<Context>()
            db =
                Room
                    .inMemoryDatabaseBuilder(context, GardenDatabase::class.java)
                    .addCallback(GardenDatabaseFactory.foreignKeysCallback)
                    .build()
            repository =
                ExperimentRepositoryImpl(
                    experimentDao = db.experimentDao(),
                    experimentLinkDao = db.experimentLinkDao(),
                    gardenDao = db.gardenDao(),
                )

            db.gardenDao().insert(sampleGarden())
        }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getExperiment_returns_null_when_none_exists() =
        runBlocking {
            assertNull(repository.getExperiment("experiment_does_not_exist"))
        }

    @Test
    fun insert_then_get_round_trips_all_fields() =
        runBlocking {
            val experiment = sampleExperiment()
            repository.insert(experiment)

            assertEquals(experiment, repository.getExperiment(experiment.id))
        }

    @Test
    fun insert_requires_existing_garden() =
        runBlocking {
            val orphan =
                sampleExperiment().copy(
                    id = "experiment_00000000-0000-0000-0000-0000000000bb",
                    gardenId = "garden_does_not_exist",
                )
            try {
                repository.insert(orphan)
                fail("Expected ValidationError; insert succeeded")
            } catch (expected: ValidationError) {
                assertEquals("garden_id", expected.field)
            }
        }

    @Test
    fun updateStatus_changes_status_and_updatedAt() =
        runBlocking {
            val experiment = sampleExperiment()
            repository.insert(experiment)

            repository.updateStatus(experiment.id, ExperimentStatus.ACTIVE, 1_700_000_500_000L)

            val updated = repository.getExperiment(experiment.id)!!
            assertEquals(ExperimentStatus.ACTIVE, updated.status)
            assertEquals(1_700_000_500_000L, updated.updatedAt)
        }

    @Test
    fun updateStatus_throws_NotFoundError_when_missing() =
        runBlocking {
            try {
                repository.updateStatus(
                    "experiment_does_not_exist",
                    ExperimentStatus.ACTIVE,
                    1_700_000_500_000L,
                )
                fail("Expected NotFoundError")
            } catch (expected: NotFoundError) {
                assertEquals("Experiment", expected.entityType)
            }
        }

    @Test
    fun addLink_creates_a_link_row() =
        runBlocking {
            val experiment = sampleExperiment()
            repository.insert(experiment)

            repository.addLink(
                ExperimentLink(
                    experimentId = experiment.id,
                    linkedType = ExperimentLinkType.ACTIVITY,
                    linkedId = "activity_00000000-0000-0000-0000-000000000001",
                    notes = null,
                ),
            )

            val links = repository.observeLinksForExperiment(experiment.id).first()
            assertEquals(1, links.size)
            assertEquals(ExperimentLinkType.ACTIVITY, links.single().linkedType)
        }

    @Test
    fun removeLink_removes_the_row() =
        runBlocking {
            val experiment = sampleExperiment()
            repository.insert(experiment)
            repository.addLink(
                ExperimentLink(
                    experimentId = experiment.id,
                    linkedType = ExperimentLinkType.ACTIVITY,
                    linkedId = "activity_00000000-0000-0000-0000-000000000001",
                    notes = null,
                ),
            )

            repository.removeLink(
                experiment.id,
                ExperimentLinkType.ACTIVITY,
                "activity_00000000-0000-0000-0000-000000000001",
            )

            assertEquals(0, repository.observeLinksForExperiment(experiment.id).first().size)
        }

    private fun sampleGarden(): GardenEntity =
        GardenEntity(
            id = gardenId,
            name = "Test Garden",
            description = null,
            country_code = null,
            region = null,
            locality = null,
            latitude = null,
            longitude = null,
            timezone = null,
            hemisphere = Hemisphere.UNKNOWN,
            status = RecordStatus.DRAFT,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
        )

    private fun sampleExperiment(): Experiment =
        Experiment(
            id = "experiment_00000000-0000-0000-0000-000000000001",
            gardenId = gardenId,
            name = "Mulch experiment",
            question = "Does mulch reduce watering?",
            status = ExperimentStatus.DRAFT,
            createdAt = 1_700_000_000_000L,
            updatedAt = 1_700_000_000_000L,
            hypothesis = "Mulch will reduce watering events.",
            startDate = KnownDate(LocalDate.of(2026, 10, 1)),
            endDate = KnownDate(LocalDate.of(2026, 12, 31)),
            notes = null,
        )
}
