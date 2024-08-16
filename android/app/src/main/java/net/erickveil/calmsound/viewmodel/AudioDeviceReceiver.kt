package net.erickveil.calmsound.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager

class AudioDeviceReceiver(private val onAudioDeviceDisconnected: () -> Unit) : BroadcastReceiver()  {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
            onAudioDeviceDisconnected()
        }
    }
}