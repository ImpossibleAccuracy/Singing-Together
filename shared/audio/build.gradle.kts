plugins {
    alias(libs.plugins.multiplatform)

    alias(libs.plugins.android.library)
}

group = "com.singing.app"

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
        all {
            languageSettings {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }

        commonMain.dependencies {
            implementation(project(":config"))
            implementation(project(":shared"))
            implementation(project(":library:utils"))
            implementation(project(":library:audio-library"))

            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.dsp.core)
        }

        jvmMain.dependencies {
            //noinspection UseTomlInstead
            implementation("org.openjfx:javafx-base:21.0.1:win")
            //noinspection UseTomlInstead
            implementation("org.openjfx:javafx-graphics:21.0.1:win")
            //noinspection UseTomlInstead
            implementation("org.openjfx:javafx-controls:21.0.1:win")
            //noinspection UseTomlInstead
            implementation("org.openjfx:javafx-media:21.0.1:win")
            //noinspection UseTomlInstead
            implementation("org.openjfx:javafx-web:21.0.1:win")
            //noinspection UseTomlInstead
            implementation("org.openjfx:javafx-swing:21.0.1:win")

            implementation(libs.dsp.jvm)
        }
    }
}

android {
    namespace = group.toString()
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jvmTargetVersion.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.jvmTargetVersion.get())
    }
}
