plugins {
    id("devlight.android.library")
    alias(libs.plugins.protobuf)
}

android {
    namespace = "com.serhiimysyshyn.devlightiptvclient.data.preference"
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${libs.versions.protoJavaLiteVersion.get()}"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    implementation(projects.domain)

    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.core)
    implementation(libs.protobuf.javalite)

    implementation(libs.koin.android)
}
