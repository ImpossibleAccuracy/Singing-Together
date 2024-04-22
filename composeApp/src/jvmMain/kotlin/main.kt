import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.singing.app.composeapp.generated.resources.Res
import com.singing.app.composeapp.generated.resources.app_name
import com.singing.audio.devices.AudioDevices
import com.singing.audio.devices.model.AudioDeviceType
import com.singing.audio.library.input.DataLineAudioInput
import com.singing.audio.utils.backgroundScope
import kotlinx.coroutines.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinApplication
import org.singing.app.App
import org.singing.app.di.totalAppModules
import org.singing.app.setup.audio.AudioDefaults
import org.singing.app.setup.audio.VoiceInput
import java.awt.Dimension
import javax.sound.sampled.TargetDataLine

suspend fun main() = application {
    backgroundScope.launch {
        withContext(Dispatchers.Main) {
            async {
                AudioDevices.Devices
                    .collect { devices ->
                        println(devices)

                        val device = devices.first {
                            it.isDefault && it.type == AudioDeviceType.Input
                        }

                        val line = device.data.line as TargetDataLine

                        val input = DataLineAudioInput(line, AudioDefaults.VoiceDecoderParams)

                        VoiceInput.setAudioInput(input)

                    }
            }
        }

        VoiceInput.voiceData
            .collect {
                println("${it.round()} Hz")
            }
    }

    val minSize = Dimension(1280, 720)

    Window(
        title = stringResource(Res.string.app_name),
        state = rememberWindowState(width = minSize.width.dp, height = minSize.height.dp),
        onCloseRequest = {
            backgroundScope.cancel()
//            AudioDevices.scope.cancel()

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
