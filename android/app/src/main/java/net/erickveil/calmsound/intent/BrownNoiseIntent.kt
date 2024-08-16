package net.erickveil.calmsound.intent

sealed class BrownNoiseIntent {
    object TogglePlayback : BrownNoiseIntent()
    object AudioDeviceDisconnected : BrownNoiseIntent()
}