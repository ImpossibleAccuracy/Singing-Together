plugins {
    alias(libs.plugins.kotlin.jvm)

    alias(libs.plugins.spring.boot)

    alias(libs.plugins.kotlin.allopen)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.serialization)
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

group = "com.singing.api"

dependencies {
    // COROUTINES DEPENDENCIES
    implementation(libs.kotlinx.coroutines.core)

    // SPRING DEPENDENCIES
    implementation(libs.bundles.spring)
    runtimeOnly(libs.spring.devtools)
    implementation(libs.bundles.jjwt)
}

kotlin {
    jvmToolchain(libs.versions.jvmTargetVersion.get().toInt())
}
