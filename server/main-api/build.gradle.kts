import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)

    alias(libs.plugins.javafx)

    alias(libs.plugins.spring.boot)

    alias(libs.plugins.kotlin.allopen)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.serialization)
}

group = "com.singing.api"

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

// For calculation total duration
javafx {
    version = "17"
    modules = listOf("javafx.media")
}

dependencies {
    implementation(project(":server:library"))

    // PLATFORM DEPENDENCIES
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)

    // COROUTINES DEPENDENCIES
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.reactive)
    implementation(libs.kotlinx.coroutines.reactor)

    // SPRING DEPENDENCIES
    implementation(libs.bundles.spring)
    runtimeOnly(libs.spring.devtools)
    implementation(libs.bundles.mysql)
    implementation(libs.bundles.jackson)
    implementation(libs.bundles.jjwt)

    // SWAGGER
    developmentOnly(libs.spring.swagger)

    // AUDIO
    implementation(project(":config"))
    implementation(project(":library:audio-library"))
    implementation(project(":library:audio-player"))
    implementation(libs.mp3spi)
    implementation(libs.dsp.core)
    implementation(libs.dsp.jvm)


    // OTHER
    implementation(libs.reactor.extensions)
    implementation(libs.mysql.connector)
    implementation(libs.jackson.module)
    implementation(libs.jackson.annotations)

    // TESTS
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.junit.params)
    testRuntimeOnly(libs.junit.engine.jupiter)

    testImplementation(libs.kotlin.test)
    testImplementation(libs.spring.test)
    testImplementation(libs.spring.mockk)
    testImplementation(libs.h2.connector)
    testImplementation(libs.reactor.test)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}
