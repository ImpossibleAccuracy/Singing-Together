plugins {
    alias(libs.plugins.multiplatform)

    alias(libs.plugins.android.library)
}

group = AppConfig.buildGroup("audio", "player")

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
        commonMain.dependencies {
//            implementation(project(":library:utils"))
            implementation(project(Modules.SHARED_BASE))

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
