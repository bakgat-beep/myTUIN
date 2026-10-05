package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.database.GardenDatabaseFactory
import com.mytuin.gardenplanner.data.entities.ActivityEntity
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.data.entities.GrowingSpaceEntity
import com.mytuin.gardenplanner.data.entities.PlantEntity
import com.mytuin.gardenplanner.data.entities.PlantInstanceEntity
import com.mytuin.gardenplanner.domain.error.ValidationError
import com.mytuin.gardenplanner.domain.model.garden.HarvestLoss
import com.mytuin.gardenplanner.domain.model.garden.KnownDate
import com.mytuin.gardenplanner.domain.repository.HarvestLossRepository
import com.mytuin.gardenplanner.domain.vocabulary.ActivityQuantityUnit
import com.mytuin.gardenplanner.domain.vocabulary.ActivityType
import com.mytuin.gardenplanner.domain.vocabulary.GrowingSpaceType
import com.mytuin.gardenplanner.domain.vocabulary.HarvestLossCause
import com.mytuin.gardenplanner.domain.vocabulary.HarvestLossSeverity
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.PlantLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
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
class HarvestLossRepositoryTest {
    private lateinit var db: GardenDatabase
    private lateinit var repository: HarvestLossRepository

    private val gardenId = "garden_00000000-0000-0000-0000-000000000001"
    private val spaceId = "growingspace_00000000-0000-0000-0000-000000000001"
    private val plantId = "plant_00000000-0000-0000-0000-000000000001"
    private val instanceId = "plantinstance_00000000-0000-0000-0000-000000000001"
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
                HarvestLossRepositoryImpl(
                    harvestLossDao = db.harvestLossDao(),
                    gardenDao = db.gardenDao(),
                    activityDao = db.activityDao(),
                    plantInstanceDao = db.plantInstanceDao(),
                    growingSpaceDao = db.growingSpaceDao(),
                )

            db.gardenDao().insert(sampleGarden())
            db.growingSpaceDao().insert(sampleGrowingSpace())
            db.plantDao().insert(samplePlant())
            db.plantInstanceDao().insert(samplePlantInstance())
            db.activityDao().insert(sampleActivity())
        }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getHarvestLoss_returns_null_when_none_exists() =
        runBlocking {
            assertNull(repository.getHarvestLoss("harvestloss_does_not_exist"))
        }

    @Test
    fun insert_then_get_round_trips_all_fields() =
        runBlocking {
            val loss = sampleHarvestLoss()
            repository.insert(loss)

            assertEquals(loss, repository.getHarvestLoss(loss.id))
        }

    @Test
    fun insert_requires_existing_garden() =
        runBlocking {
            val orphan =
                sampleHarvestLoss().copy(
                    id = "harvestloss_00000000-0000-0000-0000-0000000000bb",
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
    fun insert_requires_existing_activity_when_supplied() =
        runBlocking {
            val orphan =
                sampleHarvestLoss().copy(
                    id = "harvestloss_00000000-0000-0000-0000-0000000000cc",
                    activityId = "activity_does_not_exist",
                )
            try {
                repository.insert(orphan)
                fail("Expected ValidationError; insert succeeded")
            } catch (expected: ValidationError) {
                assertEquals("activity_id", expected.field)
            }
        }

    @Test
    fun insert_requires_existing_plant_instance_when_supplied() =
        runBlocking {
            val orphan =
                sampleHarvestLoss().copy(
                    id = "harvestloss_00000000-0000-0000-0000-0000000000dd",
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
                sampleHarvestLoss().copy(
                    id = "harvestloss_00000000-0000-0000-0000-0000000000ee",
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
    fun insert_succeeds_without_activity_id() =
        runBlocking {
            // Standalone loss. No Activity is created or required.
            val loss = sampleHarvestLoss().copy(activityId = null)
            repository.insert(loss)

            val retrieved = repository.getHarvestLoss(loss.id)
            assertNull(retrieved?.activityId)
        }

    @Test
    fun insert_does_not_create_an_activity() =
        runBlocking {
            val standalone = sampleHarvestLoss().copy(activityId = null)
            repository.insert(standalone)

            // The pre-existing sample Activity is still the only one.
            val all = db.activityDao().getById(activityId)
            assertEquals(activityId, all?.id)
            // No new Activity was created. Verify by attempting to look
            // up an Activity id that does not correspond to the sample.
            assertNull(db.activityDao().getById("activity_does_not_exist"))
        }

    @Test
    fun cause_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(sampleHarvestLoss().copy(cause = HarvestLossCause.ROT))

            db.openHelper.readableDatabase
                .query("SELECT cause FROM harvest_loss")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("rot", cursor.getString(0))
                }
        }

    @Test
    fun severity_is_stored_as_canonical_id() =
        runBlocking {
            repository.insert(sampleHarvestLoss().copy(severity = HarvestLossSeverity.MAJOR))

            db.openHelper.readableDatabase
                .query("SELECT severity FROM harvest_loss")
                .use { cursor ->
                    assertTrue(cursor.moveToFirst())
                    assertEquals("major", cursor.getString(0))
                }
        }

    @Test
    fun date_only_known_date_round_trips() =
        runBlocking {
            val loss = sampleHarvestLoss().copy(date = KnownDate(LocalDate.of(2026, 9, 15)))
            repository.insert(loss)

            val retrieved = repository.getHarvestLoss(loss.id)!!.date
            assertEquals(LocalDate.of(2026, 9, 15), retrieved.date)
            assertNull(retrieved.time)
        }

    @Test
    fun timestamp_known_date_round_trips() =
        runBlocking {
            val loss =
                sampleHarvestLoss().copy(
                    date =
                        KnownDate(
                            date = LocalDate.of(2026, 9, 15),
                            time = LocalTime.of(7, 30),
                        ),
                )
            repository.insert(loss)

            val retrieved = repository.getHarvestLoss(loss.id)!!.date
            assertEquals(LocalDate.of(2026, 9, 15), retrieved.date)
            assertEquals(LocalTime.of(7, 30), retrieved.time)
        }

    @Test
    fun observeHarvestLossesInGarden_scopes_to_garden() =
        runBlocking {
            val otherGarden = "garden_00000000-0000-0000-0000-000000000002"
            db.gardenDao().insert(sampleGarden().copy(id = otherGarden, name = "Other Garden"))

            repository.insert(
                sampleHarvestLoss().copy(
                    id = "harvestloss_00000000-0000-0000-0000-00000000a1",
                ),
            )
            repository.insert(
                sampleHarvestLoss().copy(
                    id = "harvestloss_00000000-0000-0000-0000-00000000a2",
                    gardenId = otherGarden,
                ),
            )

            val losses = repository.observeHarvestLossesInGarden(gardenId).first()
            assertEquals(1, losses.size)
            assertEquals("harvestloss_00000000-0000-0000-0000-00000000a1", losses.first().id)
        }

    @Test
    fun observeHarvestLossesForPlantInstance_scopes_to_plant_instance() =
        runBlocking {
            val otherInstance = "plantinstance_00000000-0000-0000-0000-000000000002"
            db.plantInstanceDao().insert(
                samplePlantInstance().copy(id = otherInstance, name = "Other instance"),
            )

            repository.insert(
                sampleHarvestLoss().copy(
                    id = "harvestloss_00000000-0000-0000-0000-00000000b1",
                ),
            )
            repository.insert(
                sampleHarvestLoss().copy(
                    id = "harvestloss_00000000-0000-0000-0000-00000000b2",
                    plantInstanceId = otherInstance,
                ),
            )

            val losses = repository.observeHarvestLossesForPlantInstance(instanceId).first()
            assertEquals(1, losses.size)
        }

    @Test
    fun observeHarvestLossesInGrowingSpace_scopes_to_growing_space() =
        runBlocking {
            val otherSpace = "growingspace_00000000-0000-0000-0000-000000000002"
            db.growingSpaceDao().insert(
                sampleGrowingSpace().copy(id = otherSpace, name = "Other Bed"),
            )

            repository.insert(
                sampleHarvestLoss().copy(
                    id = "harvestloss_00000000-0000-0000-0000-00000000c1",
                ),
            )
            repository.insert(
                sampleHarvestLoss().copy(
                    id = "harvestloss_00000000-0000-0000-0000-00000000c2",
                    growingSpaceId = otherSpace,
                ),
            )

            val losses = repository.observeHarvestLossesInGrowingSpace(spaceId).first()
            assertEquals(1, losses.size)
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

    private fun sampleActivity(): ActivityEntity =
        ActivityEntity(
            id = activityId,
            garden_id = gardenId,
            activity_type = ActivityType.HARVESTING,
            occurred_at = 1_700_000_000_000L,
            created_at = 1_700_000_000_000L,
            status = RecordStatus.ACTIVE,
        )

    private fun sampleHarvestLoss(): HarvestLoss =
        HarvestLoss(
            id = "harvestloss_00000000-0000-0000-0000-000000000001",
            gardenId = gardenId,
            date = KnownDate(LocalDate.of(2026, 9, 15)),
            quantity = 3.0,
            unit = ActivityQuantityUnit.COUNT,
            createdAt = 1_700_000_000_000L,
            activityId = activityId,
            cause = HarvestLossCause.ROT,
            severity = HarvestLossSeverity.MODERATE,
            plantInstanceId = instanceId,
            growingSpaceId = spaceId,
            notes = "Three tomatoes lost to rot",
        )
}
