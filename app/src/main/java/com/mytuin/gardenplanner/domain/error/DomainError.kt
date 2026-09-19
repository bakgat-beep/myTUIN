package com.mytuin.gardenplanner.domain.error

/**
 * Structured domain errors.
 *
 * PHASE_0_PROJECT_FOUNDATION §15, §30 item 22. The base hierarchy
 * exists so use cases and the UI can reason about failure categories
 * without depending on infrastructure-specific exception types.
 *
 * V1_TECHNICAL_ARCHITECTURE §57 lists five categories: validation,
 * persistence, import, external service, unexpected. Import and
 * external service are not V1 and are not represented here.
 * NotFoundError is added as a fourth because the repository layer
 * has a genuine "asked for something that does not exist" case.
 *
 * A96=a: sealed class extending RuntimeException, so existing call
 * sites continue to work without a Result type (A25=C).
 *
 * A104=a: subclasses carry structured fields, not a localisation key
 * or a display string. A UI consumer formats from the fields.
 *
 * The [message] passed to RuntimeException is developer-readable,
 * for logs and debuggers. It is not intended for display.
 *
 * Domain layer: no Android, Compose, Room or Hilt dependencies.
 */
sealed class DomainError(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)

/**
 * A value violates a business rule.
 *
 * Produced by the repository layer when a constraint the database
 * enforces corresponds to a domain invariant, for example a foreign
 * key that references a row the domain requires.
 */
class ValidationError(
    val field: String,
    val reason: String,
) : DomainError("Validation failed on '$field': $reason")

/**
 * A requested entity does not exist.
 *
 * Produced by repository reads and updates when the id has no row.
 * A25=C: absent entities surface as exceptions, not nullability, at
 * the repository boundary. Null remains valid at use-case boundaries
 * where "nothing there" is a meaningful answer.
 */
class NotFoundError(
    val entityType: String,
    val id: String,
) : DomainError("$entityType not found: $id")

/**
 * A read or write failed at the database for a reason other than a
 * constraint violation.
 *
 * No production site throws this yet (A106=a). When one does, the
 * throw site passes a meaningful [operation] string.
 */
class PersistenceError(
    val operation: String,
    val reason: String,
    cause: Throwable? = null,
) : DomainError("Persistence operation '$operation' failed: $reason", cause)

/**
 * Anything else that reached the domain boundary.
 *
 * No production site throws this yet (A106=a).
 */
class UnexpectedError(
    val reason: String,
    cause: Throwable? = null,
) : DomainError("Unexpected error: $reason", cause)
