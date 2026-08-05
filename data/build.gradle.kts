plugins {
    id("devlight.android.library")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.serhiimysyshyn.devlightiptvclient.data"
}

dependencies {
    implementation(projects.domain)
    implementation(projects.dataPreference)

    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)

    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    implementation(libs.koin.android)
}
