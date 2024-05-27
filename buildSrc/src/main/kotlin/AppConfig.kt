@Suppress("MemberVisibilityCanBePrivate")
object AppConfig {
    val APPLICATION_ID = buildGroup("app")
    const val VERSION_CODE = 1
    const val VERSION_NAME = "1.0.0"

    private const val GROUP = "com.singing."

    fun buildGroup(vararg string: String) =
        GROUP + string.joinToString(".")
}
