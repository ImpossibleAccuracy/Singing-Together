import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.singing.app.App
import com.singing.app.di.totalAppModules
import com.singing.app.presenter.generated.resources.Res
import com.singing.app.presenter.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinApplication
import java.awt.Dimension

fun main() = application {
    val minSize = Dimension(1280, 720)

    Window(
        title = stringResource(Res.string.app_name),
        state = rememberWindowState(width = minSize.width.dp, height = minSize.height.dp),
        onCloseRequest = {
            exitApplication()
        },
    ) {
        window.minimumSize = minSize

        KoinApplication(
            application = {
                modules(totalAppModules())
            }
        ) {
            App()
        }
    }
}
