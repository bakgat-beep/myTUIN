package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.database.GardenDatabaseFactory
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.data.entities.GrowingSpaceEntity
import com.mytuin.gardenplanner.data.entities.PlantEntity
import com.mytuin.gardenplanner.data.entities.PlantInstanceEntity
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.KnownDate
import com.mytuin.gardenplanner.domain.model.garden.Plan
import com.mytuin.gardenplanner.domain.repository.PlanRepository
import com.mytuin.gardenplanner.domain.vocabulary.ActivityQuantityUnit
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.PlanTargetType
import com.mytuin.gardenplanner.domain.vocabulary.PlanningPriority
import com.mytuin.gardenplanner.domain.vocabulary.PlanningStatus
import com.mytuin.gardenplanner.domain.vocabulary.PlantLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import java.time.LocalDate
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
class PlanRepositoryTest {
    private lateinit var db: GardenDatabase
    private lateinit var repository: PlanRepository

    private val gardenId = "garden_00000000-0000-0000-0000-000000000001"
    private val spaceId = "growingspace_00000000-0000-0000-0000-000000000001"
    private val plantId = "plant_00000000-0000-0000-0000-000000000001"
    private val instanceId = "plantinstance_00000000-0000-0000-0000-000000000001"

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
                PlanRepositoryImpl(
                    planDao = db.planDao(),
                    planTargetDao = db.planTargetDao(),
                    gardenDao = db.gardenDao(),
                    plantDao = db.plantDao(),
                    cultivarDao = db.cultivarDao(),
                    plantInstanceDao = db.plantInstanceDao(),
                    growingSpaceDao = db.growingSpaceDao(),
                    areaDao = db.areaDao(),
                    spatialObjectDao = db.spatialObjectDao(),
                )

            db.gardenDao().insert(sampleGarden())
            db.growingSpaceDao().insert(sampleGrowingSpace())
            db.plantDao().insert(samplePlant())
            db.plantInstanceDao().insert(samplePlantInstance())
        }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getPlan_returns_null_when_none_exists() =
        runBlocking {
            assertNull(repository.getPlan("plan_does_not_exist"))
        }

    @Test
    fun insert_then_get_round_trips_all_fields() =
        runBlocking {
            val plan = samplePlan()
            repository.insert(plan)

            assertEquals(plan, repository.getPlan(plan.id))
        }

    @Test
    fun insert_requires_existing_garden() =
        runBlocking {
            val orphan =
                samplePlan().copy(
                    id = "plan_00000000-0000-0000-0000-0000000000bb",
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
    fun plan_type_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(samplePlan().copy(planType = ActivityType.HARVESTING))

            db.openHelper.readableDatabase
                .query("SELECT plan_type FROM plan")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("harvesting", cursor.getString(0))
                }
        }

    @Test
    fun planning_status_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(samplePlan().copy(status = PlanningStatus.SCHEDULED))

            db.openHelper.readableDatabase
                .query("SELECT status FROM plan")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("scheduled", cursor.getString(0))
                }
        }

    @Test
    fun planned_dates_round_trip() =
        runBlocking {
            repository.insert(
                samplePlan().copy(
                    plannedStart = KnownDate(LocalDate.of(2026, 3, 1)),
                    plannedEnd = KnownDate(LocalDate.of(2026, 3, 15)),
                ),
            )

            val retrieved = repository.getPlan("plan_00000000-0000-0000-0000-000000000001")!!
            assertEquals(LocalDate.of(2026, 3, 1), retrieved.plannedStart?.date)
            assertEquals(LocalDate.of(2026, 3, 15), retrieved.plannedEnd?.date)
        }

    @Test
    fun updateStatus_changes_planning_status_and_updatedAt() =
        runBlocking {
            val plan = samplePlan()
            repository.insert(plan)

            repository.updateStatus(plan.id, PlanningStatus.COMPLETED, 1_700_000_500_000L)

            val updated = repository.getPlan(plan.id)!!
            assertEquals(PlanningStatus.COMPLETED, updated.status)
            assertEquals(1_700_000_500_000L, updated.updatedAt)
            assertEquals(RecordStatus.ACTIVE, updated.recordStatus)
        }

    @Test
    fun archive_does_not_change_planning_status() =
        runBlocking {
            val plan = samplePlan().copy(status = PlanningStatus.SCHEDULED)
            repository.insert(plan)

            repository.archive(plan.id, 1_700_000_500_000L)

            val updated = repository.getPlan(plan.id)!!
            assertEquals(RecordStatus.ARCHIVED, updated.recordStatus)
            assertEquals(PlanningStatus.SCHEDULED, updated.status)
        }

    @Test
    fun archive_excludes_plan_from_default_query() =
        runBlocking {
            val plan = samplePlan()
            repository.insert(plan)
            repository.archive(plan.id, 1_700_000_500_000L)

            assertEquals(0, repository.observePlansInGarden(gardenId).first().size)
            assertEquals(
                1,
                repository.observePlansInGarden(gardenId, includeArchived = true).first().size,
            )
        }

    @Test
    fun addTarget_creates_a_target_row() =
        runBlocking {
            val plan = samplePlan()
            repository.insert(plan)

            repository.addTarget(
                planId = plan.id,
                targetType = PlanTargetType.GROWING_SPACE,
                targetId = spaceId,
                notes = "North edge",
            )

            val targets = repository.observeTargetsForPlan(plan.id).first()
            assertEquals(1, targets.size)
            assertEquals(spaceId, targets.single().targetId)
            assertEquals(PlanTargetType.GROWING_SPACE, targets.single().targetType)
            assertEquals("North edge", targets.single().notes)
            assertNull(targets.single().completedAt)
        }

    @Test
    fun addTarget_requires_existing_plan() =
        runBlocking {
            try {
                repository.addTarget(
                    planId = "plan_does_not_exist",
                    targetType = PlanTargetType.GROWING_SPACE,
                    targetId = spaceId,
                    notes = null,
                )
                fail("Expected ValidationError; addTarget succeeded")
            } catch (expected: ValidationError) {
                assertEquals("plan_id", expected.field)
            }
        }

    @Test
    fun addTarget_requires_existing_target() =
        runBlocking {
            val plan = samplePlan()
            repository.insert(plan)

            try {
                repository.addTarget(
                    planId = plan.id,
                    targetType = PlanTargetType.GROWING_SPACE,
                    targetId = "growingspace_does_not_exist",
                    notes = null,
                )
                fail("Expected ValidationError; addTarget succeeded")
            } catch (expected: ValidationError) {
                assertEquals("target_id", expected.field)
            }
        }

    @Test
    fun addTarget_dispatches_correctly_for_each_target_type() =
        runBlocking {
            val plan = samplePlan()
            repository.insert(plan)

            // All four valid targets here.
            repository.addTarget(plan.id, PlanTargetType.GROWING_SPACE, spaceId, null)
            repository.addTarget(plan.id, PlanTargetType.PLANT, plantId, null)
            repository.addTarget(plan.id, PlanTargetType.PLANT_INSTANCE, instanceId, null)

            // Cross-type mismatch: a plant id supplied as a growing-space target.
            try {
                repository.addTarget(plan.id, PlanTargetType.GROWING_SPACE, plantId, null)
                fail("Expected ValidationError; cross-type addTarget succeeded")
            } catch (expected: ValidationError) {
                assertEquals("target_id", expected.field)
            }

            assertEquals(3, repository.observeTargetsForPlan(plan.id).first().size)
        }

    @Test
    fun completeTarget_sets_completed_at() =
        runBlocking {
            val plan = samplePlan()
            repository.insert(plan)
            repository.addTarget(plan.id, PlanTargetType.GROWING_SPACE, spaceId, null)

            repository.completeTarget(
                plan.id,
                PlanTargetType.GROWING_SPACE,
                spaceId,
                1_700_000_500_000L,
            )

            assertEquals(
                1_700_000_500_000L,
                repository
                    .observeTargetsForPlan(plan.id)
                    .first()
                    .single()
                    .completedAt,
            )
        }

    @Test
    fun removeTarget_removes_the_target_row() =
        runBlocking {
            val plan = samplePlan()
            repository.insert(plan)
            repository.addTarget(plan.id, PlanTargetType.GROWING_SPACE, spaceId, null)

            repository.removeTarget(plan.id, PlanTargetType.GROWING_SPACE, spaceId)

            assertEquals(0, repository.observeTargetsForPlan(plan.id).first().size)
        }

    @Test
    fun re_adding_a_completed_target_preserves_completed_at() =
        runBlocking {
            val plan = samplePlan()
            repository.insert(plan)
            repository.addTarget(plan.id, PlanTargetType.GROWING_SPACE, spaceId, "First")
            repository.completeTarget(plan.id, PlanTargetType.GROWING_SPACE, spaceId, 1_700_000_500_000L)

            repository.addTarget(plan.id, PlanTargetType.GROWING_SPACE, spaceId, "Second")

            val target = repository.observeTargetsForPlan(plan.id).first().single()
            assertEquals("Second", target.notes)
            assertEquals(1_700_000_500_000L, target.completedAt)
        }

    @Test
    fun updateStatus_throws_NotFoundError_when_plan_missing() =
        runBlocking {
            try {
                repository.updateStatus("plan_does_not_exist", PlanningStatus.SCHEDULED, 1_700_000_500_000L)
                fail("Expected NotFoundError")
            } catch (expected: NotFoundError) {
                assertEquals("Plan", expected.entityType)
            }
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

    private fun samplePlan(): Plan =
        Plan(
            id = "plan_00000000-0000-0000-0000-000000000001",
            gardenId = gardenId,
            planType = ActivityType.WATERING,
            status = PlanningStatus.PLANNED,
            recordStatus = RecordStatus.ACTIVE,
            createdAt = 1_700_000_000_000L,
            updatedAt = 1_700_000_000_000L,
            plannedStart = KnownDate(LocalDate.of(2026, 3, 1)),
            plannedEnd = KnownDate(LocalDate.of(2026, 3, 15)),
            quantity = 20.0,
            unit = ActivityQuantityUnit.LITRE,
            priority = PlanningPriority.HIGH,
            notes = "Water the south edge",
        )
}
