plugins {
    `kotlin-dsl`
}

group = "com.serhiimysyshyn.devlightiptvclient.build.logic"

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("application") {
            id = "devlight.android.application"
            implementationClass = "ApplicationConventionPlugin"
        }

        register("library") {
            id = "devlight.android.library"
            implementationClass = "LibraryConventionPlugin"
        }

        register("feature") {
            id = "devlight.android.feature"
            implementationClass = "FeatureConventionPlugin"
        }
    }
}

kotlin {
    compilerOptions {
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0)
        languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0)
    }
}