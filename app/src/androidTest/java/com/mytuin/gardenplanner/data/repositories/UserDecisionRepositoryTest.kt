package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.database.GardenDatabaseFactory
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.data.entities.PlanEntity
import com.mytuin.gardenplanner.data.entities.RecommendationEntity
import com.mytuin.gardenplanner.data.entities.UserDecisionEntity
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.recommendation.UserDecision
import com.mytuin.gardenplanner.domain.repository.UserDecisionRepository
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.PlanningStatus
import com.mytuin.gardenplanner.domain.vocabulary.RecommendationStatus
import com.mytuin.gardenplanner.domain.vocabulary.RecommendationSubjectType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.domain.vocabulary.UserDecisionType
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
class UserDecisionRepositoryTest {
    private lateinit var db: GardenDatabase
    private lateinit var repository: UserDecisionRepository

    private val gardenId = "garden_00000000-0000-0000-0000-000000000001"
    private val recommendationId = "recommendation_00000000-0000-0000-0000-000000000001"
    private val planId = "plan_00000000-0000-0000-0000-000000000001"

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
                UserDecisionRepositoryImpl(
                    userDecisionDao = db.userDecisionDao(),
                    gardenDao = db.gardenDao(),
                    recommendationDao = db.recommendationDao(),
                    planDao = db.planDao(),
                )

            db.gardenDao().insert(sampleGarden())
            db.recommendationDao().insert(sampleRecommendationEntity())
            db.planDao().insert(samplePlanEntity())
        }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getUserDecision_returns_null_when_none_exists() =
        runBlocking {
            assertNull(repository.getUserDecision("userdecision_does_not_exist"))
        }

    @Test
    fun insert_then_get_round_trips_all_fields() =
        runBlocking {
            val decision = sampleDecision()
            repository.insert(decision)

            assertEquals(decision, repository.getUserDecision(decision.id))
        }

    @Test
    fun insert_requires_existing_garden() =
        runBlocking {
            val orphan =
                sampleDecision().copy(
                    id = "userdecision_00000000-0000-0000-0000-0000000000bb",
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
    fun insert_requires_existing_recommendation_when_supplied() =
        runBlocking {
            val orphan =
                sampleDecision().copy(
                    id = "userdecision_00000000-0000-0000-0000-0000000000cc",
                    recommendationId = "recommendation_does_not_exist",
                )
            try {
                repository.insert(orphan)
                fail("Expected ValidationError; insert succeeded")
            } catch (expected: ValidationError) {
                assertEquals("recommendation_id", expected.field)
            }
        }

    @Test
    fun insert_requires_existing_plan_when_supplied() =
        runBlocking {
            val orphan =
                sampleDecision().copy(
                    id = "userdecision_00000000-0000-0000-0000-0000000000dd",
                    planId = "plan_does_not_exist",
                )
            try {
                repository.insert(orphan)
                fail("Expected ValidationError; insert succeeded")
            } catch (expected: ValidationError) {
                assertEquals("plan_id", expected.field)
            }
        }

    @Test
    fun mapper_rejects_decision_with_neither_recommendation_nor_plan() =
        runBlocking {
            // Bypass the repository pre-checks with a direct DAO insert.
            db.userDecisionDao().insert(
                UserDecisionEntity(
                    id = "userdecision_00000000-0000-0000-0000-0000000000ee",
                    garden_id = gardenId,
                    decision_type = UserDecisionType.ACCEPTED,
                    decided_at = 1_700_000_000_000L,
                    created_at = 1_700_000_000_000L,
                    recommendation_id = null,
                    plan_id = null,
                    reason = null,
                    notes = null,
                ),
            )

            try {
                repository.observeUserDecisionsInGarden(gardenId).first()
                fail("Expected IllegalArgumentException; mapper accepted both-null row")
            } catch (expected: IllegalArgumentException) {
                // Expected.
            }
        }

    @Test
    fun observeUserDecisionsInGarden_returns_inserted() =
        runBlocking {
            repository.insert(sampleDecision())
            repository.insert(
                sampleDecision().copy(
                    id = "userdecision_00000000-0000-0000-0000-000000000002",
                    recommendationId = null,
                    planId = planId,
                ),
            )

            val decisions = repository.observeUserDecisionsInGarden(gardenId).first()
            assertEquals(2, decisions.size)
        }

    @Test
    fun observeUserDecisionsForRecommendation_scopes_correctly() =
        runBlocking {
            repository.insert(sampleDecision())
            repository.insert(
                sampleDecision().copy(
                    id = "userdecision_00000000-0000-0000-0000-000000000002",
                    recommendationId = null,
                    planId = planId,
                ),
            )

            val decisions =
                repository.observeUserDecisionsForRecommendation(recommendationId).first()
            assertEquals(1, decisions.size)
            assertEquals(recommendationId, decisions.single().recommendationId)
        }

    @Test
    fun observeUserDecisionsForPlan_scopes_correctly() =
        runBlocking {
            repository.insert(
                sampleDecision().copy(
                    id = "userdecision_00000000-0000-0000-0000-000000000002",
                    recommendationId = null,
                    planId = planId,
                ),
            )

            val decisions = repository.observeUserDecisionsForPlan(planId).first()
            assertEquals(1, decisions.size)
            assertEquals(planId, decisions.single().planId)
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

    private fun sampleRecommendationEntity(): RecommendationEntity =
        RecommendationEntity(
            id = recommendationId,
            garden_id = gardenId,
            created_at = 1_700_000_000_000L,
            subject_type = RecommendationSubjectType.PLANT,
            subject_id = "plant_00000000-0000-0000-0000-000000000001",
            recommendation_type = ActivityType.PLANTING,
            status = RecommendationStatus.RECOMMENDED,
            confidence = Confidence.MODERATE,
        )

    private fun samplePlanEntity(): PlanEntity =
        PlanEntity(
            id = planId,
            garden_id = gardenId,
            plan_type = ActivityType.PLANTING,
            status = PlanningStatus.PLANNED,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
            record_status = RecordStatus.ACTIVE,
        )

    private fun sampleDecision(): UserDecision =
        UserDecision(
            id = "userdecision_00000000-0000-0000-0000-000000000001",
            gardenId = gardenId,
            decisionType = UserDecisionType.ACCEPTED,
            decidedAt = 1_700_000_500_000L,
            createdAt = 1_700_000_000_000L,
            recommendationId = recommendationId,
            planId = null,
            reason = null,
            notes = null,
        )
}
