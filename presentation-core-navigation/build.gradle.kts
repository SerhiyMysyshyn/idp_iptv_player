plugins {
    id("devlight.android.compose")
}

android {
    namespace = "com.serhiimysyshyn.devlightiptvclient.presentation.core.navigation"
}

dependencies {
    // `api`: LocalAppNavController exposes NavHostController to every consumer.
    api(libs.androidx.navigation.compose)
}
