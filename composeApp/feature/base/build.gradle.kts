plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)

    alias(libs.plugins.android.library)
}

group = AppConfig.buildGroup("app", "feature")

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
            implementation(project(Modules.App.Common.Navigation))
            implementation(project(Modules.App.Domain))

            implementation(compose.runtime)
            implementation(compose.foundation)

            implementation(libs.bundles.koin)
        }

        jvmMain.dependencies {
            // FilePicker JVM Implementation
            //noinspection UseTomlInstead
            implementation("org.openjfx:javafx-base:${libs.versions.jvmTargetVersion.get()}.0.1:win")
            //noinspection UseTomlInstead
            implementation("org.openjfx:javafx-graphics:${libs.versions.jvmTargetVersion.get()}.0.1:win")
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
