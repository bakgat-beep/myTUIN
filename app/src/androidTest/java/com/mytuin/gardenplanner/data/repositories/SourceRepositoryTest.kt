package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.database.GardenDatabaseFactory
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.KnownDate
import com.mytuin.gardenplanner.domain.model.garden.Source
import com.mytuin.gardenplanner.domain.repository.SourceRepository
import com.mytuin.gardenplanner.domain.vocabulary.GeographicScope
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.SourceStatus
import com.mytuin.gardenplanner.domain.vocabulary.SourceType
import java.time.LocalDate
import java.time.LocalTime
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SourceRepositoryTest {
    private lateinit var db: GardenDatabase
    private lateinit var repository: SourceRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db =
            Room
                .inMemoryDatabaseBuilder(context, GardenDatabase::class.java)
                .addCallback(GardenDatabaseFactory.foreignKeysCallback)
                .build()
        repository = SourceRepositoryImpl(sourceDao = db.sourceDao())
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getSource_returns_null_when_none_exists() =
        runBlocking {
            assertNull(repository.getSource("source_does_not_exist"))
        }

    @Test
    fun insert_then_get_round_trips_all_fields() =
        runBlocking {
            val source = sampleSource()
            repository.insert(source)

            assertEquals(source, repository.getSource(source.id))
        }

    @Test
    fun insert_with_all_optional_fields_null_round_trips() =
        runBlocking {
            val minimal =
                sampleSource().copy(
                    title = null,
                    authorOrOrganisation = null,
                    publicationDate = null,
                    accessDate = null,
                    url = null,
                    geographicScope = null,
                    hemisphere = null,
                    context = null,
                    notes = null,
                )
            repository.insert(minimal)

            val retrieved = repository.getSource(minimal.id)!!
            assertNull(retrieved.title)
            assertNull(retrieved.authorOrOrganisation)
            assertNull(retrieved.publicationDate)
            assertNull(retrieved.accessDate)
            assertNull(retrieved.url)
            assertNull(retrieved.geographicScope)
            assertNull(retrieved.hemisphere)
            assertNull(retrieved.context)
            assertNull(retrieved.notes)
        }

    @Test
    fun source_type_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(sampleSource().copy(sourceType = SourceType.EXTENSION_SERVICE))

            db.openHelper.readableDatabase
                .query("SELECT source_type FROM source")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("extension_service", cursor.getString(0))
                }
        }

    @Test
    fun status_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(sampleSource().copy(status = SourceStatus.VERIFIED))

            db.openHelper.readableDatabase
                .query("SELECT status FROM source")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("verified", cursor.getString(0))
                }
        }

    @Test
    fun publication_date_only_round_trips() =
        runBlocking {
            val source =
                sampleSource().copy(
                    publicationDate = KnownDate(LocalDate.of(2024, 6, 1)),
                    accessDate = null,
                )
            repository.insert(source)

            val retrieved = repository.getSource(source.id)!!.publicationDate
            assertEquals(LocalDate.of(2024, 6, 1), retrieved?.date)
            assertNull(retrieved?.time)
        }

    @Test
    fun access_date_with_time_round_trips() =
        runBlocking {
            val source =
                sampleSource().copy(
                    publicationDate = null,
                    accessDate =
                        KnownDate(
                            date = LocalDate.of(2026, 9, 15),
                            time = LocalTime.of(10, 30, 45),
                        ),
                )
            repository.insert(source)

            val retrieved = repository.getSource(source.id)!!.accessDate
            assertEquals(LocalDate.of(2026, 9, 15), retrieved?.date)
            assertEquals(LocalTime.of(10, 30, 45), retrieved?.time)
        }

    @Test
    fun updateStatus_changes_the_status() =
        runBlocking {
            val source = sampleSource()
            repository.insert(source)

            repository.updateStatus(source.id, SourceStatus.OUTDATED)

            assertEquals(SourceStatus.OUTDATED, repository.getSource(source.id)?.status)
        }

    @Test
    fun updateStatus_throws_NotFoundError_when_source_missing() =
        runBlocking {
            try {
                repository.updateStatus("source_does_not_exist", SourceStatus.VERIFIED)
                fail("Expected NotFoundError")
            } catch (expected: NotFoundError) {
                assertEquals("Source", expected.entityType)
            }
        }

    @Test
    fun observeAllSources_returns_inserted_sources() =
        runBlocking {
            repository.insert(
                sampleSource().copy(id = "source_00000000-0000-0000-0000-0000000000aa"),
            )
            repository.insert(
                sampleSource().copy(id = "source_00000000-0000-0000-0000-0000000000bb"),
            )

            assertEquals(2, repository.observeAllSources().first().size)
        }

    @Test
    fun hemisphere_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(sampleSource().copy(hemisphere = Hemisphere.SOUTHERN))

            db.openHelper.readableDatabase
                .query("SELECT hemisphere FROM source")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("southern", cursor.getString(0))
                }
        }

    @Test
    fun geographic_scope_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(sampleSource().copy(geographicScope = GeographicScope.CLIMATE_ZONE))

            db.openHelper.readableDatabase
                .query("SELECT geographic_scope FROM source")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("climate_zone", cursor.getString(0))
                }
        }

    private fun sampleSource(): Source =
        Source(
            id = "source_00000000-0000-0000-0000-000000000001",
            sourceType = SourceType.GOVERNMENT,
            status = SourceStatus.UNVERIFIED,
            createdAt = 1_700_000_000_000L,
            title = "Organic Gardening Handbook",
            authorOrOrganisation = "Ministry of Agriculture",
            publicationDate = KnownDate(LocalDate.of(2024, 6, 1)),
            accessDate = KnownDate(LocalDate.of(2026, 9, 15)),
            url = "https://example.org/handbook",
            geographicScope = GeographicScope.COUNTRY,
            hemisphere = Hemisphere.SOUTHERN,
            context = "New Zealand temperate climate",
            notes = "Chapter 4 on crop rotation",
        )
}
