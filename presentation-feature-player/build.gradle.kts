plugins {
    id("devlight.android.feature")
}

android {
    namespace = "com.serhiimysyshyn.devlightiptvclient.presentation.feature.player"
}

dependencies {
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.hls)
    implementation(libs.androidx.media3.ui)
}
