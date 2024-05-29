plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose)

    alias(libs.plugins.android.library)
}

group = AppConfig.buildGroup("feature", "publication", "details")

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
            implementation(project(Modules.App.Common.Theme))
            implementation(project(Modules.App.Common.ViewsExtensions))
            implementation(project(Modules.App.Common.Navigation))

            implementation(project(Modules.App.Feature.Base))
            implementation(project(Modules.App.Feature.RecordDetails.resolve("views")))
            implementation(project(Modules.App.Feature.PublicationDetails.Domain))

            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)

            implementation(libs.paging.compose.common)
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

//tasks.withType<GenerateResValues>().configureEach {
//    resources.Resources.copyResourcesToFeature(rootDir, projectDir)
//}
