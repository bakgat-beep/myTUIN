package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.database.GardenDatabaseFactory
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.recommendation.Recommendation
import com.mytuin.gardenplanner.domain.model.recommendation.RecommendationEvidence
import com.mytuin.gardenplanner.domain.model.recommendation.RecommendationFactor
import com.mytuin.gardenplanner.domain.repository.RecommendationRepository
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.RecommendationEvidenceRole
import com.mytuin.gardenplanner.domain.vocabulary.RecommendationEvidenceSourceType
import com.mytuin.gardenplanner.domain.vocabulary.RecommendationFactorResult
import com.mytuin.gardenplanner.domain.vocabulary.RecommendationReasonType
import com.mytuin.gardenplanner.domain.vocabulary.RecommendationStatus
import com.mytuin.gardenplanner.domain.vocabulary.RecommendationSubjectType
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.domain.vocabulary.SuitabilityDimension
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
class RecommendationRepositoryTest {
    private lateinit var db: GardenDatabase
    private lateinit var repository: RecommendationRepository

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
                RecommendationRepositoryImpl(
                    recommendationDao = db.recommendationDao(),
                    recommendationFactorDao = db.recommendationFactorDao(),
                    recommendationEvidenceDao = db.recommendationEvidenceDao(),
                    gardenDao = db.gardenDao(),
                )

            db.gardenDao().insert(sampleGarden())
        }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getRecommendation_returns_null_when_none_exists() =
        runBlocking {
            assertNull(repository.getRecommendation("recommendation_does_not_exist"))
        }

    @Test
    fun insert_then_get_round_trips_all_fields() =
        runBlocking {
            val recommendation = sampleRecommendation()
            repository.insertRecommendation(recommendation)

            assertEquals(recommendation, repository.getRecommendation(recommendation.id))
        }

    @Test
    fun insert_requires_existing_garden() =
        runBlocking {
            val orphan =
                sampleRecommendation().copy(
                    id = "recommendation_00000000-0000-0000-0000-0000000000bb",
                    gardenId = "garden_does_not_exist",
                )
            try {
                repository.insertRecommendation(orphan)
                fail("Expected ValidationError; insert succeeded")
            } catch (expected: ValidationError) {
                assertEquals("garden_id", expected.field)
            }
        }

    @Test
    fun insert_does_not_precheck_polymorphic_subject_id() =
        runBlocking {
            // subject_id refers to a plant that does not exist. No FK
            // to enforce it, so the insert succeeds (R2).
            val recommendation =
                sampleRecommendation().copy(subjectId = "plant_does_not_exist")
            repository.insertRecommendation(recommendation)

            assertEquals(
                "plant_does_not_exist",
                repository.getRecommendation(recommendation.id)?.subjectId,
            )
        }

    @Test
    fun insertFactor_requires_existing_recommendation() =
        runBlocking {
            try {
                repository.insertFactor(
                    sampleFactor().copy(recommendationId = "recommendation_does_not_exist"),
                )
                fail("Expected ValidationError; insert succeeded")
            } catch (expected: ValidationError) {
                assertEquals("recommendation_id", expected.field)
            }
        }

    @Test
    fun observeFactorsForRecommendation_returns_inserted_factors() =
        runBlocking {
            val recommendation = sampleRecommendation()
            repository.insertRecommendation(recommendation)
            repository.insertFactor(sampleFactor())
            repository.insertFactor(
                sampleFactor().copy(
                    id = "recommendationfactor_00000000-0000-0000-0000-000000000002",
                    dimension = SuitabilityDimension.WATER,
                    result = RecommendationFactorResult.GOOD,
                ),
            )

            val factors = repository.observeFactorsForRecommendation(recommendation.id).first()
            assertEquals(2, factors.size)
            assertEquals(
                setOf(SuitabilityDimension.LIGHT, SuitabilityDimension.WATER),
                factors.map { it.dimension }.toSet(),
            )
        }

    @Test
    fun insertEvidence_requires_existing_recommendation() =
        runBlocking {
            try {
                repository.insertEvidence(
                    sampleEvidence().copy(recommendationId = "recommendation_does_not_exist"),
                )
                fail("Expected ValidationError; insert succeeded")
            } catch (expected: ValidationError) {
                assertEquals("recommendation_id", expected.field)
            }
        }

    @Test
    fun insertEvidence_does_not_precheck_polymorphic_source_id() =
        runBlocking {
            val recommendation = sampleRecommendation()
            repository.insertRecommendation(recommendation)

            val evidence =
                sampleEvidence().copy(sourceId = "observation_does_not_exist")
            repository.insertEvidence(evidence)

            val retrieved = repository.observeEvidenceForRecommendation(recommendation.id).first()
            assertEquals(1, retrieved.size)
            assertEquals("observation_does_not_exist", retrieved.first().sourceId)
        }

    @Test
    fun observeEvidenceForRecommendation_returns_inserted_evidence() =
        runBlocking {
            val recommendation = sampleRecommendation()
            repository.insertRecommendation(recommendation)
            repository.insertEvidence(sampleEvidence())

            val evidence = repository.observeEvidenceForRecommendation(recommendation.id).first()
            assertEquals(1, evidence.size)
            assertEquals(RecommendationEvidenceSourceType.OBSERVATION, evidence.first().sourceType)
            assertEquals(RecommendationEvidenceRole.SUPPORTS, evidence.first().role)
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

    private fun sampleRecommendation(): Recommendation =
        Recommendation(
            id = "recommendation_00000000-0000-0000-0000-000000000001",
            gardenId = gardenId,
            createdAt = 1_700_000_000_000L,
            subjectType = RecommendationSubjectType.PLANT,
            subjectId = "plant_00000000-0000-0000-0000-000000000001",
            recommendationType = ActivityType.PLANTING,
            status = RecommendationStatus.RECOMMENDED,
            confidence = Confidence.MODERATE,
            overallResult = null,
            expiresAt = null,
            context = "Spring sowing window",
            notes = null,
        )

    private fun sampleFactor(): RecommendationFactor =
        RecommendationFactor(
            id = "recommendationfactor_00000000-0000-0000-0000-000000000001",
            recommendationId = "recommendation_00000000-0000-0000-0000-000000000001",
            dimension = SuitabilityDimension.LIGHT,
            result = RecommendationFactorResult.EXCELLENT,
            confidence = Confidence.HIGH,
            weight = null,
            reasonType = RecommendationReasonType.LIGHT_MATCH,
            notes = null,
        )

    private fun sampleEvidence(): RecommendationEvidence =
        RecommendationEvidence(
            id = "recommendationevidence_00000000-0000-0000-0000-000000000001",
            recommendationId = "recommendation_00000000-0000-0000-0000-000000000001",
            sourceType = RecommendationEvidenceSourceType.OBSERVATION,
            sourceId = "observation_00000000-0000-0000-0000-000000000001",
            role = RecommendationEvidenceRole.SUPPORTS,
            notes = "7 hours of summer sun observed",
        )
}
