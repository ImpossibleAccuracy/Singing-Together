import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.app_name
import com.singing.audio.utils.backgroundScope
import kotlinx.coroutines.cancel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinApplication
import org.singing.app.App
import org.singing.app.di.totalAppModules
import java.awt.Dimension

suspend fun main() = application {
    val minSize = Dimension(1280, 720)

    Window(
        title = stringResource(Res.string.app_name),
        state = rememberWindowState(width = minSize.width.dp, height = minSize.height.dp),
        onCloseRequest = {
            backgroundScope.cancel()

            exitApplication()
        },
    ) {
        window.minimumSize = minSize

        KoinApplication(
            application = {
                modules(totalAppModules)
            }
        ) {
            App()
        }
    }
}
