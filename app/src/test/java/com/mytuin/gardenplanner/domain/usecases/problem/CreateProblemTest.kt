package com.mytuin.gardenplanner.domain.usecases.problem

import com.mytuin.gardenplanner.domain.model.garden.NewProblem
import com.mytuin.gardenplanner.domain.vocabulary.Confidence
import com.mytuin.gardenplanner.domain.vocabulary.ProblemCategory
import com.mytuin.gardenplanner.domain.vocabulary.ProblemSeverity
import com.mytuin.gardenplanner.domain.vocabulary.ProblemStatus
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeIdGenerator
import com.mytuin.gardenplanner.testdoubles.FakeProblemRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CreateProblemTest {
    private val repository = FakeProblemRepository()
    private val idGenerator = FakeIdGenerator(nextProblemIdValue = "problem_test_0001")
    private val clock = FakeClock(nowMillisValue = 1_700_000_000_000L)

    private val createProblem =
        CreateProblem(
            repository = repository,
            idGenerator = idGenerator,
            clock = clock,
        )

    @Test
    fun invoke_returns_the_generated_id() =
        runBlocking {
            val id = createProblem(minimalInput())
            assertEquals("problem_test_0001", id)
        }

    @Test
    fun invoke_persists_with_assigned_id_record_status_and_timestamps() =
        runBlocking {
            createProblem(minimalInput())

            val problems = repository.snapshot()
            assertEquals(1, problems.size)
            val problem = problems.first()

            assertEquals("problem_test_0001", problem.id)
            assertEquals("garden_test_0001", problem.gardenId)
            assertEquals("Aphids on tomatoes", problem.name)
            assertEquals(ProblemCategory.PEST, problem.problemType)
            assertEquals(ProblemStatus.SUSPECTED, problem.status)
            assertEquals(RecordStatus.ACTIVE, problem.recordStatus)
            assertEquals(1_700_000_000_000L, problem.createdAt)
            assertEquals(1_700_000_000_000L, problem.updatedAt)
        }

    @Test
    fun invoke_defaults_status_to_suspected() =
        runBlocking {
            createProblem(minimalInput())
            assertEquals(ProblemStatus.SUSPECTED, repository.snapshot().first().status)
        }

    @Test
    fun invoke_preserves_explicit_status() =
        runBlocking {
            createProblem(minimalInput().copy(status = ProblemStatus.CONFIRMED))
            assertEquals(ProblemStatus.CONFIRMED, repository.snapshot().first().status)
        }

    @Test
    fun invoke_defaults_all_optional_fields_to_null() =
        runBlocking {
            createProblem(minimalInput())
            val problem = repository.snapshot().first()

            assertNull(problem.severity)
            assertNull(problem.confidence)
            assertNull(problem.description)
            assertNull(problem.areaId)
            assertNull(problem.growingSpaceId)
            assertNull(problem.plantInstanceId)
            assertNull(problem.notes)
        }

    @Test
    fun invoke_preserves_severity_and_confidence() =
        runBlocking {
            createProblem(
                minimalInput().copy(
                    severity = ProblemSeverity.MAJOR,
                    confidence = Confidence.MODERATE,
                ),
            )
            val problem = repository.snapshot().first()

            assertEquals(ProblemSeverity.MAJOR, problem.severity)
            assertEquals(Confidence.MODERATE, problem.confidence)
        }

    @Test
    fun invoke_accepts_all_thirteen_categories() =
        runBlocking {
            ProblemCategory.entries.forEach { category ->
                createProblem(minimalInput().copy(problemType = category))
            }
            assertEquals(13, repository.snapshot().size)
        }

    @Test
    fun invoke_accepts_all_seven_statuses() =
        runBlocking {
            ProblemStatus.entries.forEach { status ->
                createProblem(minimalInput().copy(status = status))
            }
            assertEquals(7, repository.snapshot().size)
        }

    private fun minimalInput(): NewProblem =
        NewProblem(
            gardenId = "garden_test_0001",
            name = "Aphids on tomatoes",
            problemType = ProblemCategory.PEST,
        )
}
