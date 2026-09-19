plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.spotless)
}

/**
 * Root-level formatting: settings.gradle.kts and build.gradle.kts.
 *
 * The app module configures its own Spotless block for Kotlin source
 * and its own Gradle files. Both projects must be configured because
 * a plugin applied in one project does not configure another.
 */
spotless {
    kotlinGradle {
        target("*.gradle.kts")
        ktlint(libs.versions.ktlint.get())
    }
}
