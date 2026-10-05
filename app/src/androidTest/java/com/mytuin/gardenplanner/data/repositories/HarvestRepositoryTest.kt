package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.database.GardenDatabaseFactory
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.data.entities.GrowingSpaceEntity
import com.mytuin.gardenplanner.data.entities.PlantEntity
import com.mytuin.gardenplanner.data.entities.PlantInstanceEntity
import com.mytuin.gardenplanner.domain.error.NotFoundError
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.Harvest
import com.mytuin.gardenplanner.domain.model.garden.KnownDate
import com.mytuin.gardenplanner.domain.repository.HarvestRepository
import com.mytuin.gardenplanner.domain.vocabulary.ActivityQuantityUnit
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.HarvestSizeCategory
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.PlantLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HarvestRepositoryTest {
    private lateinit var db: GardenDatabase
    private lateinit var repository: HarvestRepository

    private val gardenId = "garden_00000000-0000-0000-0000-000000000001"
    private val spaceId = "growingspace_00000000-0000-0000-0000-000000000001"
    private val plantId = "plant_00000000-0000-0000-0000-000000000001"
    private val instanceId = "plantinstance_00000000-0000-0000-0000-000000000001"
    private val harvestId = "harvest_00000000-0000-0000-0000-000000000001"
    private val activityId = "activity_00000000-0000-0000-0000-000000000001"

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
                HarvestRepositoryImpl(
                    db = db,
                    harvestDao = db.harvestDao(),
                    activityDao = db.activityDao(),
                    gardenDao = db.gardenDao(),
                    plantInstanceDao = db.plantInstanceDao(),
                    growingSpaceDao = db.growingSpaceDao(),
                )

            db.gardenDao().insert(sampleGarden())
            db.growingSpaceDao().insert(sampleGrowingSpace())
            db.plantDao().insert(samplePlant())
            db.plantInstanceDao().insert(samplePlantInstance())
        }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getHarvest_returns_null_when_no_harvest_exists() =
        runBlocking {
            assertNull(repository.getHarvest("harvest_does_not_exist"))
        }

    @Test
    fun insert_then_get_round_trips_all_fields() =
        runBlocking {
            val harvest = sampleHarvest()
            repository.insert(harvest)

            assertEquals(harvest, repository.getHarvest(harvest.id))
        }

    @Test
    fun insert_writes_the_linked_activity() =
        runBlocking {
            repository.insert(sampleHarvest())

            val activity = db.activityDao().getById(activityId)
            assertNotNull("linked activity must be created", activity)
            assertEquals(ActivityType.HARVESTING, activity!!.activity_type)
            assertEquals(RecordStatus.ACTIVE, activity.status)
            assertEquals(gardenId, activity.garden_id)
            assertEquals(instanceId, activity.plant_instance_id)
            assertEquals(spaceId, activity.growing_space_id)
            assertEquals(5.0, activity.quantity!!, 0.0)
            assertEquals(ActivityQuantityUnit.COUNT, activity.unit)
        }

    @Test
    fun date_only_harvest_normalizes_activity_occurred_at_to_utc_midnight() =
        runBlocking {
            val harvest =
                sampleHarvest().copy(
                    date = KnownDate(LocalDate.of(2026, 9, 15)),
                )
            repository.insert(harvest)

            val activity = db.activityDao().getById(activityId)!!
            val expectedMillis =
                LocalDate
                    .of(2026, 9, 15)
                    .atStartOfDay(ZoneOffset.UTC)
                    .toInstant()
                    .toEpochMilli()
            assertEquals(expectedMillis, activity.occurred_at)
        }

    @Test
    fun timestamped_harvest_uses_exact_time_for_activity_occurred_at() =
        runBlocking {
            val harvest =
                sampleHarvest().copy(
                    date =
                        KnownDate(
                            date = LocalDate.of(2026, 9, 15),
                            time = LocalTime.of(9, 30),
                        ),
                )
            repository.insert(harvest)

            val activity = db.activityDao().getById(activityId)!!
            val expectedMillis =
                LocalDate
                    .of(2026, 9, 15)
                    .atTime(9, 30)
                    .atZone(ZoneOffset.UTC)
                    .toInstant()
                    .toEpochMilli()
            assertEquals(expectedMillis, activity.occurred_at)
        }

    @Test
    fun insert_requires_existing_garden() =
        runBlocking {
            val orphan =
                sampleHarvest().copy(
                    id = "harvest_00000000-0000-0000-0000-0000000000bb",
                    activityId = "activity_00000000-0000-0000-0000-0000000000bb",
                    gardenId = "garden_does_not_exist",
                )
            try {
                repository.insert(orphan)
                fail("Expected ValidationError; insert succeeded")
            } catch (expected: ValidationError) {
                assertEquals("garden_id", expected.field)
            }
        }

    @Test
    fun insert_requires_existing_plant_instance_when_supplied() =
        runBlocking {
            val orphan =
                sampleHarvest().copy(
                    id = "harvest_00000000-0000-0000-0000-0000000000cc",
                    activityId = "activity_00000000-0000-0000-0000-0000000000cc",
                    plantInstanceId = "plantinstance_does_not_exist",
                )
            try {
                repository.insert(orphan)
                fail("Expected ValidationError; insert succeeded")
            } catch (expected: ValidationError) {
                assertEquals("plant_instance_id", expected.field)
            }
        }

    @Test
    fun insert_requires_existing_growing_space_when_supplied() =
        runBlocking {
            val orphan =
                sampleHarvest().copy(
                    id = "harvest_00000000-0000-0000-0000-0000000000dd",
                    activityId = "activity_00000000-0000-0000-0000-0000000000dd",
                    growingSpaceId = "growingspace_does_not_exist",
                )
            try {
                repository.insert(orphan)
                fail("Expected ValidationError; insert succeeded")
            } catch (expected: ValidationError) {
                assertEquals("growing_space_id", expected.field)
            }
        }

    @Test
    fun size_category_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(sampleHarvest().copy(sizeCategory = HarvestSizeCategory.LARGE))

            db.openHelper.readableDatabase
                .query("SELECT size_category FROM harvest")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("large", cursor.getString(0))
                }
        }

    @Test
    fun unit_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(sampleHarvest().copy(unit = ActivityQuantityUnit.KILOGRAM))

            db.openHelper.readableDatabase
                .query("SELECT unit FROM harvest")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("kilogram", cursor.getString(0))
                }
        }

    @Test
    fun archive_cascades_to_the_linked_activity() =
        runBlocking {
            val harvest = sampleHarvest()
            repository.insert(harvest)

            repository.archive(harvest.id, 1_700_000_500_000L)

            val activity = db.activityDao().getById(activityId)
            assertEquals(RecordStatus.ARCHIVED, activity?.status)
        }

    @Test
    fun archived_harvest_is_excluded_from_default_observation_query() =
        runBlocking {
            val harvest = sampleHarvest()
            repository.insert(harvest)
            repository.archive(harvest.id, 1_700_000_500_000L)

            assertEquals(
                0,
                repository.observeHarvestsInGarden(gardenId).first().size,
            )
            assertEquals(
                1,
                repository.observeHarvestsInGarden(gardenId, includeArchived = true).first().size,
            )
        }

    @Test
    fun observeHarvestsInGarden_scopes_to_garden_and_excludes_archived() =
        runBlocking {
            val otherGarden = "garden_00000000-0000-0000-0000-000000000002"
            db.gardenDao().insert(sampleGarden().copy(id = otherGarden, name = "Other Garden"))

            repository.insert(
                sampleHarvest().copy(
                    id = "harvest_00000000-0000-0000-0000-00000000a1",
                    activityId = "activity_00000000-0000-0000-0000-00000000a1",
                ),
            )
            repository.insert(
                sampleHarvest().copy(
                    id = "harvest_00000000-0000-0000-0000-00000000a2",
                    activityId = "activity_00000000-0000-0000-0000-00000000a2",
                ),
            )
            repository.insert(
                sampleHarvest().copy(
                    id = "harvest_00000000-0000-0000-0000-00000000a3",
                    activityId = "activity_00000000-0000-0000-0000-00000000a3",
                    gardenId = otherGarden,
                ),
            )
            repository.archive("harvest_00000000-0000-0000-0000-00000000a2", 1_700_000_500_000L)

            assertEquals(
                1,
                repository.observeHarvestsInGarden(gardenId).first().size,
            )
            assertEquals(
                2,
                repository.observeHarvestsInGarden(gardenId, includeArchived = true).first().size,
            )
        }

    @Test
    fun observeHarvestsForPlantInstance_scopes_to_plant_instance() =
        runBlocking {
            val otherInstance = "plantinstance_00000000-0000-0000-0000-000000000002"
            db.plantInstanceDao().insert(
                samplePlantInstance().copy(id = otherInstance, name = "Other instance"),
            )

            repository.insert(
                sampleHarvest().copy(
                    id = "harvest_00000000-0000-0000-0000-00000000b1",
                    activityId = "activity_00000000-0000-0000-0000-00000000b1",
                ),
            )
            repository.insert(
                sampleHarvest().copy(
                    id = "harvest_00000000-0000-0000-0000-00000000b2",
                    activityId = "activity_00000000-0000-0000-0000-00000000b2",
                    plantInstanceId = otherInstance,
                ),
            )

            val harvests = repository.observeHarvestsForPlantInstance(instanceId).first()
            assertEquals(1, harvests.size)
            assertEquals("harvest_00000000-0000-0000-0000-00000000b1", harvests.first().id)
        }

    @Test
    fun archive_throws_NotFoundError_when_harvest_missing() =
        runBlocking {
            try {
                repository.archive("harvest_does_not_exist", 1_700_000_500_000L)
                fail("Expected NotFoundError")
            } catch (expected: NotFoundError) {
                assertEquals("Harvest", expected.entityType)
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

    private fun sampleGrowingSpace(): GrowingSpaceEntity =
        GrowingSpaceEntity(
            id = spaceId,
            garden_id = gardenId,
            name = "Bed 2",
            space_type = GrowingSpaceType.RAISED_BED,
            status = RecordStatus.ACTIVE,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
            geometry_type = null,
            geometry_data = null,
            length = null,
            width = null,
            height = null,
            diameter = null,
            area = null,
            volume = null,
            description = null,
            area_id = null,
            notes = null,
        )

    private fun samplePlant(): PlantEntity =
        PlantEntity(
            id = plantId,
            canonical_name = "Test plant",
            scientific_name = null,
            genus = null,
            species = null,
            family = null,
            lifecycle = PlantLifecycle.PERENNIAL,
            description = null,
            status = RecordStatus.ACTIVE,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
        )

    private fun samplePlantInstance(): PlantInstanceEntity =
        PlantInstanceEntity(
            id = instanceId,
            garden_id = gardenId,
            plant_id = plantId,
            status = RecordStatus.ACTIVE,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
        )

    private fun sampleHarvest(): Harvest =
        Harvest(
            id = harvestId,
            activityId = activityId,
            gardenId = gardenId,
            date = KnownDate(LocalDate.of(2026, 9, 15)),
            quantity = 5.0,
            unit = ActivityQuantityUnit.COUNT,
            createdAt = 1_700_000_000_000L,
            sizeCategory = HarvestSizeCategory.REGULAR,
            plantInstanceId = instanceId,
            growingSpaceId = spaceId,
            notes = "First pick of the season",
        )
}
