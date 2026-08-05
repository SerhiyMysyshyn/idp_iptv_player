// Top-level build file. Plugins are declared here only to put them on the root classpath;
// the actual configuration lives in the `devlight.*` convention plugins in `build-logic`.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.protobuf) apply false
}

/**
 * Installs a pre-commit hook that runs the static analysis gates before every commit.
 *
 * Run once per clone: `./gradlew setupPreCommitHook`
 */
tasks.register("setupPreCommitHook") {
    group = "git hooks"
    description = "Installs a pre-commit Git hook that runs ktlint and detekt"

    val hooksDir = rootProject.layout.projectDirectory.dir(".git/hooks").asFile

    doLast {
        if (!hooksDir.isDirectory) {
            logger.warn(".git/hooks does not exist — is this a Git repository?")
            return@doLast
        }

        val preCommit = hooksDir.resolve("pre-commit")
        preCommit.writeText(
            """
            #!/bin/sh
            echo "Running lint checks..."
            ./gradlew ktlintCheck detekt || {
                echo "Code style checks failed. Commit aborted."
                exit 1
            }
            exit 0
            """.trimIndent() + "\n",
        )
        preCommit.setExecutable(true)

        logger.lifecycle("Pre-commit hook installed at ${preCommit.absolutePath}")
    }
}
