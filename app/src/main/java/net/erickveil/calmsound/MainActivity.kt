package net.erickveil.calmsound

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

class MainActivity : ComponentActivity() {

    private val viewModel: BrownNoiseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    @Composable
    fun BrownNoisePlayerScreen( state: BrownNoiseState, onTogglePlayback: () -> Unit ) {
        Button(onClick = onTogglePlayback) {
            Text(if (state.isPlaying) "Stop Brown Noise" else "Play Brown Noise")
        }
    }

}
