plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.library)
}

group = AppConfig.buildGroup("feature", "main", "domain")

kotlin {
    jvm()

    androidTarget("android")

    sourceSets {
        commonMain.dependencies {
            api(project(Modules.APP_DOMAIN))

            implementation(compose.runtime)
            implementation(compose.foundation)

            api(libs.kotlinx.coroutines.core)
            api(libs.kotlinx.collections)

            implementation(libs.paging.compose)
        }

        androidMain.dependencies {
            implementation(libs.kotlinx.coroutines.android)
        }
    }
}

android {
    namespace = AppConfig.APPLICATION_ID
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jvmTargetVersion.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.jvmTargetVersion.get())
    }
}
