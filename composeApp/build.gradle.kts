import com.android.build.api.dsl.ManagedVirtualDevice
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)

    alias(libs.plugins.android.application)

    alias(libs.plugins.sqldelight)
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

    androidTarget("android") {
        compilations.all {
            kotlinOptions {
                jvmTarget = libs.versions.jvmTargetVersion.get()
                freeCompilerArgs += "-Xjdk-release=${libs.versions.jvmTargetVersion.get()}"
            }
        }

        //https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html
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
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.uiTooling)
            implementation(compose.components.uiToolingPreview)

            implementation(project(":config"))
            implementation(project(":library:utils"))
            implementation(project(":library:audio-library"))
            implementation(project(":library:audio-player"))

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.collections)

            implementation(libs.bundles.voyager)
            implementation(libs.bundles.koin)
            implementation(libs.bundles.ktor)

            implementation(libs.sqldelight.coroutines)

            implementation(libs.settings.multiplatform)
            implementation(libs.calf.ui)
            implementation(libs.chiptextfield)
            implementation(libs.composeImageLoader)
            implementation(libs.humanReadable)
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

            implementation(compose.desktop.currentOs)

            implementation(libs.kotlinx.coroutines.javafx)

            implementation(libs.koin.compose.jvm)

            implementation(libs.ktor.client.cio)
            implementation(libs.sqldelight.sqlite)

            implementation(libs.mp3spi)
            implementation(libs.dsp.core)
            implementation(libs.dsp.jvm)
        }

        androidMain.dependencies {
            implementation(compose.components.uiToolingPreview)
            implementation(compose.uiTooling)

            implementation(libs.kotlinx.coroutines.android)

            implementation(libs.bundles.android)

            implementation(libs.koin.android)

            implementation(libs.ktor.client.android)
            implementation(libs.sqldelight.android)

            implementation(libs.mp3spi)
            implementation(libs.dsp.core)
            implementation(libs.dsp.jvm)
        }
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set(group as String)
        }
    }
}

android {
    namespace = "org.singing.app"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        applicationId = "org.singing.app"
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    sourceSets["main"].apply {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/res")
        resources.srcDirs("src/commonMain/resources")
    }

    //https://developer.android.com/studio/test/gradle-managed-devices
    @Suppress("UnstableApiUsage")
    testOptions {
        managedDevices.devices {
            maybeCreate<ManagedVirtualDevice>("pixel5").apply {
                device = "Pixel 5"
                apiLevel = 34
                systemImageSource = "aosp"
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.jvmTargetVersion.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.jvmTargetVersion.get())
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
            packageName = "com.singing.app"
            packageVersion = "1.0.0"
        }
    }
}
