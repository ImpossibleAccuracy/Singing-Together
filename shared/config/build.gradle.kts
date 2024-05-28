plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
}

group = AppConfig.buildGroup("config")

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
            implementation(libs.kotlinx.coroutines.core)

            implementation(project(Modules.Library.AudioDecoder))
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
