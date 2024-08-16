package net.erickveil.calmsound

import android.content.IntentFilter
import android.media.AudioManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BrownNoisePlayerScreen(
                        state = state,
                        onTogglePlayback = {
                            viewModel.processIntent(BrownNoiseIntent.TogglePlayback)
                        }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(audioDeviceReceiver)
    }

    @Composable
    fun BrownNoisePlayerScreen( state: BrownNoiseState, onTogglePlayback: () -> Unit ) {
        CustomButton(
            text = if (state.isPlaying) "Stop Calm Sound" else "Play Calm Sound",
            drawableId = R.drawable.calmsound,
            onClick = onTogglePlayback
        )
    }

    @Composable
    fun CustomButton(text: String, drawableId: Int, onClick: () -> Unit) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(100.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor =  Color(0xFFF5F5DC)
            ),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = text,
                    fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 8.dp),
                    color = Color.Black
                )
                Image(
                    painter = painterResource(id = drawableId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(144.dp)
                        .clip(RoundedCornerShape(50.dp))
                )

            }
        }
    }

}
