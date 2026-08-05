@file:Suppress("UnstableApiUsage")

pluginManagement {
    includeBuild("build-logic")
    enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "devlight-iptv-client"

// App — composition root, the only Android application module.
include(":app")

// Domain — pure Kotlin: models, repository contracts. Depends on nothing.
include(":domain")

// Data — implementations of the domain contracts.
include(":data")
include(":data-preference")

// Presentation core — shared building blocks, no feature knowledge.
include(":presentation-core-styling")
include(":presentation-core-platform")
include(":presentation-core-navigation")
include(":presentation-core-ui")

// Presentation features — one module per functional area.
include(":presentation-feature-home")
include(":presentation-feature-player")
include(":presentation-feature-settings")
