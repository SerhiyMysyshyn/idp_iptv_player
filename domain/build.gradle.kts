plugins {
    id("devlight.kotlin.library")
}

// Pure Kotlin on purpose: no Android SDK, no Compose. Everything here is unit-testable on the JVM.
// Compose stability for these models is declared in config/compose/stability.conf.
