package com.mytuin.gardenplanner

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.database.GardenDatabaseFactory
import com.mytuin.gardenplanner.data.repositories.GardenRepositoryImpl
import com.mytuin.gardenplanner.data.repositories.GrowingSpaceHistoryRepositoryImpl
import com.mytuin.gardenplanner.data.repositories.GrowingSpaceRepositoryImpl
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.NewGarden
import com.mytuin.gardenplanner.domain.model.garden.NewGrowingSpace
import com.mytuin.gardenplanner.domain.repository.GardenRepository
import com.mytuin.gardenplanner.domain.repository.GrowingSpaceHistoryRepository
import com.mytuin.gardenplanner.domain.repository.GrowingSpaceRepository
import com.mytuin.gardenplanner.domain.usecases.garden.CreateGarden
import com.mytuin.gardenplanner.domain.usecases.garden.CreateGrowingSpace
import com.mytuin.gardenplanner.domain.usecases.garden.UpdateGrowingSpaceGeometry
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.platform.identifiers.UuidIdGenerator
import com.mytuin.gardenplanner.platform.time.SystemClock
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * The Phase 0 exit demonstration.
 *
 * PHASE_0_PROJECT_FOUNDATION §31 (Vertical slice), §36.
 * IMPLEMENTATION_PLAN §13.
 *
 * Exercises the full slice through the application layer:
 *   Create Garden → Create Growing Space → Persist → Reload →
 *   Verify history-safe edit.
 *
 * Uses a real file-backed database so "Persist → Reload" is a true
 * close-and-reopen cycle (A74, A76=a), not an in-memory fiction.
 *
 * Uses the production IdGenerator and Clock rather than fakes, so the
 * slice exercises the same code paths a real device would (A77).
 * Assertions therefore do not fix exact ids or timestamps; they assert
 * shape and ordering.
 */
@RunWith(AndroidJUnit4::class)
class VerticalSliceTest {
    private val dbName = "vertical-slice-test.db"

    private lateinit var context: Context
    private lateinit var db: GardenDatabase

    private val idGenerator = UuidIdGenerator()
    private val clock = SystemClock()

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        context.deleteDatabase(dbName)
        db = GardenDatabaseFactory.create(context, dbName)
    }

    @After
    fun tearDown() {
        db.close()
        context.deleteDatabase(dbName)
    }

    @Test
    fun vertical_slice_create_garden_create_space_persist_reload_history_safe_edit() =
        runBlocking {
            // --- Phase 1: Create Garden through the use case.

            val gardenRepository: GardenRepository = GardenRepositoryImpl(db.gardenDao())
            val createGarden =
                CreateGarden(
                    repository = gardenRepository,
                    idGenerator = idGenerator,
                    clock = clock,
                )

            val gardenId =
                createGarden(
                    NewGarden(
                        name = "Vertical Slice Garden",
                        countryCode = "NZ",
                        hemisphere = Hemisphere.SOUTHERN,
                    ),
                )
            assertTrue(
                "garden id must use the garden_ prefix",
                gardenId.startsWith("garden_"),
            )

            // --- Phase 2: Create Growing Space with an initial geometry.

            val growingSpaceRepository: GrowingSpaceRepository =
                GrowingSpaceRepositoryImpl(
                    db = db,
                    growingSpaceDao = db.growingSpaceDao(),
                    growingSpaceHistoryDao = db.growingSpaceHistoryDao(),
                    idGenerator = idGenerator,
                )
            val createGrowingSpace =
                CreateGrowingSpace(
                    repository = growingSpaceRepository,
                    idGenerator = idGenerator,
                    clock = clock,
                )

            val initialGeometry =
                Geometry.Polygon(
                    listOf(
                        Coordinate(0.0, 0.0),
                        Coordinate(3.0, 0.0),
                        Coordinate(3.0, 1.0),
                        Coordinate(0.0, 1.0),
                        Coordinate(0.0, 0.0),
                    ),
                )

            val spaceId =
                createGrowingSpace(
                    NewGrowingSpace(
                        gardenId = gardenId,
                        name = "Bed 2",
                        spaceType = GrowingSpaceType.RAISED_BED,
                        geometry = initialGeometry,
                        lengthMetres = 3.0,
                        widthMetres = 1.0,
                    ),
                )
            assertTrue(
                "growing space id must use the growingspace_ prefix",
                spaceId.startsWith("growingspace_"),
            )

            // Capture the space's initial created_at before reload, so
            // we can assert the history row's valid_from matches it.
            val spaceBeforeReload = growingSpaceRepository.getGrowingSpace(spaceId)
            assertNotNull(spaceBeforeReload)
            val initialCreatedAt = spaceBeforeReload!!.createdAt

            // --- Phase 3: Persist → Reload (real close-and-reopen).

            db.close()
            db = GardenDatabaseFactory.create(context, dbName)

            // Rebuild repositories against the reopened database.
            val reloadedGardenRepository: GardenRepository =
                GardenRepositoryImpl(db.gardenDao())
            val reloadedSpaceRepository: GrowingSpaceRepository =
                GrowingSpaceRepositoryImpl(
                    db = db,
                    growingSpaceDao = db.growingSpaceDao(),
                    growingSpaceHistoryDao = db.growingSpaceHistoryDao(),
                    idGenerator = idGenerator,
                )
            val historyRepository: GrowingSpaceHistoryRepository =
                GrowingSpaceHistoryRepositoryImpl(
                    growingSpaceHistoryDao = db.growingSpaceHistoryDao(),
                )

            // --- Phase 4: Verify data survived the reload.

            val garden = reloadedGardenRepository.getGarden(gardenId)
            assertNotNull("garden must survive reload", garden)
            assertEquals("Vertical Slice Garden", garden!!.name)

            val space = reloadedSpaceRepository.getGrowingSpace(spaceId)
            assertNotNull("growing space must survive reload", space)
            assertEquals("Bed 2", space!!.name)
            assertEquals(initialGeometry, space.geometry)

            // No history yet — history rows are only written on edit.
            assertEquals(0, historyRepository.getHistoryForSpace(spaceId).size)

            // --- Phase 5: History-safe edit.

            val newGeometry =
                Geometry.Polygon(
                    listOf(
                        Coordinate(0.0, 0.0),
                        Coordinate(4.0, 0.0),
                        Coordinate(4.0, 1.0),
                        Coordinate(0.0, 1.0),
                        Coordinate(0.0, 0.0),
                    ),
                )
            val updateGeometry =
                UpdateGrowingSpaceGeometry(
                    repository = reloadedSpaceRepository,
                    clock = clock,
                )

            updateGeometry(
                growingSpaceId = spaceId,
                newGeometry = newGeometry,
                reason = "Widened bed",
            )

            // --- Phase 6: Verify history-safe edit.

            val spaceAfterEdit = reloadedSpaceRepository.getGrowingSpace(spaceId)
            assertNotNull(spaceAfterEdit)
            assertEquals(
                "current geometry must reflect the edit",
                newGeometry,
                spaceAfterEdit!!.geometry,
            )

            val history = historyRepository.getHistoryForSpace(spaceId)
            assertEquals(
                "exactly one history row must exist after the first edit",
                1,
                history.size,
            )
            val historyRow = history.first()
            assertEquals(
                "history row must capture the prior geometry",
                initialGeometry,
                historyRow.geometry,
            )
            assertEquals(
                "history valid_from must equal the space's initial created_at",
                initialCreatedAt,
                historyRow.validFrom,
            )
            assertTrue(
                "history valid_to must be at or after valid_from",
                historyRow.validTo >= historyRow.validFrom,
            )
            assertEquals(
                "recorded_at must equal valid_to for an append-only edit",
                historyRow.validTo,
                historyRow.recordedAt,
            )
            assertEquals("Widened bed", historyRow.reason)

            // --- Phase 7: Persist the edit and reload once more, to
            // confirm the history and current state both survive a
            // second close-and-reopen cycle.

            db.close()
            db = GardenDatabaseFactory.create(context, dbName)

            val spaceAfterSecondReload =
                GrowingSpaceRepositoryImpl(
                    db = db,
                    growingSpaceDao = db.growingSpaceDao(),
                    growingSpaceHistoryDao = db.growingSpaceHistoryDao(),
                    idGenerator = idGenerator,
                ).getGrowingSpace(spaceId)

            val historyAfterSecondReload =
                GrowingSpaceHistoryRepositoryImpl(
                    growingSpaceHistoryDao = db.growingSpaceHistoryDao(),
                ).getHistoryForSpace(spaceId)

            assertEquals(newGeometry, spaceAfterSecondReload?.geometry)
            assertEquals(1, historyAfterSecondReload.size)
            assertEquals(initialGeometry, historyAfterSecondReload.first().geometry)
        }
}
