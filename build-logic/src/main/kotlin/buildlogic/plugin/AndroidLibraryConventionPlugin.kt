package buildlogic.plugin

import buildlogic.ext.addAndroidTestImplementation
import buildlogic.ext.addImplementation
import buildlogic.ext.addTestImplementation
import buildlogic.ext.configureJvmToolchain
import buildlogic.ext.configureLibrary
import buildlogic.ext.configurePlugins
import buildlogic.ext.libs
import buildlogic.ext.versionAsInt
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Configures a plain Android library module — no Compose, no UI.
 *
 * This is the base for data-layer modules (`:data`, `:data-preference`). Compose is deliberately
 * NOT enabled here: pulling the Compose compiler into data modules slows the build and invites
 * UI types to leak below the presentation layer.
 *
 * Plugin id: `devlight.android.library`
 */
@Suppress("unused")
class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        configurePlugins {
            apply("com.android.library")
            apply("org.jetbrains.kotlin.android")
            apply("devlight.quality")
        }

        configureLibrary {
            compileSdk = libs.versionAsInt { compileSdk }

            defaultConfig {
                minSdk = libs.versionAsInt { minSdk }
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                consumerProguardFiles("consumer-rules.pro")
            }

            buildTypes {
                release {
                    isMinifyEnabled = false
                }
            }

            configureJvmToolchain(this)
        }

        dependencies {
            addImplementation(libs.kotlinx.coroutines.core)
            addImplementation(libs.kotlinx.coroutines.android)

            addTestImplementation(libs.bundles.unit.test)
            addAndroidTestImplementation(libs.androidx.junit)
            addAndroidTestImplementation(libs.androidx.espresso.core)
        }
    }
}
