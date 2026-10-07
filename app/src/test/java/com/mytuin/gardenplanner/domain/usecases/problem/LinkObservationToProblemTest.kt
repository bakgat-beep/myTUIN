package com.mytuin.gardenplanner.domain.usecases.problem

import com.mytuin.gardenplanner.domain.vocabulary.ProblemEvidenceDirection
import com.mytuin.gardenplanner.testdoubles.FakeProblemRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LinkObservationToProblemTest {
    private val repository = FakeProblemRepository()
    private val useCase = LinkObservationToProblem(repository)

    private val problemId = "problem_test_0001"
    private val observationId = "observation_test_0001"

    @Test
    fun invoke_creates_a_link() =
        runBlocking {
            useCase(problemId, observationId)

            val link = repository.linksSnapshot().single()
            assertEquals(problemId, link.problemId)
            assertEquals(observationId, link.observationId)
            assertNull(link.evidenceDirection)
            assertNull(link.notes)
        }

    @Test
    fun invoke_preserves_evidence_direction_and_notes() =
        runBlocking {
            useCase(
                problemId,
                observationId,
                ProblemEvidenceDirection.SUPPORTS,
                "White powder on leaves",
            )

            val link = repository.linksSnapshot().single()
            assertEquals(ProblemEvidenceDirection.SUPPORTS, link.evidenceDirection)
            assertEquals("White powder on leaves", link.notes)
        }

    @Test
    fun relinking_the_same_pair_replaces_the_prior_link() =
        runBlocking {
            useCase(problemId, observationId, ProblemEvidenceDirection.WEAKLY_SUPPORTS, null)
            useCase(problemId, observationId, ProblemEvidenceDirection.SUPPORTS, "Confirmed")

            val links = repository.linksSnapshot()
            assertEquals(1, links.size)
            assertEquals(ProblemEvidenceDirection.SUPPORTS, links.single().evidenceDirection)
            assertEquals("Confirmed", links.single().notes)
        }
}
