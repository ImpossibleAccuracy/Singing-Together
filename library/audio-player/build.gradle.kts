plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)

    alias(libs.plugins.android.library)
}

group = "com.singing.audio.player"

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
            implementation(compose.runtime)
            implementation(compose.foundation)

            implementation(project(":library:utils"))

            implementation(libs.kotlinx.coroutines.core)
        }

        jvmMain.dependencies {
            implementation("org.openjfx:javafx-base:${libs.versions.jvmTargetVersion.get()}.0.1:win")
            implementation("org.openjfx:javafx-media:${libs.versions.jvmTargetVersion.get()}.0.1:win")
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
