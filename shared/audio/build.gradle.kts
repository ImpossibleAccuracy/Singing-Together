plugins {
    alias(libs.plugins.multiplatform)

    alias(libs.plugins.android.library)
}

group = AppConfig.buildGroup("app", "audio")

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
            implementation(project(Modules.Shared.Config))
            implementation(project(Modules.Shared.Model))
            api(project(Modules.Library.Audio.Decoder))

            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.dsp.core)
        }

        jvmMain.dependencies {
            //noinspection UseTomlInstead
            implementation("org.openjfx:javafx-base:${libs.versions.jvmTargetVersion.get()}.0.1:win")
            //noinspection UseTomlInstead
            implementation("org.openjfx:javafx-graphics:${libs.versions.jvmTargetVersion.get()}.0.1:win")
            //noinspection UseTomlInstead
            implementation("org.openjfx:javafx-controls:${libs.versions.jvmTargetVersion.get()}.0.1:win")
            //noinspection UseTomlInstead
            implementation("org.openjfx:javafx-media:${libs.versions.jvmTargetVersion.get()}.0.1:win")
            //noinspection UseTomlInstead
            implementation("org.openjfx:javafx-web:${libs.versions.jvmTargetVersion.get()}.0.1:win")
            //noinspection UseTomlInstead
            implementation("org.openjfx:javafx-swing:${libs.versions.jvmTargetVersion.get()}.0.1:win")

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
