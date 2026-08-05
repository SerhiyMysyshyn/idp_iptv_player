plugins {
    id("devlight.android.compose")
}

android {
    namespace = "com.serhiimysyshyn.devlightiptvclient.presentation.core.platform"
}

dependencies {
    // `api`, not `implementation`: BaseViewModel extends androidx ViewModel and every feature
    // ViewModel inherits from it, so the type has to stay on the consumer's compile classpath.
    api(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
}
