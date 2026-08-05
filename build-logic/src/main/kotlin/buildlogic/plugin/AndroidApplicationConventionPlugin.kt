package buildlogic.plugin

import buildlogic.ext.addAndroidTestImplementation
import buildlogic.ext.addDebugImplementation
import buildlogic.ext.addImplementation
import buildlogic.ext.addTestImplementation
import buildlogic.ext.configureApplication
import buildlogic.ext.configureComposeStability
import buildlogic.ext.configureJvmToolchain
import buildlogic.ext.configurePlugins
import buildlogic.ext.libs
import buildlogic.ext.versionAsInt
import buildlogic.ext.versionAsString
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Configures the `:app` module — the only Android application in the project.
 *
 * Every SDK level, the application id and the version name/code come from `libs.versions.toml`,
 * so bumping a release means editing one file.
 *
 * Plugin id: `devlight.android.application`
 */
@Suppress("unused")
class AndroidApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        configurePlugins {
            apply("com.android.application")
            apply("org.jetbrains.kotlin.android")
            apply("org.jetbrains.kotlin.plugin.compose")
            apply("devlight.quality")
        }

        configureApplication {
            compileSdk = libs.versionAsInt { compileSdk }

            defaultConfig {
                applicationId = libs.versionAsString { applicationId }
                minSdk = libs.versionAsInt { minSdk }
                targetSdk = libs.versionAsInt { targetSdk }
                versionCode = libs.versionAsInt { versionCode }
                versionName = libs.versionAsString { versionName }
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            buildTypes {
                release {
                    isMinifyEnabled = true
                    isShrinkResources = true
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "proguard-rules.pro",
                    )
                }
                debug {
                    isMinifyEnabled = false
                }
            }

            buildFeatures {
                compose = true
                // Needed for BuildConfig.DEBUG — AGP 8 no longer generates it by default.
                buildConfig = true
            }

            configureJvmToolchain(this)
        }

        configureComposeStability()

        dependencies {
            val bom = platform(libs.androidx.compose.bom)
            addImplementation(bom)
            addAndroidTestImplementation(bom)

            addImplementation(libs.androidx.core.ktx)
            addImplementation(libs.androidx.activity.compose)
            addImplementation(libs.bundles.compose)
            addImplementation(libs.bundles.koin)
            libs.bundles.compose.debug.get().forEach { addDebugImplementation(it) }

            addTestImplementation(libs.bundles.unit.test)
            addTestImplementation(libs.koin.test)
            addTestImplementation(libs.koin.test.junit4)
            addAndroidTestImplementation(libs.androidx.junit)
            addAndroidTestImplementation(libs.androidx.espresso.core)
            addAndroidTestImplementation(libs.androidx.ui.test.junit4)
        }
    }
}
