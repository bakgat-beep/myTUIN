package com.mytuin.gardenplanner.domain.vocabulary

import com.mytuin.gardenplanner.domain.display.ThemeMode
import com.mytuin.gardenplanner.domain.display.UnitSystem
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Tests for VocabularyValidator.
 *
 * PHASE_0_PROJECT_FOUNDATION §31 (Vocabulary); DEC-040.
 * A140=c: this is the JUnit 5 exemplar. Other src/test tests remain
 * on JUnit 4, running under the vintage engine.
 */
class VocabularyValidatorTest {
    private val allVocabularies: List<Collection<VocabularyValue>> =
        listOf(
            Hemisphere.entries,
            RecordStatus.entries,
            PlantLifecycle.entries,
            PlantAliasType.entries,
            GrowingSpaceType.entries,
            GeometryType.entries,
            GardenPriority.entries,
            GardenPreferenceKey.entries,
            GardenPlantPreferenceKind.entries,
            ThemeMode.entries,
            UnitSystem.entries,
        )

    @Test
    fun every_id_of_every_vocabulary_is_accepted() {
        allVocabularies.forEach { vocabulary ->
            vocabulary.forEach { value ->
                assertTrue(
                    VocabularyValidator.isValid(vocabulary, value.id),
                    "expected '${value.id}' to be valid in its vocabulary",
                )
            }
        }
    }

    @Test
    fun no_vocabulary_contains_duplicate_ids() {
        allVocabularies.forEachIndexed { index, vocabulary ->
            val ids = vocabulary.map { it.id }
            val unique = ids.toSet()
            assertTrue(
                ids.size == unique.size,
                "vocabulary #$index contains duplicate ids: " +
                    ids.groupBy { it }.filterValues { it.size > 1 }.keys,
            )
        }
    }

    @Test
    fun every_id_is_lowercase_snake_case() {
        val pattern = Regex("^[a-z][a-z0-9]*(_[a-z0-9]+)*$")
        allVocabularies.forEach { vocabulary ->
            vocabulary.forEach { value ->
                assertTrue(
                    pattern.matches(value.id),
                    "id '${value.id}' does not match lowercase snake_case",
                )
            }
        }
    }

    @Test
    fun validation_is_case_sensitive() {
        assertTrue(VocabularyValidator.isValid(Hemisphere.entries, "northern"))
        assertFalse(VocabularyValidator.isValid(Hemisphere.entries, "Northern"))
        assertFalse(VocabularyValidator.isValid(Hemisphere.entries, "NORTHERN"))
    }

    @Test
    fun empty_and_whitespace_strings_are_rejected() {
        allVocabularies.forEach { vocabulary ->
            assertFalse(VocabularyValidator.isValid(vocabulary, ""))
            assertFalse(VocabularyValidator.isValid(vocabulary, " "))
            assertFalse(VocabularyValidator.isValid(vocabulary, "\t"))
            assertFalse(VocabularyValidator.isValid(vocabulary, " northern "))
        }
    }

    @Test
    fun unknown_is_a_valid_id_for_hemisphere() {
        assertTrue(VocabularyValidator.isValid(Hemisphere.entries, "unknown"))
    }
}
