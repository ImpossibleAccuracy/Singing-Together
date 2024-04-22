rootProject.name = "Singing-Together"
include(":composeApp")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

include(
    ":library",
    ":library:utils",
    ":library:audio-library",
    ":library:audio-devices",
)

include(
    ":composeApp"
)
