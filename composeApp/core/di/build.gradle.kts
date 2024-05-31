plugins {
    alias(libs.plugins.multiplatform)

    alias(libs.plugins.android.library)
}

group = AppConfig.buildGroup("app", "di")

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
            api(project(Modules.App.Domain))

            implementation(project(Modules.Shared.Payload))
            implementation(project(Modules.App.Data))

            implementation(libs.bundles.koin)
            implementation(libs.bundles.ktor)
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
