plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
}

group = "com.singing.audio.library"

kotlin {
    targets.configureEach {
        compilations.configureEach {
            compilerOptions.configure {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }

    jvm()

    androidTarget()

    sourceSets {
        all {
            languageSettings {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }

        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.dsp.core)
        }

        jvmMain.dependencies {
            implementation(libs.xtaudio)
            implementation(libs.mp3spi)
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
