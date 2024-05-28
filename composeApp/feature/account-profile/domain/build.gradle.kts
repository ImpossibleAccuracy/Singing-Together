plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.library)
}

group = AppConfig.buildGroup("feature", "account", "profile", "domain")

kotlin {
    jvm()

    androidTarget("android")

    sourceSets {
        commonMain.dependencies {
            api(project(Modules.App.Domain))

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
