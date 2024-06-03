plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)

    alias(libs.plugins.android.library)

    alias(libs.plugins.kotlin.serialization)
}

group = AppConfig.buildGroup("app", "auth")

kotlin {
    jvm()

    androidTarget("android")

    sourceSets {
        commonMain.dependencies {
            api(project(Modules.Shared.Model))
            api(project(Modules.App.Domain))

            implementation(compose.runtime)
            implementation(compose.foundation)

            api(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization)

            implementation(libs.settings)
            implementation(libs.settings.coroutines)
            implementation(libs.settings.serialization)
        }

        androidMain.dependencies {
            api(libs.kotlinx.coroutines.android)
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
