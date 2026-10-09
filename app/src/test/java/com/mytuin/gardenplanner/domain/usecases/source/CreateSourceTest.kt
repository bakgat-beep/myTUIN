package com.mytuin.gardenplanner.domain.usecases.source

import com.mytuin.gardenplanner.domain.model.garden.KnownDate
import com.mytuin.gardenplanner.domain.model.garden.NewSource
import com.mytuin.gardenplanner.domain.vocabulary.GeographicScope
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.SourceStatus
import com.mytuin.gardenplanner.domain.vocabulary.SourceType
import com.mytuin.gardenplanner.testdoubles.FakeClock
import com.mytuin.gardenplanner.testdoubles.FakeIdGenerator
import com.mytuin.gardenplanner.testdoubles.FakeSourceRepository
import java.time.LocalDate
import java.time.LocalTime
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CreateSourceTest {
    private val repository = FakeSourceRepository()
    private val idGenerator = FakeIdGenerator(nextSourceIdValue = "source_test_0001")
    private val clock = FakeClock(nowMillisValue = 1_700_000_000_000L)

    private val createSource =
        CreateSource(
            repository = repository,
            idGenerator = idGenerator,
            clock = clock,
        )

    @Test
    fun invoke_returns_the_generated_id() =
        runBlocking {
            val id = createSource(minimalInput())
            assertEquals("source_test_0001", id)
        }

    @Test
    fun invoke_persists_with_assigned_id_and_createdAt() =
        runBlocking {
            createSource(minimalInput())

            val sources = repository.snapshot()
            assertEquals(1, sources.size)
            val source = sources.first()

            assertEquals("source_test_0001", source.id)
            assertEquals(SourceType.GOVERNMENT, source.sourceType)
            assertEquals(SourceStatus.UNVERIFIED, source.status)
            assertEquals(1_700_000_000_000L, source.createdAt)
        }

    @Test
    fun invoke_defaults_status_to_unverified() =
        runBlocking {
            createSource(minimalInput())
            assertEquals(SourceStatus.UNVERIFIED, repository.snapshot().first().status)
        }

    @Test
    fun invoke_defaults_all_optional_fields_to_null() =
        runBlocking {
            createSource(minimalInput())
            val source = repository.snapshot().first()

            assertNull(source.title)
            assertNull(source.authorOrOrganisation)
            assertNull(source.publicationDate)
            assertNull(source.accessDate)
            assertNull(source.url)
            assertNull(source.geographicScope)
            assertNull(source.hemisphere)
            assertNull(source.context)
            assertNull(source.notes)
        }

    @Test
    fun invoke_preserves_all_optional_fields() =
        runBlocking {
            createSource(
                minimalInput().copy(
                    title = "Organic Gardening Handbook",
                    authorOrOrganisation = "Ministry of Agriculture",
                    publicationDate = KnownDate(LocalDate.of(2024, 6, 1)),
                    accessDate =
                        KnownDate(
                            date = LocalDate.of(2026, 9, 15),
                            time = LocalTime.of(10, 30),
                        ),
                    url = "https://example.org/handbook",
                    geographicScope = GeographicScope.COUNTRY,
                    hemisphere = Hemisphere.SOUTHERN,
                    context = "New Zealand temperate climate",
                    notes = "Chapter 4 on crop rotation",
                ),
            )
            val source = repository.snapshot().first()

            assertEquals("Organic Gardening Handbook", source.title)
            assertEquals("Ministry of Agriculture", source.authorOrOrganisation)
            assertEquals(LocalDate.of(2024, 6, 1), source.publicationDate?.date)
            assertNull(source.publicationDate?.time)
            assertEquals(LocalDate.of(2026, 9, 15), source.accessDate?.date)
            assertEquals(LocalTime.of(10, 30), source.accessDate?.time)
            assertEquals("https://example.org/handbook", source.url)
            assertEquals(GeographicScope.COUNTRY, source.geographicScope)
            assertEquals(Hemisphere.SOUTHERN, source.hemisphere)
            assertEquals("New Zealand temperate climate", source.context)
            assertEquals("Chapter 4 on crop rotation", source.notes)
        }

    @Test
    fun invoke_accepts_all_ten_source_types() =
        runBlocking {
            SourceType.entries.forEach { type ->
                createSource(minimalInput().copy(sourceType = type))
            }
            assertEquals(10, repository.snapshot().size)
        }

    @Test
    fun invoke_accepts_all_seven_statuses() =
        runBlocking {
            SourceStatus.entries.forEach { status ->
                createSource(minimalInput().copy(status = status))
            }
            assertEquals(7, repository.snapshot().size)
        }

    private fun minimalInput(): NewSource = NewSource(sourceType = SourceType.GOVERNMENT)
}
