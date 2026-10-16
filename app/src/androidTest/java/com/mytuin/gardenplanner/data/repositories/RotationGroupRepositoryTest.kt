package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.database.GardenDatabaseFactory
import com.mytuin.gardenplanner.data.entities.GardenEntity
import com.mytuin.gardenplanner.data.entities.RotationGroupEntity
import com.mytuin.gardenplanner.domain.repository.RotationGroupRepository
import com.mytuin.gardenplanner.domain.vocabulary.Hemisphere
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.domain.vocabulary.RotationGroupType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RotationGroupRepositoryTest {
    private lateinit var db: GardenDatabase
    private lateinit var repository: RotationGroupRepository

    private val gardenId = "garden_00000000-0000-0000-0000-000000000001"

    @Before
    fun setUp() =
        runBlocking {
            val context = ApplicationProvider.getApplicationContext<Context>()
            db =
                Room
                    .inMemoryDatabaseBuilder(context, GardenDatabase::class.java)
                    .addCallback(GardenDatabaseFactory.foreignKeysCallback)
                    .build()
            repository = RotationGroupRepositoryImpl(rotationGroupDao = db.rotationGroupDao())

            db.gardenDao().insert(sampleGarden())
        }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getGroup_returns_null_when_none_exists() =
        runBlocking {
            assertNull(repository.getGroup("rotationgroup_does_not_exist"))
        }

    @Test
    fun observeSystemGroups_returns_only_null_garden_ones() =
        runBlocking {
            db.rotationGroupDao().insert(systemGroup())
            db.rotationGroupDao().insert(userDefinedGroup())

            val groups = repository.observeSystemGroups().first()

            assertEquals(1, groups.size)
            assertEquals(RotationGroupType.SYSTEM, groups.single().groupType)
            assertNull(groups.single().gardenId)
        }

    @Test
    fun observeGroupsForGarden_returns_only_that_gardens_ones() =
        runBlocking {
            db.rotationGroupDao().insert(systemGroup())
            db.rotationGroupDao().insert(userDefinedGroup())

            val groups = repository.observeGroupsForGarden(gardenId).first()

            assertEquals(1, groups.size)
            assertEquals(RotationGroupType.USER_DEFINED, groups.single().groupType)
            assertEquals(gardenId, groups.single().gardenId)
        }

    @Test
    fun observeAvailableGroupsForGarden_returns_both() =
        runBlocking {
            db.rotationGroupDao().insert(systemGroup())
            db.rotationGroupDao().insert(userDefinedGroup())

            val groups = repository.observeAvailableGroupsForGarden(gardenId).first()

            assertEquals(2, groups.size)
            assertEquals(
                setOf(RotationGroupType.SYSTEM, RotationGroupType.USER_DEFINED),
                groups.map { it.groupType }.toSet(),
            )
        }

    @Test
    fun system_group_with_null_garden_id_round_trips() =
        runBlocking {
            val group = systemGroup()
            db.rotationGroupDao().insert(group)

            val retrieved = repository.getGroup(group.id)!!
            assertEquals(RotationGroupType.SYSTEM, retrieved.groupType)
            assertNull(retrieved.gardenId)
            assertEquals("Solanaceae", retrieved.name)
        }

    @Test
    fun user_defined_group_with_garden_id_round_trips() =
        runBlocking {
            val group = userDefinedGroup()
            db.rotationGroupDao().insert(group)

            val retrieved = repository.getGroup(group.id)!!
            assertEquals(RotationGroupType.USER_DEFINED, retrieved.groupType)
            assertEquals(gardenId, retrieved.gardenId)
        }

    @Test
    fun mapper_rejects_system_group_with_garden_id() =
        runBlocking {
            // Construct an inconsistent row: SYSTEM group with a
            // non-null garden_id. The row must reach the mapper to
            // be validated. observeSystemGroups filters on
            // garden_id IS NULL, which excludes this row, so we use
            // observeAvailableGroupsForGarden, whose predicate
            // matches garden_id = :gardenId.
            db.rotationGroupDao().insert(
                systemGroup().copy(garden_id = gardenId),
            )

            try {
                repository.observeAvailableGroupsForGarden(gardenId).first()
                fail("Expected IllegalArgumentException; mapper accepted inconsistent row")
            } catch (expected: IllegalArgumentException) {
                // Expected.
            }
        }

    @Test
    fun mapper_rejects_user_defined_group_with_null_garden_id() =
        runBlocking {
            // Construct an inconsistent row: USER_DEFINED group with
            // a null garden_id. The row must reach the mapper.
            // observeGroupsForGarden filters on garden_id = :gardenId,
            // which excludes this row; observeAvailableGroupsForGarden
            // matches garden_id IS NULL and returns it.
            db.rotationGroupDao().insert(
                userDefinedGroup().copy(garden_id = null),
            )

            try {
                repository.observeAvailableGroupsForGarden(gardenId).first()
                fail("Expected IllegalArgumentException; mapper accepted inconsistent row")
            } catch (expected: IllegalArgumentException) {
                // Expected.
            }
        }

    @Test
    fun insert_requires_existing_garden_when_garden_id_supplied() =
        runBlocking {
            try {
                db.rotationGroupDao().insert(
                    userDefinedGroup().copy(garden_id = "garden_does_not_exist"),
                )
                fail("Expected FK violation; insert succeeded")
            } catch (expected: android.database.sqlite.SQLiteConstraintException) {
                // Expected.
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

    private fun systemGroup(): RotationGroupEntity =
        RotationGroupEntity(
            id = "rotationgroup_00000000-0000-0000-0000-000000000001",
            name = "Solanaceae",
            group_type = RotationGroupType.SYSTEM,
            status = RecordStatus.ACTIVE,
            garden_id = null,
            description = "Nightshade family",
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
        )

    private fun userDefinedGroup(): RotationGroupEntity =
        RotationGroupEntity(
            id = "rotationgroup_00000000-0000-0000-0000-000000000002",
            name = "My rotation group",
            group_type = RotationGroupType.USER_DEFINED,
            status = RecordStatus.ACTIVE,
            garden_id = gardenId,
            description = null,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
        )
}
