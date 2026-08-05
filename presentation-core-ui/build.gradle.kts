plugins {
    id("devlight.android.compose")
}

android {
    namespace = "com.serhiimysyshyn.devlightiptvclient.presentation.core.ui"
}

dependencies {
    implementation(projects.presentationCoreStyling)
    implementation(projects.presentationCorePlatform)

    implementation(libs.androidx.core.ktx)
    implementation(libs.glide)
    implementation(libs.glide.compose)
}
