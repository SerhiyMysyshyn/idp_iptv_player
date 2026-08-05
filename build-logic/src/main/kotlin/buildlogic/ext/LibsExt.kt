package buildlogic.ext

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.the
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/**
 * Type-safe version catalog accessor, same as `libs` in a regular build script.
 *
 * Usage: `libs.androidx.core.ktx`
 */
internal val Project.libs: LibrariesForLibs
    get() = the<LibrariesForLibs>()

/**
 * Resolves a version from the catalog as [String].
 *
 * Usage: `libs.versionAsString { versionName }`
 */
internal fun LibrariesForLibs.versionAsString(
    block: LibrariesForLibs.VersionAccessors.() -> Provider<String>,
): String = versions.block().get()

/**
 * Resolves a version from the catalog as [Int].
 *
 * Usage: `libs.versionAsInt { compileSdk }`
 */
internal fun LibrariesForLibs.versionAsInt(
    block: LibrariesForLibs.VersionAccessors.() -> Provider<String>,
): Int = versionAsString(block).toInt()

/** The project-wide `javaVersion` from the catalog, as a [JavaVersion]. */
internal val LibrariesForLibs.javaVersion: JavaVersion
    get() = JavaVersion.toVersion(versionAsInt { javaVersion })

/** The project-wide `javaVersion` from the catalog, as a Kotlin [JvmTarget]. */
internal val LibrariesForLibs.jvmTarget: JvmTarget
    get() = JvmTarget.fromTarget(versionAsString { javaVersion })
