package com.mytuin.gardenplanner.data.repositories

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mytuin.gardenplanner.data.database.GardenDatabase
import com.mytuin.gardenplanner.data.database.GardenDatabaseFactory
import com.mytuin.gardenplanner.data.entities.CultivarEntity
import com.mytuin.gardenplanner.data.entities.PlantAliasEntity
import com.mytuin.gardenplanner.data.entities.PlantEntity
import com.mytuin.gardenplanner.data.entities.PlantMultiValueEntity
import com.mytuin.gardenplanner.data.entities.PlantNoteEntity
import com.mytuin.gardenplanner.data.entities.PlantNumericFactEntity
import com.mytuin.gardenplanner.data.entities.PlantTemporalFactEntity
import com.mytuin.gardenplanner.domain.model.plant.PlantMultiValue
import com.mytuin.gardenplanner.domain.repository.PlantRepository
import com.mytuin.gardenplanner.domain.vocabulary.PlantAliasType
import com.mytuin.gardenplanner.domain.vocabulary.PlantLifecycle
import com.mytuin.gardenplanner.domain.vocabulary.PlantMultiValueKind
import com.mytuin.gardenplanner.domain.vocabulary.PlantNoteKind
import com.mytuin.gardenplanner.domain.vocabulary.PlantNumericFactKind
import com.mytuin.gardenplanner.domain.vocabulary.PlantRole
import com.mytuin.gardenplanner.domain.vocabulary.PlantTemporalFactKind
import com.mytuin.gardenplanner.domain.vocabulary.QuantityUnit
import com.mytuin.gardenplanner.domain.vocabulary.RecordStatus
import com.mytuin.gardenplanner.domain.vocabulary.Season
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

/**
 * Repository tests for the Plant read path.
 *
 * PHASE_0_PROJECT_FOUNDATION §30 item 10. TESTING_STRATEGY §7 (data/
 * repository tests against the real persistence layer, using isolated
 * test databases — §56).
 *
 * Wiring is manual rather than via Hilt. Hilt-based tests require
 * hilt-android-testing, which is deferred to §32 item 14.
 */
@RunWith(AndroidJUnit4::class)
class PlantRepositoryTest {
    private lateinit var db: GardenDatabase
    private lateinit var repository: PlantRepository

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db =
            Room
                .inMemoryDatabaseBuilder(context, GardenDatabase::class.java)
                .addCallback(GardenDatabaseFactory.foreignKeysCallback)
                .build()
        repository =
            PlantRepositoryImpl(
                plantDao = db.plantDao(),
                plantAliasDao = db.plantAliasDao(),
                cultivarDao = db.cultivarDao(),
                plantMultiValueDao = db.plantMultiValueDao(),
                plantNumericFactDao = db.plantNumericFactDao(),
                plantTemporalFactDao = db.plantTemporalFactDao(),
                plantNoteDao = db.plantNoteDao(),
            )
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getPlant_returns_null_when_no_plant_exists() =
        runBlocking {
            assertNull(repository.getPlant("plant_does_not_exist"))
        }

    @Test
    fun getPlant_maps_all_fields_from_entity_to_domain() =
        runBlocking {
            val entity = samplePlant()
            db.plantDao().insert(entity)

            val domain = repository.getPlant(entity.id)

            assertEquals(entity.id, domain?.id)
            assertEquals(entity.canonical_name, domain?.canonicalName)
            assertEquals(entity.scientific_name, domain?.scientificName)
            assertEquals(entity.genus, domain?.genus)
            assertEquals(entity.species, domain?.species)
            assertEquals(entity.family, domain?.family)
            assertEquals(entity.lifecycle, domain?.lifecycle)
            assertEquals(entity.description, domain?.description)
            assertEquals(entity.status, domain?.status)
            assertEquals(entity.created_at, domain?.createdAt)
            assertEquals(entity.updated_at, domain?.updatedAt)
        }

    @Test
    fun getPlant_preserves_null_lifecycle_distinct_from_unknown() =
        runBlocking {
            val entity =
                samplePlant().copy(
                    id = "plant_00000000-0000-0000-0000-0000000000aa",
                    lifecycle = null,
                )
            db.plantDao().insert(entity)

            val domain = repository.getPlant(entity.id)

            assertNull(domain?.lifecycle)
        }

    @Test
    fun observePlants_emits_inserted_rows_in_name_order() =
        runBlocking {
            val beta =
                samplePlant().copy(
                    id = "plant_00000000-0000-0000-0000-0000000000bb",
                    canonical_name = "Beta plant",
                )
            val alpha =
                samplePlant().copy(
                    id = "plant_00000000-0000-0000-0000-0000000000aa",
                    canonical_name = "Alpha plant",
                )
            db.plantDao().insert(beta)
            db.plantDao().insert(alpha)

            val plants = repository.observePlants().first()

            assertEquals(2, plants.size)
            assertEquals("Alpha plant", plants[0].canonicalName)
            assertEquals("Beta plant", plants[1].canonicalName)
        }

    @Test
    fun observeAliasesForPlant_returns_only_that_plants_aliases() =
        runBlocking {
            val plantA = samplePlant()
            val plantB =
                samplePlant().copy(
                    id = "plant_00000000-0000-0000-0000-0000000000bb",
                    canonical_name = "Other plant",
                )
            db.plantDao().insert(plantA)
            db.plantDao().insert(plantB)

            val aliasA =
                sampleAlias(
                    id = "plantalias_00000000-0000-0000-0000-0000000000a1",
                    plantId = plantA.id,
                    alias = "Alias for A",
                )
            val aliasB =
                sampleAlias(
                    id = "plantalias_00000000-0000-0000-0000-0000000000b1",
                    plantId = plantB.id,
                    alias = "Alias for B",
                )
            db.plantAliasDao().insert(aliasA)
            db.plantAliasDao().insert(aliasB)

            val aliases = repository.observeAliasesForPlant(plantA.id).first()

            assertEquals(1, aliases.size)
            assertEquals("Alias for A", aliases.first().alias)
            assertEquals(plantA.id, aliases.first().plantId)
        }

    @Test
    fun observeCultivarsForPlant_returns_only_that_plants_cultivars() =
        runBlocking {
            val plantA = samplePlant()
            val plantB =
                samplePlant().copy(
                    id = "plant_00000000-0000-0000-0000-0000000000bb",
                    canonical_name = "Other plant",
                )
            db.plantDao().insert(plantA)
            db.plantDao().insert(plantB)

            val cultivarA =
                sampleCultivar(
                    id = "cultivar_00000000-0000-0000-0000-0000000000a1",
                    plantId = plantA.id,
                    name = "Cultivar for A",
                )
            val cultivarB =
                sampleCultivar(
                    id = "cultivar_00000000-0000-0000-0000-0000000000b1",
                    plantId = plantB.id,
                    name = "Cultivar for B",
                )
            db.cultivarDao().insert(cultivarA)
            db.cultivarDao().insert(cultivarB)

            val cultivars = repository.observeCultivarsForPlant(plantA.id).first()

            assertEquals(1, cultivars.size)
            assertEquals("Cultivar for A", cultivars.first().name)
            assertEquals(plantA.id, cultivars.first().plantId)
        }

    @Test
    fun lifecycle_enum_id_round_trips_through_domain_layer() =
        runBlocking {
            val entity = samplePlant().copy(lifecycle = PlantLifecycle.PERENNIAL)
            db.plantDao().insert(entity)

            val domain = repository.getPlant(entity.id)

            assertEquals(PlantLifecycle.PERENNIAL, domain?.lifecycle)
            assertEquals("perennial", domain?.lifecycle?.id)
        }

    @Test
    fun alias_type_enum_id_round_trips_through_domain_layer() =
        runBlocking {
            val plant = samplePlant()
            db.plantDao().insert(plant)
            val alias =
                sampleAlias(
                    id = "plantalias_00000000-0000-0000-0000-0000000000a1",
                    plantId = plant.id,
                    aliasType = PlantAliasType.SYNONYM,
                )
            db.plantAliasDao().insert(alias)

            val aliases = repository.observeAliasesForPlant(plant.id).first()

            assertEquals(PlantAliasType.SYNONYM, aliases.first().aliasType)
            assertEquals("synonym", aliases.first().aliasType.id)
        }

    @Test
    fun observeAllMultiValuesForPlant_returns_all_kinds() =
        runBlocking {
            val plant = samplePlant()
            db.plantDao().insert(plant)
            seedMultiValue(plant.id, PlantMultiValueKind.PLANT_ROLE, "food_crop")
            seedMultiValue(plant.id, PlantMultiValueKind.PLANT_ROLE, "pollinator_support")
            seedMultiValue(plant.id, PlantMultiValueKind.GROWTH_HABIT, "climbing")
            seedMultiValue(plant.id, PlantMultiValueKind.GROWING_METHOD, "raised_bed")

            val values = repository.observeAllMultiValuesForPlant(plant.id).first()

            assertEquals(4, values.size)
            assertEquals(
                2,
                values.filterIsInstance<PlantMultiValue.Role>().size,
            )
            assertEquals(
                1,
                values.filterIsInstance<PlantMultiValue.Habit>().size,
            )
            assertEquals(
                1,
                values.filterIsInstance<PlantMultiValue.Growing>().size,
            )
        }

    @Test
    fun observeMultiValuesForPlant_filters_by_kind() =
        runBlocking {
            val plant = samplePlant()
            db.plantDao().insert(plant)
            seedMultiValue(plant.id, PlantMultiValueKind.PLANT_ROLE, "food_crop")
            seedMultiValue(plant.id, PlantMultiValueKind.PLANT_ROLE, "herb")
            seedMultiValue(plant.id, PlantMultiValueKind.GROWTH_HABIT, "climbing")

            val roles =
                repository
                    .observeMultiValuesForPlant(plant.id, PlantMultiValueKind.PLANT_ROLE)
                    .first()

            assertEquals(2, roles.size)
            assertTrue(roles.all { it is PlantMultiValue.Role })
            assertEquals(
                setOf(PlantRole.FOOD_CROP, PlantRole.HERB),
                roles.filterIsInstance<PlantMultiValue.Role>().map { it.role }.toSet(),
            )
        }

    @Test
    fun multi_value_mapper_rejects_unknown_value_for_kind() =
        runBlocking {
            val plant = samplePlant()
            db.plantDao().insert(plant)
            // Store a PlantRole id under the GROWTH_HABIT kind: a
            // data-integrity error that SQLite cannot catch but the
            // mapper must.
            db.plantMultiValueDao().upsert(
                PlantMultiValueEntity(
                    plant_id = plant.id,
                    kind = PlantMultiValueKind.GROWTH_HABIT,
                    value_id = "food_crop",
                ),
            )

            try {
                repository.observeAllMultiValuesForPlant(plant.id).first()
                fail("Expected IllegalArgumentException; mapper accepted a mismatched value")
            } catch (expected: IllegalStateException) {
                // Expected: error(...) throws IllegalStateException.
            }
        }

    private suspend fun seedMultiValue(
        plantId: String,
        kind: PlantMultiValueKind,
        valueId: String,
    ) {
        db.plantMultiValueDao().upsert(
            PlantMultiValueEntity(
                plant_id = plantId,
                kind = kind,
                value_id = valueId,
            ),
        )
    }

    private fun samplePlant(): PlantEntity =
        PlantEntity(
            id = "plant_00000000-0000-0000-0000-000000000001",
            canonical_name = "Test plant",
            scientific_name = "Testus plantus",
            genus = "Testus",
            species = "plantus",
            family = "Testaceae",
            lifecycle = PlantLifecycle.PERENNIAL,
            description = "Synthetic reference data for repository testing.",
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
            status = RecordStatus.ACTIVE,
        )

    private fun sampleAlias(
        id: String,
        plantId: String,
        alias: String = "Test alias",
        aliasType: PlantAliasType = PlantAliasType.COMMON_NAME,
    ): PlantAliasEntity =
        PlantAliasEntity(
            id = id,
            plant_id = plantId,
            alias = alias,
            alias_type = aliasType,
            language = "en",
        )

    private fun sampleCultivar(
        id: String,
        plantId: String,
        name: String = "Test cultivar",
    ): CultivarEntity =
        CultivarEntity(
            id = id,
            plant_id = plantId,
            name = name,
            description = "Synthetic cultivar for repository testing.",
            notes = null,
            created_at = 1_700_000_000_000L,
            updated_at = 1_700_000_000_000L,
            status = RecordStatus.ACTIVE,
        )

    @Test
    fun observeNumericFactsForPlant_returns_all_kinds() =
        runBlocking {
            val plant = samplePlant()
            db.plantDao().insert(plant)
            db.plantNumericFactDao().upsert(
                PlantNumericFactEntity(
                    plant_id = plant.id,
                    fact_kind = PlantNumericFactKind.PREFERRED_PH,
                    min_value = 6.0,
                    max_value = 6.8,
                    typical_value = 6.5,
                    unit = QuantityUnit.PH,
                    notes = null,
                ),
            )
            db.plantNumericFactDao().upsert(
                PlantNumericFactEntity(
                    plant_id = plant.id,
                    fact_kind = PlantNumericFactKind.DAYS_TO_MATURITY,
                    typical_value = 70.0,
                    unit = QuantityUnit.DAY,
                    notes = null,
                ),
            )

            val facts = repository.observeNumericFactsForPlant(plant.id).first()

            assertEquals(2, facts.size)
            assertEquals(
                setOf(PlantNumericFactKind.PREFERRED_PH, PlantNumericFactKind.DAYS_TO_MATURITY),
                facts.map { it.factKind }.toSet(),
            )
            val phFact = facts.first { it.factKind == PlantNumericFactKind.PREFERRED_PH }
            assertEquals(6.0, phFact.minValue!!, 0.0)
            assertEquals(6.8, phFact.maxValue!!, 0.0)
            assertEquals(6.5, phFact.typicalValue!!, 0.0)
            assertEquals(QuantityUnit.PH, phFact.unit)
        }

    @Test
    fun numeric_fact_mapper_rejects_value_less_row() =
        runBlocking {
            val plant = samplePlant()
            db.plantDao().insert(plant)
            db.plantNumericFactDao().upsert(
                PlantNumericFactEntity(
                    plant_id = plant.id,
                    fact_kind = PlantNumericFactKind.PREFERRED_PH,
                ),
            )

            try {
                repository.observeNumericFactsForPlant(plant.id).first()
                fail("Expected IllegalArgumentException; mapper accepted a value-less row")
            } catch (expected: IllegalArgumentException) {
                // Expected.
            }
        }

    @Test
    fun numeric_fact_mapper_rejects_min_greater_than_max() =
        runBlocking {
            val plant = samplePlant()
            db.plantDao().insert(plant)
            db.plantNumericFactDao().upsert(
                PlantNumericFactEntity(
                    plant_id = plant.id,
                    fact_kind = PlantNumericFactKind.PREFERRED_PH,
                    min_value = 7.0,
                    max_value = 6.0,
                ),
            )

            try {
                repository.observeNumericFactsForPlant(plant.id).first()
                fail("Expected IllegalArgumentException; mapper accepted min > max")
            } catch (expected: IllegalArgumentException) {
                // Expected.
            }
        }

    @Test
    fun observeTemporalFactsForPlant_returns_all_kinds() =
        runBlocking {
            val plant = samplePlant()
            db.plantDao().insert(plant)
            db.plantTemporalFactDao().upsert(
                PlantTemporalFactEntity(
                    plant_id = plant.id,
                    fact_kind = PlantTemporalFactKind.SOWING_PERIOD,
                    start_month = 3,
                    end_month = 5,
                    season = Season.SPRING,
                    hemisphere = null,
                    geographic_scope = null,
                    notes = null,
                ),
            )

            val facts = repository.observeTemporalFactsForPlant(plant.id).first()

            assertEquals(1, facts.size)
            val fact = facts.single()
            assertEquals(PlantTemporalFactKind.SOWING_PERIOD, fact.factKind)
            assertEquals(3, fact.startMonth)
            assertEquals(5, fact.endMonth)
            assertEquals(Season.SPRING, fact.season)
        }

    @Test
    fun observeNotesForPlant_returns_all_kinds() =
        runBlocking {
            val plant = samplePlant()
            db.plantDao().insert(plant)
            db.plantNoteDao().upsert(
                PlantNoteEntity(
                    plant_id = plant.id,
                    note_kind = PlantNoteKind.WATERING,
                    text = "Water deeply once a week in dry weather.",
                ),
            )
            db.plantNoteDao().upsert(
                PlantNoteEntity(
                    plant_id = plant.id,
                    note_kind = PlantNoteKind.STORAGE,
                    text = "Store in a cool, dark place.",
                ),
            )

            val notes = repository.observeNotesForPlant(plant.id).first()

            assertEquals(2, notes.size)
            assertEquals(
                setOf(PlantNoteKind.WATERING, PlantNoteKind.STORAGE),
                notes.map { it.noteKind }.toSet(),
            )
        }

    @Test
    fun multi_value_mapper_decodes_new_soil_kinds() =
        runBlocking {
            val plant = samplePlant()
            db.plantDao().insert(plant)
            db.plantMultiValueDao().upsert(
                PlantMultiValueEntity(
                    plant_id = plant.id,
                    kind = PlantMultiValueKind.PREFERRED_SOIL,
                    value_id = "loam",
                ),
            )
            db.plantMultiValueDao().upsert(
                PlantMultiValueEntity(
                    plant_id = plant.id,
                    kind = PlantMultiValueKind.SOIL_DRAINAGE,
                    value_id = "fast",
                ),
            )
            db.plantMultiValueDao().upsert(
                PlantMultiValueEntity(
                    plant_id = plant.id,
                    kind = PlantMultiValueKind.WATER_REQUIREMENT,
                    value_id = "high",
                ),
            )

            val values = repository.observeAllMultiValuesForPlant(plant.id).first()

            assertEquals(3, values.size)
            assertEquals(
                1,
                values.filterIsInstance<PlantMultiValue.PreferredSoil>().size,
            )
            assertEquals(
                1,
                values.filterIsInstance<PlantMultiValue.SoilDrainagePreference>().size,
            )
            assertEquals(
                1,
                values.filterIsInstance<PlantMultiValue.WaterRequirementLevel>().size,
            )
        }
}
