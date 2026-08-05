package buildlogic.plugin

import buildlogic.ext.addAndroidTestImplementation
import buildlogic.ext.addDebugImplementation
import buildlogic.ext.addImplementation
import buildlogic.ext.configureComposeStability
import buildlogic.ext.configureLibrary
import buildlogic.ext.configurePlugins
import buildlogic.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Adds Jetpack Compose on top of [AndroidLibraryConventionPlugin].
 *
 * Note there is no `composeOptions.kotlinCompilerExtensionVersion` here: since Kotlin 2.0 the
 * Compose compiler ships with the Kotlin plugin itself and that setting is ignored.
 *
 * Plugin id: `devlight.android.compose`
 */
@Suppress("unused")
class AndroidComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        configurePlugins {
            apply("devlight.android.library")
            apply("org.jetbrains.kotlin.plugin.compose")
        }

        configureLibrary {
            buildFeatures {
                compose = true
            }
        }

        configureComposeStability()

        dependencies {
            val bom = platform(libs.androidx.compose.bom)
            addImplementation(bom)
            addAndroidTestImplementation(bom)

            addImplementation(libs.bundles.compose)
            libs.bundles.compose.debug.get().forEach { addDebugImplementation(it) }

            addAndroidTestImplementation(libs.androidx.ui.test.junit4)
        }
    }
}
