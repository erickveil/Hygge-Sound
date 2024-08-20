//
//  ContentView.swift
//  Hygge-Sound
//
//  Created by Erick Veil on 8/16/24.
//

import SwiftUI
import AVFoundation

struct ContentView: View {
    // @StateObject This property wrapper makes it so that the instance is
    // created once and remains consistent across view updates, preserving
    // its state.
    @StateObject private var brownNoisePlayer = BrownNoisePlayer()
    @State private var isPlaying = false
    
    var body: some View {
        Button(action: {
            if self.isPlaying {
                self.brownNoisePlayer.stop()
            } else {
                self.brownNoisePlayer.play()
            }
            self.isPlaying.toggle()
        }) {
            Text(self.isPlaying ? "Stop Calm Sound" : "Play Calm Sound")
                .font(.largeTitle)
                .foregroundColor(.white)
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(Color.blue)
                .edgesIgnoringSafeArea(.all)
        }
    }
}
