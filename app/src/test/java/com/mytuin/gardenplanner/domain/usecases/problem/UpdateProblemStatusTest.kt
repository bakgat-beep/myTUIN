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

class UpdateProblemStatusTest {
    private val repository = FakeProblemRepository()
    private val clock = FakeClock(nowMillisValue = 1_700_000_500_000L)
    private val useCase = UpdateProblemStatus(repository, clock)

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
                    status = ProblemStatus.SUSPECTED,
                    createdAt = 1_700_000_000_000L,
                    updatedAt = 1_700_000_000_000L,
                    severity = null,
                    confidence = null,
                    recordStatus = RecordStatus.ACTIVE,
                    description = null,
                    areaId = null,
                    growingSpaceId = null,
                    plantInstanceId = null,
                    notes = null,
                ),
            )
        }

    @Test
    fun invoke_updates_the_status() =
        runBlocking {
            useCase(problemId, ProblemStatus.CONFIRMED)
            assertEquals(ProblemStatus.CONFIRMED, repository.snapshot().single().status)
        }

    @Test
    fun invoke_supplies_updatedAt_from_clock() =
        runBlocking {
            useCase(problemId, ProblemStatus.CONFIRMED)

            assertEquals(
                ProblemStatus.CONFIRMED,
                repository.statusCalls().single().status,
            )
            assertEquals(1_700_000_500_000L, repository.statusCalls().single().at)
            assertEquals(1_700_000_500_000L, repository.snapshot().single().updatedAt)
        }

    @Test
    fun invoke_does_not_change_record_status() =
        runBlocking {
            useCase(problemId, ProblemStatus.RESOLVED)
            assertEquals(RecordStatus.ACTIVE, repository.snapshot().single().recordStatus)
        }
}
