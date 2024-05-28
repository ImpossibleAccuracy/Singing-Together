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

include(":library:audio-decoder")
include(":library:audio-player")

include(":shared:base")
include(":shared:config")
include(":shared:model")
include(":shared:payload")
include(":shared:audio")

include(":server:library")
include(":server:main-api")

include(":composeApp")
include(":composeApp:core:data")
include(":composeApp:core:domain")
include(":composeApp:core:di")
include(":composeApp:presenter")

include(":composeApp:common:base")
include(":composeApp:common:ui-tools")
include(":composeApp:common:views")
include(":composeApp:common:views:extensions")
include(":composeApp:common:navigation")

include(":composeApp:feature:home:presenter")
include(":composeApp:feature:home:domain")
include(":composeApp:feature:home:di")

include(":composeApp:feature:community:presenter")
include(":composeApp:feature:community:domain")
include(":composeApp:feature:community:di")
