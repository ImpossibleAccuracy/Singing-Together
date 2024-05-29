plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
}

group = AppConfig.buildGroup("feature", "recording", "domain")

kotlin {
    targets.configureEach {
        compilations.configureEach {
            compilerOptions.configure {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }

    jvm()

    androidTarget("android")

    sourceSets {
        commonMain.dependencies {
            implementation(project(Modules.App.Feature.Recording.Presenter))
            implementation(project(Modules.App.Feature.Recording.Domain))
            implementation(project(Modules.App.Feature.Recording.Data))

            implementation(libs.bundles.koin)
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
