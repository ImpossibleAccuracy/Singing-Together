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
)

include(
    ":composeApp"
)
