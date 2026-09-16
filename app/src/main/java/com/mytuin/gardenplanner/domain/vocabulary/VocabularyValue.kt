package com.mytuin.gardenplanner.domain.vocabulary

/**
 * Marker interface for canonical vocabulary values.
 *
 * A50=GeoJSON etc. DEC-040: every fixed vocabulary is a Kotlin enum
 * with an explicit canonical identifier. Implementing this interface
 * lets VocabularyValidator operate over any vocabulary without
 * reflection (A89; V1_TECHNICAL_ARCHITECTURE §7 forbids reflection
 * in the domain).
 *
 * A86=a: named VocabularyValue rather than DEC-040's illustrative
 * "VocabularyId", because "id" would suggest a value class wrapping
 * a string. This interface describes the enum, not the token.
 *
 * Domain layer: no Android, Compose, Room or Hilt dependencies.
 */
interface VocabularyValue {
    val id: String
}