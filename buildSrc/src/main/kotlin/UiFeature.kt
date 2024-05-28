data class UiFeature(
    val path: String
) {
    fun resolve(submodule: String) ="$path:$submodule"
}

val UiFeature.Domain
    get() = resolve("domain")

val UiFeature.Data
    get() = resolve("data")

val UiFeature.Di
    get() = resolve("di")

val UiFeature.Presenter
    get() = resolve("presenter")
