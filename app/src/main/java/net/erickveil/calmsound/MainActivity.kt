package net.erickveil.calmsound

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import kotlin.random.Random

class MainActivity : ComponentActivity() {

    private var audioTrack: AudioTrack? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BrownNoisePlayerScreen()
        }
    }

    @Composable
    fun BrownNoisePlayerScreen() {
        var isPlaying by remember { mutableStateOf(false) }

        LaunchedEffect(isPlaying) {
            if (isPlaying) {
                startBrownNoise()
            } else {
                stopBrownNoise()
            }
        }

        Button(onClick = { isPlaying = !isPlaying }) {
            Text(if (isPlaying) "Stop Brown Noise" else "Play Brown Noise")
        }
    }

    private fun startBrownNoise() {
        val sampleRate = 44100
        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT)

        audioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT,
            bufferSize,
            AudioTrack.MODE_STREAM)

        val buffer = ShortArray(bufferSize)
        var brownNoise = 0.0

        audioTrack?.play()

        Thread {
            while (audioTrack?.playState == AudioTrack.PLAYSTATE_PLAYING) {
                for (i in buffer.indices) {
                    brownNoise += Random.nextDouble(-1.0, 1.0)
                    brownNoise *= 0.03 // Scale the noise
                    buffer[i] = (brownNoise * Short.MAX_VALUE).toInt().toShort()
                }
                audioTrack?.write(buffer, 0, buffer.size)
            }
        }.start()
    }

    private fun stopBrownNoise() {
        audioTrack?.stop()
        audioTrack?.release()
        audioTrack = null
    }
}
