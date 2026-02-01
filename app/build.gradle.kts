import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

plugins {
    id("devlight.android.application")
}

android {
    namespace = "com.serhiimysyshyn.devlightiptvclient"

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

detekt {
    toolVersion = "1.23.0"
    config = files("detekt-config.yml")
    buildUponDefaultConfig = true
}

dependencies {
    implementation(libs.koin.android)
    implementation(libs.koin.compose)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    testImplementation(libs.junit)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit4)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    implementation(project(":presentation"))
    implementation(project(":presentation-theme"))
    implementation(project(":data"))
}

val setupPreCommitHook by tasks.register("setupPreCommitHook") {
    group = "git hooks"
    description = "Installs pre-commit Git hook to run lint checks"

    doLast {
        val gitHooksDir = Paths.get(rootDir.absolutePath, ".git", "hooks")
        if (!Files.exists(gitHooksDir)) {
            println(".git/hooks directory does not exist. Are you inside a Git repo?")
            return@doLast
        }

        val preCommitHook = gitHooksDir.resolve("pre-commit")
        val script = """
            #!/bin/sh
            echo "Running lint checks..."
            ./gradlew ktlintCheck detekt
            RESULT=$?
            if [ ${'$'}RESULT -ne 0 ]; then
                echo "Code style checks failed. Commit aborted."
                exit 1
            fi
            exit 0
        """.trimIndent()

        Files.write(preCommitHook, script.toByteArray(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)
        preCommitHook.toFile().setExecutable(true)

        println("Pre-commit hook installed successfully!")
    }
}