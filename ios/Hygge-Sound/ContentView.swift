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
        ZStack {
            Color.black.edgesIgnoringSafeArea(.all)
            
            Button(action: {
                if self.isPlaying {
                    self.brownNoisePlayer.stop()
                } else {
                    self.brownNoisePlayer.play()
                }
                self.isPlaying.toggle()
            }) {
                VStack {
                    Text(self.isPlaying ? "Stop Calm Sound" : "Play Calm Sound")
                        .font(.system(size: 24))
                        .fontWeight(.medium)
                        .foregroundColor(.black)
                        .padding(.bottom, 10)
                    Image("csicon")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 125, height: 125)
                }
                .padding()
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(Color(red: 245/255, green: 245/255, blue: 220/255))
                .edgesIgnoringSafeArea(.all)
                .cornerRadius(90)
                .padding(10)

            }
        }
    }
}
