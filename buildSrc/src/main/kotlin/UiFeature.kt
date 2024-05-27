data class UiFeature(
    val path: String
)

val UiFeature.DOMAIN
    get() = "$path:domain"

val UiFeature.DATA
    get() = "$path:data"

val UiFeature.DI
    get() = "$path:di"

val UiFeature.PRESENTER
    get() = "$path:presenter"
