plugins {
    alias(libs.plugins.kotlin.jvm) apply false

    alias(libs.plugins.javafx) apply false

    alias(libs.plugins.spring.boot) apply false

    alias(libs.plugins.kotlin.allopen) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.kotlin.jpa) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

group = "com.singing.api"

