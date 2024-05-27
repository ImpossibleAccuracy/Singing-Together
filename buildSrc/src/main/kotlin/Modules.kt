object Modules {
    const val SHARED_CONFIG = ":shared:config"
    const val SHARED_BASE = ":shared:base"
    const val SHARED_MODEL = ":shared:model"
    const val SHARED_PAYLOAD = ":shared:payload"
    const val SHARED_AUDIO = ":shared:audio"

    const val LIBRARY_AUDIO_DECODER = ":library:audio-decoder"
    const val LIBRARY_AUDIO_PLAYER = ":library:audio-player"

    const val SERVER_LIBRARY = ":server:library"

    const val APP_DOMAIN = ":composeApp:core:domain"
    const val APP_DATA = ":composeApp:core:data"
    const val APP_DI = ":composeApp:core:di"

    const val APP_COMMON = ":composeApp:common:base"
    const val APP_UI_TOOLS = ":composeApp:common:ui-tools"
    const val APP_VIEWS = ":composeApp:common:views"
    const val APP_VIEWS_EXTENSIONS = ":composeApp:common:views:extensions"
    const val APP_NAVIGATION = ":composeApp:common:navigation"

    val APP_FEATURE_MAIN = UiFeature(":composeApp:feature:main")
    val APP_FEATURE_COMMUNITY = UiFeature(":composeApp:feature:community")
}