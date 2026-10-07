package com.mytuin.gardenplanner.domain.usecases.problem

import com.mytuin.gardenplanner.domain.vocabulary.ProblemEvidenceDirection
import com.mytuin.gardenplanner.testdoubles.FakeProblemRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class UnlinkObservationFromProblemTest {
    private val repository = FakeProblemRepository()
    private val useCase = UnlinkObservationFromProblem(repository)

    private val problemId = "problem_test_0001"
    private val observationId = "observation_test_0001"

    @Test
    fun invoke_removes_the_link() =
        runBlocking {
            repository.linkObservation(
                problemId,
                observationId,
                ProblemEvidenceDirection.SUPPORTS,
                null,
            )
            assertEquals(1, repository.linksSnapshot().size)

            useCase(problemId, observationId)

            assertEquals(0, repository.linksSnapshot().size)
        }

    @Test
    fun invoke_on_a_missing_link_is_not_an_error() =
        runBlocking {
            useCase(problemId, observationId)
            assertEquals(0, repository.linksSnapshot().size)
        }
}
