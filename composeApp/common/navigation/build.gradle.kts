plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)

    alias(libs.plugins.android.library)
}

group = AppConfig.buildGroup("app", "navigation")

kotlin {
    jvm()

    androidTarget("android")

    sourceSets {
        commonMain.dependencies {
            implementation(project(Modules.App.Domain))

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)

            api(libs.voyager.navigator)
            api(libs.voyager.koin)
            api(libs.koin.core)
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
