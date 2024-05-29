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

include(":library:audio:decoder")
include(":library:audio:player")
include(":library:files")
include(":library:notes")

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

include(":composeApp:common:theme")
include(":composeApp:common:ui-tools")
include(":composeApp:common:views")
include(":composeApp:common:views:extensions")
include(":composeApp:common:navigation")
include(":composeApp:feature:base")

// ---------------------------------------------------------------------------

include(":composeApp:feature:home:presenter")
include(":composeApp:feature:home:domain")
include(":composeApp:feature:home:di")

include(":composeApp:feature:community:presenter")
include(":composeApp:feature:community:domain")
include(":composeApp:feature:community:di")

include(":composeApp:feature:record-details:views")
include(":composeApp:feature:record-details:presenter")
include(":composeApp:feature:record-details:di")

include(":composeApp:feature:record-list:presenter")
include(":composeApp:feature:record-list:domain")
include(":composeApp:feature:record-list:di")

include(":composeApp:feature:account-profile:presenter")
include(":composeApp:feature:account-profile:domain")
include(":composeApp:feature:account-profile:di")

include(":composeApp:feature:recording:presenter")
include(":composeApp:feature:recording:domain")
include(":composeApp:feature:recording:data")
include(":composeApp:feature:recording:di")
