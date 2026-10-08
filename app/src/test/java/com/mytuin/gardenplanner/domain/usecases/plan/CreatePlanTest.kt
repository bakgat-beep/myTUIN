package com.mytuin.gardenplanner.domain.usecases.plan

import com.mytuin.gardenplanner.domain.model.garden.KnownDate
import com.mytuin.gardenplanner.domain.model.garden.NewPlan
import com.mytuin.gardenplanner.domain.vocabulary.ActivityQuantityUnit
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.PlanningPriority
import com.mytuin.gardenplanner.domain.vocabulary.PlanningStatus
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeIdGenerator
import com.mytuin.gardenplanner.testdoubles.FakePlanRepository
import java.time.LocalDate
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CreatePlanTest {
    private val repository = FakePlanRepository()
    private val idGenerator = FakeIdGenerator(nextPlanIdValue = "plan_test_0001")
    private val clock = FakeClock(nowMillisValue = 1_700_000_000_000L)

    private val createPlan =
        CreatePlan(
            repository = repository,
            idGenerator = idGenerator,
            clock = clock,
        )

    @Test
    fun invoke_returns_the_generated_id() =
        runBlocking {
            val id = createPlan(minimalInput())
            assertEquals("plan_test_0001", id)
        }

    @Test
    fun invoke_persists_with_assigned_id_record_status_and_timestamps() =
        runBlocking {
            createPlan(minimalInput())

            val plans = repository.snapshot()
            assertEquals(1, plans.size)
            val plan = plans.first()

            assertEquals("plan_test_0001", plan.id)
            assertEquals("garden_test_0001", plan.gardenId)
            assertEquals(ActivityType.WATERING, plan.planType)
            assertEquals(PlanningStatus.IDEA, plan.status)
            assertEquals(RecordStatus.ACTIVE, plan.recordStatus)
            assertEquals(1_700_000_000_000L, plan.createdAt)
            assertEquals(1_700_000_000_000L, plan.updatedAt)
        }

    @Test
    fun invoke_defaults_status_to_idea() =
        runBlocking {
            createPlan(minimalInput())
            assertEquals(PlanningStatus.IDEA, repository.snapshot().first().status)
        }

    @Test
    fun invoke_defaults_all_optional_fields_to_null() =
        runBlocking {
            createPlan(minimalInput())
            val plan = repository.snapshot().first()

            assertNull(plan.plannedStart)
            assertNull(plan.plannedEnd)
            assertNull(plan.quantity)
            assertNull(plan.unit)
            assertNull(plan.priority)
            assertNull(plan.notes)
        }

    @Test
    fun invoke_preserves_dates_quantity_unit_priority_and_notes() =
        runBlocking {
            createPlan(
                minimalInput().copy(
                    plannedStart = KnownDate(LocalDate.of(2026, 3, 1)),
                    plannedEnd = KnownDate(LocalDate.of(2026, 3, 15)),
                    quantity = 20.0,
                    unit = ActivityQuantityUnit.LITRE,
                    priority = PlanningPriority.HIGH,
                    notes = "Water along the south edge",
                ),
            )
            val plan = repository.snapshot().first()

            assertEquals(LocalDate.of(2026, 3, 1), plan.plannedStart?.date)
            assertEquals(LocalDate.of(2026, 3, 15), plan.plannedEnd?.date)
            assertEquals(20.0, plan.quantity!!, 0.0)
            assertEquals(ActivityQuantityUnit.LITRE, plan.unit)
            assertEquals(PlanningPriority.HIGH, plan.priority)
            assertEquals("Water along the south edge", plan.notes)
        }

    @Test
    fun invoke_accepts_all_ten_planning_statuses() =
        runBlocking {
            PlanningStatus.entries.forEach { status ->
                createPlan(minimalInput().copy(status = status))
            }
            assertEquals(10, repository.snapshot().size)
        }

    @Test
    fun invoke_accepts_all_eleven_activity_types() =
        runBlocking {
            ActivityType.entries.forEach { type ->
                createPlan(minimalInput().copy(planType = type))
            }
            assertEquals(11, repository.snapshot().size)
        }

    private fun minimalInput(): NewPlan =
        NewPlan(
            gardenId = "garden_test_0001",
            planType = ActivityType.WATERING,
        )
}
