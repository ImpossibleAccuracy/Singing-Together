plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.library)
}

group = AppConfig.buildGroup("feature", "recording", "data")

kotlin {
    jvm()

    androidTarget("android")

    sourceSets {
        commonMain.dependencies {
            api(project(Modules.Library.Audio.Decoder))
            api(project(Modules.Library.Audio.Player))
            api(project(Modules.Shared.Config))
            api(project(Modules.Shared.Audio))
            api(project(Modules.App.Domain))

            api(project(Modules.App.Feature.Recording.Domain))

            implementation(compose.runtime)
            implementation(compose.foundation)
        }
    }
}

android {
    namespace = group as String
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jvmTargetVersion.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.jvmTargetVersion.get())
    }
}
