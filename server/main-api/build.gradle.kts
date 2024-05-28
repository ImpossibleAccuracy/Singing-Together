plugins {
    alias(libs.plugins.kotlin.jvm)

    alias(libs.plugins.javafx)

    alias(libs.plugins.spring.boot)

    alias(libs.plugins.kotlin.allopen)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.serialization)
}

group = AppConfig.buildGroup("api")

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}

javafx {
    version = libs.versions.jvmTargetVersion.get()
    modules = listOf("javafx.media")
}

dependencies {
    implementation(project(Modules.Shared.Config))
    implementation(project(Modules.Shared.Base))
    implementation(project(Modules.Shared.Model))
    implementation(project(Modules.Shared.Payload))
    implementation(project(Modules.Shared.Audio))
    implementation(project(Modules.Library.AudioDecoder))

    implementation(project(Modules.Server.Library))

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
    implementation(libs.spring.swagger.ui)
    implementation(libs.spring.swagger.api)

    // AUDIO
    implementation("org.openjfx:javafx-media:${libs.versions.jvmTargetVersion.get()}.0.1:win")
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
    testImplementation(libs.spring.test) {
        exclude(module = "junit")
        exclude(module = "mockito-core")
    }
    testImplementation(libs.byte.buddy)
    testImplementation(libs.spring.mockk)
    testImplementation(libs.reactor.test)
    testImplementation(libs.h2.connector)
    testImplementation(libs.junit.api)
}


kotlin {
    jvmToolchain(libs.versions.jvmTargetVersion.get().toInt())
}
