package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.database.GardenDatabaseFactory
import com.mytuin.gardenplanner.data.entities.ActivityEntity
import com.mytuin.gardenplanner.data.entities.AreaEntity
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.data.entities.GrowingSpaceEntity
import com.mytuin.gardenplanner.data.entities.ObservationEntity
import com.mytuin.gardenplanner.data.entities.PlantEntity
import com.mytuin.gardenplanner.data.entities.PlantInstanceEntity
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.Problem
import com.mytuin.gardenplanner.domain.repository.ProblemRepository
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.AreaType
import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.ObservationType
import com.mytuin.gardenplanner.domain.vocabulary.PlantLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.ProblemCategory
import com.mytuin.gardenplanner.domain.vocabulary.ProblemEvidenceDirection
import com.mytuin.gardenplanner.domain.vocabulary.ProblemSeverity
import com.mytuin.gardenplanner.domain.vocabulary.ProblemStatus
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProblemRepositoryTest {
    private lateinit var db: GardenDatabase
    private lateinit var repository: ProblemRepository

    private val gardenId = "garden_00000000-0000-0000-0000-000000000001"
    private val areaId = "area_00000000-0000-0000-0000-000000000001"
    private val spaceId = "growingspace_00000000-0000-0000-0000-000000000001"
    private val plantId = "plant_00000000-0000-0000-0000-000000000001"
    private val instanceId = "plantinstance_00000000-0000-0000-0000-000000000001"
    private val observationId = "observation_00000000-0000-0000-0000-000000000001"
    private val observationActivityId = "activity_00000000-0000-0000-0000-0000000000ob"

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
                ProblemRepositoryImpl(
                    problemDao = db.problemDao(),
                    problemObservationDao = db.problemObservationDao(),
                    gardenDao = db.gardenDao(),
                    areaDao = db.areaDao(),
                    growingSpaceDao = db.growingSpaceDao(),
                    plantInstanceDao = db.plantInstanceDao(),
                    observationDao = db.observationDao(),
                )

            db.gardenDao().insert(sampleGarden())
            db.areaDao().insert(sampleArea())
            db.growingSpaceDao().insert(sampleGrowingSpace())
            db.plantDao().insert(samplePlant())
            db.plantInstanceDao().insert(samplePlantInstance())
            db.activityDao().insert(sampleObservationActivity())
            db.observationDao().insert(sampleObservation())
        }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getProblem_returns_null_when_none_exists() =
        runBlocking {
            assertNull(repository.getProblem("problem_does_not_exist"))
        }

    @Test
    fun insert_then_get_round_trips_all_fields() =
        runBlocking {
            val problem = sampleProblem()
            repository.insert(problem)

            assertEquals(problem, repository.getProblem(problem.id))
        }

    @Test
    fun insert_requires_existing_garden() =
        runBlocking {
            val orphan =
                sampleProblem().copy(
                    id = "problem_00000000-0000-0000-0000-0000000000bb",
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
    fun insert_requires_existing_plant_instance_when_supplied() =
        runBlocking {
            val orphan =
                sampleProblem().copy(
                    id = "problem_00000000-0000-0000-0000-0000000000cc",
                    plantInstanceId = "plantinstance_does_not_exist",
                )
            try {
                repository.insert(orphan)
                fail("Expected ValidationError; insert succeeded")
            } catch (expected: ValidationError) {
                assertEquals("plant_instance_id", expected.field)
            }
        }

    @Test
    fun category_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(sampleProblem().copy(problemType = ProblemCategory.DISEASE))

            db.openHelper.readableDatabase
                .query("SELECT problem_type FROM problem")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("disease", cursor.getString(0))
                }
        }

    @Test
    fun status_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(sampleProblem().copy(status = ProblemStatus.CONFIRMED))

            db.openHelper.readableDatabase
                .query("SELECT status FROM problem")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("confirmed", cursor.getString(0))
                }
        }

    @Test
    fun record_status_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(sampleProblem().copy(recordStatus = RecordStatus.ACTIVE))

            db.openHelper.readableDatabase
                .query("SELECT record_status FROM problem")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("active", cursor.getString(0))
                }
        }

    @Test
    fun updateStatus_changes_the_problem_status_and_updatedAt() =
        runBlocking {
            val problem = sampleProblem()
            repository.insert(problem)

            repository.updateStatus(problem.id, ProblemStatus.CONFIRMED, 1_700_000_500_000L)

            val updated = repository.getProblem(problem.id)!!
            assertEquals(ProblemStatus.CONFIRMED, updated.status)
            assertEquals(1_700_000_500_000L, updated.updatedAt)
            // Record status is untouched.
            assertEquals(RecordStatus.ACTIVE, updated.recordStatus)
        }

    @Test
    fun updateStatus_throws_NotFoundError_when_problem_missing() =
        runBlocking {
            try {
                repository.updateStatus("problem_does_not_exist", ProblemStatus.RESOLVED, 1_700_000_500_000L)
                fail("Expected NotFoundError")
            } catch (expected: NotFoundError) {
                assertEquals("Problem", expected.entityType)
            }
        }

    @Test
    fun archive_sets_record_status_and_excludes_from_default_observation() =
        runBlocking {
            val problem = sampleProblem()
            repository.insert(problem)

            repository.archive(problem.id, 1_700_000_500_000L)

            assertEquals(
                RecordStatus.ARCHIVED,
                repository.getProblem(problem.id)?.recordStatus,
            )
            assertEquals(
                0,
                repository.observeProblemsInGarden(gardenId).first().size,
            )
            assertEquals(
                1,
                repository.observeProblemsInGarden(gardenId, includeArchived = true).first().size,
            )
        }

    @Test
    fun archive_does_not_change_the_problem_status() =
        runBlocking {
            val problem = sampleProblem().copy(status = ProblemStatus.ACTIVE)
            repository.insert(problem)

            repository.archive(problem.id, 1_700_000_500_000L)

            assertEquals(
                ProblemStatus.ACTIVE,
                repository.getProblem(problem.id)?.status,
            )
        }

    @Test
    fun restore_returns_the_problem_to_active_record_status() =
        runBlocking {
            val problem = sampleProblem()
            repository.insert(problem)
            repository.archive(problem.id, 1_700_000_500_000L)

            repository.restore(problem.id, 1_700_001_000_000L)

            assertEquals(
                RecordStatus.ACTIVE,
                repository.getProblem(problem.id)?.recordStatus,
            )
        }

    @Test
    fun linkObservation_creates_a_link_with_evidence_direction() =
        runBlocking {
            val problem = sampleProblem()
            repository.insert(problem)

            repository.linkObservation(
                problemId = problem.id,
                observationId = observationId,
                evidenceDirection = ProblemEvidenceDirection.SUPPORTS,
                notes = "White powder on leaves",
            )

            val links = repository.observeLinkedObservations(problem.id).first()
            assertEquals(1, links.size)
            assertEquals(observationId, links.single().observationId)
            assertEquals(ProblemEvidenceDirection.SUPPORTS, links.single().evidenceDirection)
            assertEquals("White powder on leaves", links.single().notes)
        }

    @Test
    fun linkObservation_requires_existing_problem() =
        runBlocking {
            try {
                repository.linkObservation(
                    problemId = "problem_does_not_exist",
                    observationId = observationId,
                    evidenceDirection = null,
                    notes = null,
                )
                fail("Expected ValidationError; link succeeded")
            } catch (expected: ValidationError) {
                assertEquals("problem_id", expected.field)
            }
        }

    @Test
    fun linkObservation_requires_existing_observation() =
        runBlocking {
            val problem = sampleProblem()
            repository.insert(problem)

            try {
                repository.linkObservation(
                    problemId = problem.id,
                    observationId = "observation_does_not_exist",
                    evidenceDirection = null,
                    notes = null,
                )
                fail("Expected ValidationError; link succeeded")
            } catch (expected: ValidationError) {
                assertEquals("observation_id", expected.field)
            }
        }

    @Test
    fun relinking_the_same_pair_replaces_the_evidence_direction() =
        runBlocking {
            val problem = sampleProblem()
            repository.insert(problem)

            repository.linkObservation(
                problem.id,
                observationId,
                ProblemEvidenceDirection.WEAKLY_SUPPORTS,
                null,
            )
            repository.linkObservation(
                problem.id,
                observationId,
                ProblemEvidenceDirection.SUPPORTS,
                "Confirmed",
            )

            val links = repository.observeLinkedObservations(problem.id).first()
            assertEquals(1, links.size)
            assertEquals(ProblemEvidenceDirection.SUPPORTS, links.single().evidenceDirection)
            assertEquals("Confirmed", links.single().notes)
        }

    @Test
    fun unlinkObservation_removes_the_link() =
        runBlocking {
            val problem = sampleProblem()
            repository.insert(problem)
            repository.linkObservation(
                problem.id,
                observationId,
                ProblemEvidenceDirection.SUPPORTS,
                null,
            )

            repository.unlinkObservation(problem.id, observationId)

            assertEquals(
                0,
                repository.observeLinkedObservations(problem.id).first().size,
            )
        }

    @Test
    fun observeLinkedProblems_returns_links_by_observation() =
        runBlocking {
            val problem = sampleProblem()
            repository.insert(problem)
            repository.linkObservation(
                problem.id,
                observationId,
                ProblemEvidenceDirection.SUPPORTS,
                null,
            )

            val links = repository.observeLinkedProblems(observationId).first()
            assertEquals(1, links.size)
            assertEquals(problem.id, links.single().problemId)
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

    private fun sampleArea(): AreaEntity =
        AreaEntity(
            id = areaId,
            garden_id = gardenId,
            name = "Vegetable Garden",
            area_type = AreaType.ZONE,
            description = null,
            geometry_type = null,
            geometry_data = null,
            status = RecordStatus.ACTIVE,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
        )

    private fun sampleGrowingSpace(): GrowingSpaceEntity =
        GrowingSpaceEntity(
            id = spaceId,
            garden_id = gardenId,
            name = "Bed 2",
            space_type = GrowingSpaceType.RAISED_BED,
            status = RecordStatus.ACTIVE,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
            geometry_type = null,
            geometry_data = null,
            length = null,
            width = null,
            height = null,
            diameter = null,
            area = null,
            volume = null,
            description = null,
            area_id = null,
            notes = null,
        )

    private fun samplePlant(): PlantEntity =
        PlantEntity(
            id = plantId,
            canonical_name = "Test plant",
            scientific_name = null,
            genus = null,
            species = null,
            family = null,
            lifecycle = PlantLifecycle.PERENNIAL,
            description = null,
            status = RecordStatus.ACTIVE,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
        )

    private fun samplePlantInstance(): PlantInstanceEntity =
        PlantInstanceEntity(
            id = instanceId,
            garden_id = gardenId,
            plant_id = plantId,
            status = RecordStatus.ACTIVE,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
        )

    private fun sampleObservationActivity(): ActivityEntity =
        ActivityEntity(
            id = observationActivityId,
            garden_id = gardenId,
            activity_type = ActivityType.OBSERVATION,
            occurred_at = 1_700_000_000_000L,
            created_at = 1_700_000_000_000L,
            status = RecordStatus.ACTIVE,
        )

    private fun sampleObservation(): ObservationEntity =
        ObservationEntity(
            id = observationId,
            garden_id = gardenId,
            activity_id = observationActivityId,
            observed_at = 1_700_000_000_000L,
            observation_type = ObservationType.PLANT,
            confidence = Confidence.MODERATE,
            created_at = 1_700_000_000_000L,
        )

    private fun sampleProblem(): Problem =
        Problem(
            id = "problem_00000000-0000-0000-0000-000000000001",
            gardenId = gardenId,
            name = "Aphids on tomatoes",
            problemType = ProblemCategory.PEST,
            status = ProblemStatus.SUSPECTED,
            createdAt = 1_700_000_000_000L,
            updatedAt = 1_700_000_000_000L,
            severity = ProblemSeverity.MODERATE,
            confidence = Confidence.MODERATE,
            recordStatus = RecordStatus.ACTIVE,
            description = "Visible on the undersides of leaves",
            areaId = areaId,
            growingSpaceId = spaceId,
            plantInstanceId = instanceId,
            notes = null,
        )
}
