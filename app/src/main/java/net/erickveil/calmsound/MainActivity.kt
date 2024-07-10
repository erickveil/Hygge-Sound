package net.erickveil.calmsound

import android.content.IntentFilter
import android.media.AudioManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import net.erickveil.calmsound.viewmodel.BrownNoiseViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import net.erickveil.calmsound.intent.BrownNoiseIntent
import net.erickveil.calmsound.model.BrownNoiseState
import net.erickveil.calmsound.ui.theme.CalmSoundTheme
import net.erickveil.calmsound.viewmodel.AudioDeviceReceiver

class MainActivity : ComponentActivity() {

    private val viewModel: BrownNoiseViewModel by viewModels()
    private lateinit var audioDeviceReceiver: AudioDeviceReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle speaker disconnect
        audioDeviceReceiver = AudioDeviceReceiver {
            viewModel.processIntent(BrownNoiseIntent.AudioDeviceDisconnected)
        }
        val filter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
        registerReceiver(audioDeviceReceiver, filter)

        setContent {
            CalmSoundTheme {
                val state by viewModel.state.collectAsStateWithLifecycle()
                BrownNoisePlayerScreen(
                    state = state,
                    onTogglePlayback = {
                        viewModel.processIntent(BrownNoiseIntent.TogglePlayback)
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(audioDeviceReceiver)
    }

    @Composable
    fun BrownNoisePlayerScreen( state: BrownNoiseState, onTogglePlayback: () -> Unit ) {
        Button(onClick = onTogglePlayback) {
            Text(if (state.isPlaying) "Stop Brown Noise" else "Play Brown Noise")
        }
    }

}
