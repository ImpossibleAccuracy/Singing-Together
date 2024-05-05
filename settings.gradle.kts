import java.net.URI

rootProject.name = "Singing-Together"

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven {
            name = "TarsosDSP repository"
            url = URI("https://mvn.0110.be/releases")
        }
    }
}

include(
    ":library",
    ":library:utils",
    ":library:audio-library",
    ":library:audio-player",
)

include(
    ":config",
)

include(
    ":composeApp"
)

include(
    ":server",
    ":server:library",
    ":server:main-api",
)
