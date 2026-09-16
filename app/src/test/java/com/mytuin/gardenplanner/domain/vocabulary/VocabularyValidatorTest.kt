package com.mytuin.gardenplanner.domain.vocabulary

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests for VocabularyValidator.
 *
 * PHASE_0_PROJECT_FOUNDATION §31 (Vocabulary); DEC-040.
 * A93: pure JVM unit tests, no Android runtime.
 * A94: six cases, as specified.
 */
class VocabularyValidatorTest {

    private val allVocabularies: List<Collection<VocabularyValue>> = listOf(
        Hemisphere.entries,
        RecordStatus.entries,
        PlantLifecycle.entries,
        PlantAliasType.entries,
        GrowingSpaceType.entries,
        GeometryType.entries,
    )

    @Test
    fun every_id_of_every_vocabulary_is_accepted() {
        allVocabularies.forEach { vocabulary ->
            vocabulary.forEach { value ->
                assertTrue(
                    "expected '${value.id}' to be valid in its vocabulary",
                    VocabularyValidator.isValid(vocabulary, value.id),
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
                "vocabulary #$index contains duplicate ids: " +
                        ids.groupBy { it }.filterValues { it.size > 1 }.keys,
                ids.size == unique.size,
            )
        }
    }

    @Test
    fun every_id_is_lowercase_snake_case() {
        val pattern = Regex("^[a-z][a-z0-9]*(_[a-z0-9]+)*$")
        allVocabularies.forEach { vocabulary ->
            vocabulary.forEach { value ->
                assertTrue(
                    "id '${value.id}' does not match lowercase snake_case",
                    pattern.matches(value.id),
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
        // A88: "unknown" is a canonical value, not an error.
        assertTrue(VocabularyValidator.isValid(Hemisphere.entries, "unknown"))
    }
}