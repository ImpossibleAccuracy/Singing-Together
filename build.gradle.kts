import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm) apply false

    alias(libs.plugins.javafx) apply false

    alias(libs.plugins.multiplatform) apply false
    alias(libs.plugins.compose) apply false

    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.kotlin.allopen) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.kotlin.jpa) apply false
    alias(libs.plugins.kotlin.serialization) apply false

    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false

    alias(libs.plugins.sqldelight) apply false
}

subprojects {
    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            if (project.findProperty("composeCompilerReports") == "true") {
                freeCompilerArgs += "-P"
                freeCompilerArgs += "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                        project.buildDir.absolutePath + "/compose_compiler"

            }
            if (project.findProperty("composeCompilerMetrics") == "true") {
                freeCompilerArgs += "-P"
                freeCompilerArgs += "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                        project.buildDir.absolutePath + "/compose_compiler"

            }
        }
    }
}