//
//  ContentView.swift
//  Hygge-Sound
//
//  Created by Erick Veil on 8/16/24.
//

import SwiftUI
import AVFoundation

struct ContentView: View {
    var body: some View {
        VStack {
            Image(systemName: "globe")
                .imageScale(.large)
                .foregroundStyle(.tint)
            Text("Hello, world!")
        }
        .padding()
    }
}

private func generateBrownNoiseBuffer() -> AVAudioPCMBuffer {
    // 44.1 kHz is a common sample rate for CD quality audio
    let sampleRate: Double = 44100
    // 1 second of noise
    let duration: Double = 1.0
    let frameCount = UInt32(sampleRate * duration)
    
    // Generates a mono (1 channel) audio format object and the `!` assumes 
    // that it is not going to be null.
    // If it is nil, this will crash here.
    // TODO: It may be safer to test for `nil`.
    let format = AVAudioFormat(standardFormatWithSampleRate: sampleRate, 
                               channels: 1)!
    let buffer = AVAudioPCMBuffer(pcmFormat: format, frameCapacity: frameCount)!
    buffer.frameLength = frameCount
    
    let brownNoise = buffer.floatChannelData![0]
    var lastOutput: Float = 0.0
    
    for i in 0..<Int(frameCount) {
        let white = Float.random(in: -1...1)
        // filter down to brown
        lastOutput = (lastOutput + (0.02 * white)) / 1.02
        // Increase the amplitude (volume) a little.
        let volume: Float = 3.5
        brownNoise[i] = lastOutput * volume
    }
    
    return buffer
}

#Preview {
    ContentView()
}
