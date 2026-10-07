package com.mytuin.gardenplanner.domain.usecases.problem

import com.mytuin.gardenplanner.domain.model.garden.Problem
import com.mytuin.gardenplanner.domain.vocabulary.ProblemCategory
import com.mytuin.gardenplanner.domain.vocabulary.ProblemStatus
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeProblemRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RestoreProblemTest {
    private val repository = FakeProblemRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_001_000_000L)
    private val useCase = RestoreProblem(repository, clock)

    private val problemId = "problem_test_0001"

    @Before
    fun setUp() =
        runBlocking {
            repository.insert(
                Problem(
                    id = problemId,
                    gardenId = "garden_test_0001",
                    name = "Aphids on tomatoes",
                    problemType = ProblemCategory.PEST,
                    status = ProblemStatus.RESOLVED,
                    createdAt = 1_700_000_000_000L,
                    updatedAt = 1_700_000_500_000L,
                    severity = null,
                    confidence = null,
                    recordStatus = RecordStatus.ARCHIVED,
                    description = null,
                    areaId = null,
                    growingSpaceId = null,
                    plantInstanceId = null,
                    notes = null,
                ),
            )
        }

    @Test
    fun invoke_sets_record_status_to_active() =
        runBlocking {
            useCase(problemId)
            assertEquals(RecordStatus.ACTIVE, repository.snapshot().single().recordStatus)
        }

    @Test
    fun invoke_does_not_change_problem_status() =
        runBlocking {
            useCase(problemId)
            assertEquals(ProblemStatus.RESOLVED, repository.snapshot().single().status)
        }
}
