import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)

    alias(libs.plugins.android.application)
}

group = AppConfig.buildGroup("app")

kotlin {
    targets.configureEach {
        compilations.configureEach {
            compilerOptions.configure {
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }

    jvm()

    androidTarget("android") {
        compilations.all {
            kotlinOptions {
                jvmTarget = libs.versions.jvmTargetVersion.get()
                freeCompilerArgs += "-Xjdk-release=${libs.versions.jvmTargetVersion.get()}"
            }
        }

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant {
            sourceSetTree.set(KotlinSourceSetTree.test)

            dependencies {
                debugImplementation(libs.androidx.testManifest)
                implementation(libs.androidx.junit4)
            }
        }
    }

    sourceSets {
        all {
            languageSettings {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }

        commonMain.dependencies {
            implementation(project(Modules.App.Common.Navigation))
            implementation(project(Modules.App.Common.Theme))
            implementation(project(Modules.App.Domain))
            implementation(project(Modules.App.Data))
            implementation(project(Modules.App.Di))

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.uiTooling)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.collections)

            implementation(libs.bundles.voyager)
            implementation(libs.bundles.koin)

            implementation(libs.composeImageLoader)

            // FEATURES --------------------------------------------------------------

            implementation(project(Modules.App.Feature.Home.Presenter))
            implementation(project(Modules.App.Feature.Home.Di))

            implementation(project(Modules.App.Feature.Auth.Presenter))
            implementation(project(Modules.App.Feature.Auth.Di))

            implementation(project(Modules.App.Feature.Community.Presenter))
            implementation(project(Modules.App.Feature.Community.Di))

            implementation(project(Modules.App.Feature.PublicationDetails.Presenter))
            implementation(project(Modules.App.Feature.PublicationDetails.Di))

            implementation(project(Modules.App.Feature.RecordList.Presenter))
            implementation(project(Modules.App.Feature.RecordList.Di))

            implementation(project(Modules.App.Feature.RecordDetails.Presenter))
            implementation(project(Modules.App.Feature.RecordDetails.Di))

            implementation(project(Modules.App.Feature.Profile.Presenter))
            implementation(project(Modules.App.Feature.Profile.Di))

            implementation(project(Modules.App.Feature.Recording.Presenter))
            implementation(project(Modules.App.Feature.Recording.Di))

            implementation(project(Modules.App.Feature.RecordingSetup.Presenter))
            implementation(project(Modules.App.Feature.RecordingSetup.Di))
        }

        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)

            implementation(libs.kotlinx.coroutines.javafx)

            implementation(libs.koin.compose.jvm)
        }

        androidMain.dependencies {
            implementation(compose.components.uiToolingPreview)
            implementation(compose.uiTooling)

            implementation(libs.bundles.android)
            implementation(libs.kotlinx.coroutines.android)

            implementation(libs.koin.android)
        }
    }
}

android {
    namespace = AppConfig.APPLICATION_ID
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        applicationId = AppConfig.APPLICATION_ID
        versionCode = AppConfig.VERSION_CODE
        versionName = AppConfig.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        resources.srcDirs("src/commonMain/resources")
        res.srcDirs("src/androidMain/res")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jvmTargetVersion.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.jvmTargetVersion.get())
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Exe, TargetFormat.Msi)
            packageName = AppConfig.APPLICATION_ID
            packageVersion = AppConfig.VERSION_NAME
        }
    }
}
