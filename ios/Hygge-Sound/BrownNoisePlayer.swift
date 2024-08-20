//
//  BrownNoisePlayer.swift
//  Hygge-Sound
//
//  Created by Erick Veil on 8/20/24.
//

import SwiftUI
import AVFoundation

// `ObservableObject` allows the SwiftUI to passively observe the changes to its
// properties and automatically update when these changes occur.
//
// Since we are making changes to these properties, we can't just handle this
// stuff in ContentView's `struct` - properties there are immutable by default.
// So we create this external, `@StateObject` so that I can mutate the
// properties as needed.
class BrownNoisePlayer: ObservableObject {
    private var audioEngine: AVAudioEngine?
    private var brownNoisePlayer: AVAudioPlayerNode?
    
    func play() {
        let brownNoiseBuffer = generateBrownNoiseBuffer()
        audioEngine = AVAudioEngine()
        brownNoisePlayer = AVAudioPlayerNode()
        
        // Here we make sure that two optional objects are definitely not nil.
        // If either of them are, we exit this method early.
        guard let audioEngine = audioEngine,
                let brownNoisePlayer = brownNoisePlayer
        else { return }
        
        let mainMixer = audioEngine.mainMixerNode
        audioEngine.attach(brownNoisePlayer)
        audioEngine.connect(
            brownNoisePlayer, to: mainMixer, format: brownNoiseBuffer.format)
        
        // Play the buffer we created:
        //
        // `at: nil` - play NOW
        // `options: .loops` - The `.loops` is an enum value defined somewhere
        //      in `AVFoundation` that indicates we want to loop indefinitely.
        // `completionHandler: nil` - Don't need to do anything special when
        //      we stop.
        brownNoisePlayer.scheduleBuffer(
            brownNoiseBuffer, at: nil, options: .loops, completionHandler: nil)
        
        // ? do/catch like try/catch?
        do {
            try audioEngine.start()
            brownNoisePlayer.play()
        } catch {
            print("Error starting audio engione: \(error)")
            
        }
    }
    
    func stop() {
        brownNoisePlayer?.stop()
        audioEngine?.stop()
        audioEngine = nil
        brownNoisePlayer = nil
    }
    
    private func generateBrownNoiseBuffer() -> AVAudioPCMBuffer {
        // 43.1 kHz is a common sample rate for CD quality audio
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
}
