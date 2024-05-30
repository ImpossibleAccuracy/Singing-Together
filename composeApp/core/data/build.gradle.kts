plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)

    alias(libs.plugins.android.library)

    alias(libs.plugins.sqldelight)
}

group = AppConfig.buildGroup("app", "data")

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("$group.database")
        }
    }
}

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
            implementation(project(Modules.Library.Audio.Player))
            implementation(project(Modules.Library.Notes))
            implementation(project(Modules.Shared.Payload))
            implementation(project(Modules.App.Domain))

            implementation(compose.runtime)
            implementation(compose.foundation)

            implementation(libs.kotlinx.datetime)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.bundles.ktor)
            implementation(libs.sqldelight.coroutines)
            implementation(libs.paging.compose)
            implementation(libs.apiresult)
        }

        androidMain.dependencies {
            implementation(compose.components.uiToolingPreview)

            implementation(libs.bundles.android)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.android)
            implementation(libs.sqldelight.driver.android)
        }

        jvmMain.dependencies {
            implementation(libs.ktor.client.cio)
            implementation(libs.sqldelight.driver.sqlite)
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
