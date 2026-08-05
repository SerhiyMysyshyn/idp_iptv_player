plugins {
    `kotlin-dsl`
}

group = "com.serhiimysyshyn.devlightiptvclient.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0)
        languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0)
    }
}

dependencies {
    // Makes the generated type-safe version catalog accessors (LibrariesForLibs) available
    // inside Plugin<Project> classes, not just in .gradle.kts scripts.
    compileOnly(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
    compileOnly(libs.compose.compiler.gradle.plugin)

    // Quality plugins must be on the runtime classpath — convention plugins apply them by id.
    implementation(libs.detekt.gradle.plugin)
    implementation(libs.ktlint.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "devlight.android.application"
            implementationClass = "buildlogic.plugin.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "devlight.android.library"
            implementationClass = "buildlogic.plugin.AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "devlight.android.compose"
            implementationClass = "buildlogic.plugin.AndroidComposeConventionPlugin"
        }
        register("androidFeature") {
            id = "devlight.android.feature"
            implementationClass = "buildlogic.plugin.AndroidFeatureConventionPlugin"
        }
        register("kotlinLibrary") {
            id = "devlight.kotlin.library"
            implementationClass = "buildlogic.plugin.KotlinLibraryConventionPlugin"
        }
        register("quality") {
            id = "devlight.quality"
            implementationClass = "buildlogic.plugin.QualityConventionPlugin"
        }
    }
}
