package com.mytuin.gardenplanner.domain.vocabulary

/**
 * Validates a candidate string against a controlled vocabulary.
 *
 * PHASE_0_PROJECT_FOUNDATION §30 item 9, §31 (Vocabulary):
 * "A validation function exists to check a string against a given
 * vocabulary."
 *
 * DEC-040: "Validation functions exist to verify that a string is a
 * valid id in a given vocabulary, used by import and by domain
 * validation."
 *
 * A85=a, A87=a: one function, Boolean result. The fromId companion
 * method on each enum already offers a typed alternative; adding a
 * Result type here would duplicate it.
 *
 * A88: "unknown" is a valid id, not an error. This function answers
 * "is this id in the vocabulary", not "does this id represent
 * information that was supplied". Those are different questions
 * (CORE_VOCABULARIES §4).
 *
 * A90=a: case-sensitive. Canonical ids are lowercase snake_case
 * (CORE_VOCABULARIES §2.3); "Northern" is not a valid Hemisphere id.
 *
 * A91=a: no distinction between active and deprecated values yet.
 * When a vocabulary carries a deprecated value, the interface and
 * this function gain that distinction deliberately.
 *
 * A95: this function is for external input. The Room TypeConverters
 * in data/database/VocabularyConverters.kt call error(...) on unknown
 * ids read from disk — a different concern (data integrity), with a
 * different response (loud failure). Both behaviours are correct.
 *
 * Domain layer: no Android, Compose, Room or Hilt dependencies.
 */
object VocabularyValidator {

    /**
     * Returns true if [candidateId] is a canonical id of [vocabulary].
     *
     * Exact string match. No normalisation, no case folding, no
     * trimming. Empty and whitespace-only strings return false
     * because no vocabulary contains them.
     */
    fun isValid(
        vocabulary: Collection<VocabularyValue>,
        candidateId: String,
    ): Boolean = vocabulary.any { it.id == candidateId }
}