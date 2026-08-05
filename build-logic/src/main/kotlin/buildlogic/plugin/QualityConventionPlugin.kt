package buildlogic.plugin

import buildlogic.ext.configurePlugins
import buildlogic.ext.javaVersion
import buildlogic.ext.libs
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jlleitschuh.gradle.ktlint.KtlintExtension

/**
 * Applies and configures the static analysis stack (ktlint + detekt).
 *
 * Every module gets the same detekt ruleset from `config/detekt/detekt.yml` at the root,
 * so quality gates cannot drift per module.
 *
 * Plugin id: `devlight.quality`
 */
@Suppress("unused")
class QualityConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        configurePlugins {
            apply("org.jlleitschuh.gradle.ktlint")
            apply("io.gitlab.arturbosch.detekt")
        }

        extensions.configure<KtlintExtension> {
            // Style comes from the root `.editorconfig` (ktlint_official), not from this flag —
            // setting `android = true` would force the Android Studio style and, among other
            // things, ban the trailing commas the project uses everywhere.
            ignoreFailures.set(false)
            filter {
                // Generated sources (Room, protobuf, Compose) must not be linted.
                exclude { it.file.path.contains("/build/") }
            }
        }

        extensions.configure<DetektExtension> {
            buildUponDefaultConfig = true
            ignoreFailures = false
            config.setFrom(rootProject.file(DETEKT_CONFIG_PATH))
            baseline = file(DETEKT_BASELINE_NAME)
        }

        // Detekt otherwise inherits the JDK the daemon runs on (21), which its embedded compiler
        // does not accept. Pin it to the project's own target instead.
        tasks.withType<Detekt>().configureEach {
            jvmTarget = libs.javaVersion.toString()
        }
        tasks.withType<DetektCreateBaselineTask>().configureEach {
            jvmTarget = libs.javaVersion.toString()
        }
    }

    private companion object {
        const val DETEKT_CONFIG_PATH = "config/detekt/detekt.yml"
        const val DETEKT_BASELINE_NAME = "detekt-baseline.xml"
    }
}
