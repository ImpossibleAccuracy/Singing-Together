plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)

    alias(libs.plugins.android.library)
}

group = AppConfig.buildGroup("app", "common", "views")

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
            implementation(project(Modules.App.Domain))
            implementation(project(Modules.App.Common.UiTools))
            implementation(project(Modules.App.Common.Views))

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.uiTooling)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.collections)

            implementation(libs.humanReadable)
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
