@file:Suppress("ConstPropertyName")

object Modules {
    object Shared {
        const val Config = ":shared:config"
        const val Base = ":shared:base"
        const val Model = ":shared:model"
        const val Payload = ":shared:payload"
        const val Audio = ":shared:audio"
    }

    object Library {
        const val AudioDecoder = ":library:audio-decoder"
        const val Player = ":library:audio-player"
    }

    object Server {
        const val Library = ":server:library"
        const val Api = ":server:main-api"
    }

    object App {
        const val Domain = ":composeApp:core:domain"
        const val Data = ":composeApp:core:data"
        const val Di = ":composeApp:core:di"

        object Common {
            const val Base = ":composeApp:common:base"
            const val UiTools = ":composeApp:common:ui-tools"
            const val Views = ":composeApp:common:views"
            const val ViewsExtensions = ":composeApp:common:views:extensions"
            const val Navigation = ":composeApp:common:navigation"
        }

        object Feature{
            val Home = UiFeature(":composeApp:feature:home")
            val Community = UiFeature(":composeApp:feature:community")
        }
    }
}