package buildlogic.ext

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.plugins.PluginManager
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/** Applies plugins with a terse `configurePlugins { apply(...) }` block. */
internal fun Project.configurePlugins(block: PluginManager.() -> Unit) = pluginManager.block()

/** Configures the `android { }` block of an application module. */
internal fun Project.configureApplication(block: ApplicationExtension.() -> Unit) =
    extensions.configure<ApplicationExtension>(block)

/** Configures the `android { }` block of a library module. */
internal fun Project.configureLibrary(block: LibraryExtension.() -> Unit) =
    extensions.configure<LibraryExtension>(block)

/**
 * Applies the Java/Kotlin toolchain settings shared by every Android module.
 *
 * Both source/target compatibility and the Kotlin `jvmTarget` come from the single
 * `javaVersion` entry in `libs.versions.toml`, so no module ever declares them again.
 */
internal fun Project.configureJvmToolchain(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    commonExtension.compileOptions {
        sourceCompatibility = libs.javaVersion
        targetCompatibility = libs.javaVersion
    }
    configureKotlinJvmTarget()
}

/** Sets the Kotlin `jvmTarget` for every compile task in the module. */
internal fun Project.configureKotlinJvmTarget() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(libs.jvmTarget)
        }
    }
}

/**
 * Points the Compose compiler at the shared stability configuration.
 *
 * `:domain` is pure Kotlin and cannot annotate its models with `@Immutable`, so without this the
 * compiler treats every domain type crossing a module boundary as unstable.
 */
internal fun Project.configureComposeStability() {
    extensions.configure<ComposeCompilerGradlePluginExtension> {
        stabilityConfigurationFile.set(
            rootProject.layout.projectDirectory.file("config/compose/stability.conf"),
        )
    }
}

internal fun DependencyHandler.addImplementation(dependency: Any): Dependency? =
    add("implementation", dependency)

internal fun DependencyHandler.addDebugImplementation(dependency: Any): Dependency? =
    add("debugImplementation", dependency)

internal fun DependencyHandler.addTestImplementation(dependency: Any): Dependency? =
    add("testImplementation", dependency)

internal fun DependencyHandler.addAndroidTestImplementation(dependency: Any): Dependency? =
    add("androidTestImplementation", dependency)
