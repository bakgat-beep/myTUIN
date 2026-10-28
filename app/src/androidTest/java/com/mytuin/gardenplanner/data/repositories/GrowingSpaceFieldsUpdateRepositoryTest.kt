package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.database.GardenDatabaseFactory
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.data.entities.GrowingSpaceEntity
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.model.garden.Coordinate
import com.mytuin.gardenplanner.domain.model.garden.Geometry
import com.mytuin.gardenplanner.domain.model.garden.GrowingSpaceFields
import com.mytuin.gardenplanner.domain.repository.GrowingSpaceRepository
import com.mytuin.gardenplanner.domain.vocabulary.GeometryType
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.platform.identifiers.UuidIdGenerator
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GrowingSpaceFieldsUpdateRepositoryTest {
    private lateinit var db: GardenDatabase
    private lateinit var repository: GrowingSpaceRepository

    private val gardenId = "garden_00000000-0000-0000-0000-000000000001"
    private val spaceId = "growingspace_00000000-0000-0000-0000-000000000001"

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
                GrowingSpaceRepositoryImpl(
                    db = db,
                    growingSpaceDao = db.growingSpaceDao(),
                    growingSpaceHistoryDao = db.growingSpaceHistoryDao(),
                    idGenerator = UuidIdGenerator(),
                    gardenDao = db.gardenDao(),
                    areaDao = db.areaDao(),
                )

            db.gardenDao().insert(sampleGarden())
            db.growingSpaceDao().insert(sampleSpace())
        }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun updateFields_persists_all_mutable_fields() =
        runBlocking {
            repository.updateFields(
                id = spaceId,
                fields =
                    GrowingSpaceFields(
                        name = "Renamed bed",
                        spaceType = GrowingSpaceType.IN_GROUND_BED,
                        description = "In-ground now",
                        lengthMetres = 4.5,
                        widthMetres = 2.0,
                        heightMetres = 0.2,
                        diameterMetres = null,
                        areaSquareMetres = 9.0,
                        volumeCubicMetres = 1.8,
                        notes = "Moved",
                    ),
                updatedAt = 1_700_000_900_000L,
            )

            val retrieved = repository.getGrowingSpace(spaceId)!!
            assertEquals("Renamed bed", retrieved.name)
            assertEquals(GrowingSpaceType.IN_GROUND_BED, retrieved.spaceType)
            assertEquals("In-ground now", retrieved.description)
            assertEquals(4.5, retrieved.lengthMetres!!, 0.0)
            assertEquals(2.0, retrieved.widthMetres!!, 0.0)
            assertEquals(0.2, retrieved.heightMetres!!, 0.0)
            assertEquals(9.0, retrieved.areaSquareMetres!!, 0.0)
            assertEquals(1.8, retrieved.volumeCubicMetres!!, 0.0)
            assertEquals("Moved", retrieved.notes)
            assertEquals(1_700_000_900_000L, retrieved.updatedAt)
        }

    @Test
    fun updateFields_preserves_geometry() =
        runBlocking {
            // Pre-check the initial geometry.
            val before = repository.getGrowingSpace(spaceId)!!
            assertNotNull(before.geometry)

            repository.updateFields(
                id = spaceId,
                fields =
                    GrowingSpaceFields(
                        name = "Still has geometry",
                        spaceType = GrowingSpaceType.RAISED_BED,
                        description = null,
                        lengthMetres = null,
                        widthMetres = null,
                        heightMetres = null,
                        diameterMetres = null,
                        areaSquareMetres = null,
                        volumeCubicMetres = null,
                        notes = null,
                    ),
                updatedAt = 1_700_000_900_000L,
            )

            val after = repository.getGrowingSpace(spaceId)!!
            assertEquals(before.geometry, after.geometry)
        }

    @Test
    fun updateFields_preserves_status_and_createdAt() =
        runBlocking {
            val before = repository.getGrowingSpace(spaceId)!!
            assertEquals(RecordStatus.ACTIVE, before.status)
            assertEquals(1_700_000_000_000L, before.createdAt)

            repository.updateFields(
                id = spaceId,
                fields =
                    GrowingSpaceFields(
                        name = "Bed 2",
                        spaceType = GrowingSpaceType.RAISED_BED,
                        description = null,
                        lengthMetres = null,
                        widthMetres = null,
                        heightMetres = null,
                        diameterMetres = null,
                        areaSquareMetres = null,
                        volumeCubicMetres = null,
                        notes = null,
                    ),
                updatedAt = 1_700_000_900_000L,
            )

            val after = repository.getGrowingSpace(spaceId)!!
            assertEquals(RecordStatus.ACTIVE, after.status)
            assertEquals(1_700_000_000_000L, after.createdAt)
        }

    @Test
    fun updateFields_clears_dimensions_when_all_null() =
        runBlocking {
            repository.updateFields(
                id = spaceId,
                fields =
                    GrowingSpaceFields(
                        name = "Bed 2",
                        spaceType = GrowingSpaceType.RAISED_BED,
                        description = null,
                        lengthMetres = null,
                        widthMetres = null,
                        heightMetres = null,
                        diameterMetres = null,
                        areaSquareMetres = null,
                        volumeCubicMetres = null,
                        notes = null,
                    ),
                updatedAt = 1_700_000_900_000L,
            )

            val after = repository.getGrowingSpace(spaceId)!!
            assertEquals(null, after.lengthMetres)
            assertEquals(null, after.widthMetres)
            assertEquals(null, after.heightMetres)
        }

    @Test
    fun updateFields_throws_NotFoundError_when_space_missing() =
        runBlocking {
            try {
                repository.updateFields(
                    id = "growingspace_does_not_exist",
                    fields =
                        GrowingSpaceFields(
                            name = "x",
                            spaceType = GrowingSpaceType.RAISED_BED,
                            description = null,
                            lengthMetres = null,
                            widthMetres = null,
                            heightMetres = null,
                            diameterMetres = null,
                            areaSquareMetres = null,
                            volumeCubicMetres = null,
                            notes = null,
                        ),
                    updatedAt = 1_700_000_900_000L,
                )
                fail("Expected NotFoundError")
            } catch (expected: NotFoundError) {
                assertEquals("GrowingSpace", expected.entityType)
            }
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

    private fun sampleSpace(): GrowingSpaceEntity =
        GrowingSpaceEntity(
            id = spaceId,
            garden_id = gardenId,
            name = "Bed 2",
            space_type = GrowingSpaceType.RAISED_BED,
            status = RecordStatus.ACTIVE,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
            geometry_type = GeometryType.POLYGON,
            geometry_data =
                GeometryGeoJson.encode(
                    Geometry.Polygon(
                        listOf(
                            Coordinate(0.0, 0.0),
                            Coordinate(3.0, 0.0),
                            Coordinate(3.0, 1.0),
                            Coordinate(0.0, 0.0),
                        ),
                    ),
                ),
            length = 3.0,
            width = 1.0,
            height = null,
            diameter = null,
            area = null,
            volume = null,
            description = null,
            area_id = null,
            notes = null,
        )
}
