package com.mytuin.gardenplanner.domain.usecases.source

import com.mytuin.gardenplanner.domain.model.garden.Source
import com.mytuin.gardenplanner.domain.vocabulary.SourceStatus
import com.mytuin.gardenplanner.domain.vocabulary.SourceType
import com.mytuin.gardenplanner.testdoubles.FakeSourceRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UpdateSourceStatusTest {
    private val repository = FakeSourceRepository()
    private val useCase = UpdateSourceStatus(repository)

    private val sourceId = "source_test_0001"

    @Before
    fun setUp() =
        runBlocking {
            repository.insert(
                Source(
                    id = sourceId,
                    sourceType = SourceType.GOVERNMENT,
                    status = SourceStatus.UNVERIFIED,
                    createdAt = 1_700_000_000_000L,
                    title = "Handbook",
                    authorOrOrganisation = null,
                    publicationDate = null,
                    accessDate = null,
                    url = null,
                    geographicScope = null,
                    hemisphere = null,
                    context = null,
                    notes = null,
                ),
            )
        }

    @Test
    fun invoke_updates_the_status() =
        runBlocking {
            useCase(sourceId, SourceStatus.VERIFIED)
            assertEquals(SourceStatus.VERIFIED, repository.snapshot().single().status)
        }

    @Test
    fun invoke_passes_status_through_to_repository() =
        runBlocking {
            useCase(sourceId, SourceStatus.VERIFIED)

            val call = repository.statusCalls().single()
            assertEquals(sourceId, call.id)
            assertEquals(SourceStatus.VERIFIED, call.status)
        }

    @Test
    fun invoke_does_not_change_other_fields() =
        runBlocking {
            useCase(sourceId, SourceStatus.OUTDATED)

            val source = repository.snapshot().single()
            assertEquals(sourceId, source.id)
            assertEquals(SourceType.GOVERNMENT, source.sourceType)
            assertEquals("Handbook", source.title)
            assertEquals(1_700_000_000_000L, source.createdAt)
        }
}
