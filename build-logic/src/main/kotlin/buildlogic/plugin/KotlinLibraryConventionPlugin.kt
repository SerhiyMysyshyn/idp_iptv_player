package buildlogic.plugin

import buildlogic.ext.addImplementation
import buildlogic.ext.addTestImplementation
import buildlogic.ext.configureKotlinJvmTarget
import buildlogic.ext.configurePlugins
import buildlogic.ext.javaVersion
import buildlogic.ext.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Configures a pure-Kotlin (JVM) module with no Android dependencies.
 *
 * This is the plugin for `:domain` — models, repository contracts and use cases stay free of
 * the Android SDK so they can be unit-tested on the JVM without Robolectric.
 *
 * Plugin id: `devlight.kotlin.library`
 */
@Suppress("unused")
class KotlinLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        configurePlugins {
            apply("org.jetbrains.kotlin.jvm")
            apply("devlight.quality")
        }

        extensions.configure<JavaPluginExtension> {
            sourceCompatibility = libs.javaVersion
            targetCompatibility = libs.javaVersion
        }

        configureKotlinJvmTarget()

        dependencies {
            addImplementation(libs.kotlinx.coroutines.core)
            addTestImplementation(libs.bundles.unit.test)
        }
    }
}
