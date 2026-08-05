plugins {
    id("devlight.android.compose")
}

android {
    namespace = "com.serhiimysyshyn.devlightiptvclient.presentation.core.styling"
}

// No project dependencies on purpose — the design system must not know about features,
// navigation or data. Compose itself comes from the `devlight.android.compose` convention.
