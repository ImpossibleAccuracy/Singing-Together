data class UiFeature(
    val path: String
)

val UiFeature.Domain
    get() = "$path:domain"

val UiFeature.Data
    get() = "$path:data"

val UiFeature.Di
    get() = "$path:di"

val UiFeature.Presenter
    get() = "$path:presenter"
