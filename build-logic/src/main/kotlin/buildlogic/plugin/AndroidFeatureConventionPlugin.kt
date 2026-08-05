package buildlogic.plugin

import buildlogic.ext.addImplementation
import buildlogic.ext.addTestImplementation
import buildlogic.ext.configurePlugins
import buildlogic.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

/**
 * Configures a `presentation-feature-*` module: Compose, navigation, lifecycle, Koin, plus the
 * shared presentation-core modules every screen needs.
 *
 * Because the wiring lives here, a feature module's own build script only ever declares the
 * dependencies unique to that feature (e.g. media3 for the player).
 *
 * Plugin id: `devlight.android.feature`
 */
@Suppress("unused")
class AndroidFeatureConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        configurePlugins {
            apply("devlight.android.compose")
        }

        dependencies {
            // Shared layers — a feature never reaches below :domain on its own.
            addImplementation(project(":domain"))
            addImplementation(project(":presentation-core-styling"))
            addImplementation(project(":presentation-core-platform"))
            addImplementation(project(":presentation-core-navigation"))
            addImplementation(project(":presentation-core-ui"))

            addImplementation(libs.androidx.core.ktx)
            addImplementation(libs.androidx.lifecycle.runtime.ktx)
            addImplementation(libs.androidx.lifecycle.viewmodel.compose)
            addImplementation(libs.androidx.lifecycle.runtime.compose)
            addImplementation(libs.androidx.navigation.compose)
            addImplementation(libs.bundles.koin)

            addTestImplementation(libs.koin.test)
            addTestImplementation(libs.koin.test.junit4)
        }
    }
}
