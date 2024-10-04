package net.erickveil.calmsound.viewmodel

import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import net.erickveil.calmsound.intent.BrownNoiseIntent
import net.erickveil.calmsound.model.BrownNoiseState
import kotlin.random.Random
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch

class BrownNoiseViewModel() : ViewModel() {

    private var audioTrack: AudioTrack? = null
    private val _state = MutableStateFlow(BrownNoiseState())
    val state: StateFlow<BrownNoiseState> get() = _state

    fun processIntent(intent: BrownNoiseIntent) {
        when (intent) {
            is BrownNoiseIntent.TogglePlayback -> togglePlayback()
            is BrownNoiseIntent.AudioDeviceDisconnected -> stopBrownNoise()
        }
    }

    private fun togglePlayback() {
        viewModelScope.launch {
            val newState = !_state.value.isPlaying
            _state.value =BrownNoiseState(isPlaying = newState)
            if (newState) {
                startBrownNoise()
            } else {
                stopBrownNoise()
            }
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
        var lastOutput = 0.0

        Thread {
            while (audioTrack?.playState == AudioTrack.PLAYSTATE_PLAYING) {
                for (i in buffer.indices) {
                    val whiteNoise = Random.nextDouble(-1.0, 1.0)
                    brownNoise = (lastOutput + (0.02 * whiteNoise)) / 1.02
                    lastOutput = brownNoise
                    brownNoise *= 3.5 // Volume
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