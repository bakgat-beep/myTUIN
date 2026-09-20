plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.detekt)
}

/**
 * Root-level formatting: settings.gradle.kts and build.gradle.kts.
 */
spotless {
    kotlinGradle {
        target("*.gradle.kts")
        ktlint(libs.versions.ktlint.get())
    }
}

/**
 * Static analysis for the root project.
 *
 * buildUponDefaultConfig = true (A204=d): the config file contains
 * overrides only. Defaults are active unless explicitly changed.
 *
 * maxIssues = 0 (A219=a): any finding fails. An acceptable finding is
 * a config exception with a stated reason, not a raised threshold.
 */
detekt {
    buildUponDefaultConfig = true
    config.setFrom(rootProject.files("config/detekt/detekt.yml"))
    baseline = file("$rootDir/config/detekt/baseline.xml")
}

tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
    jvmTarget = "17"
    reports {
        html.required.set(true)
        xml.required.set(true)
        txt.required.set(false)
        sarif.required.set(false)
        md.required.set(false)
    }
}
