plugins {
    id("devlight.android.application")
}

android {
    namespace = "com.serhiimysyshyn.devlightiptvclient"
}

// :app is the composition root — it is the only module that may see every layer at once,
// because it is where the Koin graph and the root NavHost are assembled.
dependencies {
    implementation(libs.androidx.core.splashscreen)

    implementation(projects.domain)
    implementation(projects.data)
    implementation(projects.dataPreference)

    implementation(projects.presentationCoreStyling)
    implementation(projects.presentationCoreNavigation)
    implementation(projects.presentationCorePlatform)

    implementation(projects.presentationFeatureHome)
    implementation(projects.presentationFeaturePlayer)
    implementation(projects.presentationFeatureSettings)
}
