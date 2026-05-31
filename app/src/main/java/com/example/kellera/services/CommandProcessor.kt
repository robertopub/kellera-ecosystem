package com.example.kellera.services

import com.example.kellera.services.VoiceEngine
import android.content.Context

class CommandProcessor(
    private val context: Context,
    private val voiceEngine: VoiceEngine
) {

    private val appLauncherService =
        AppLauncherService(context)

    fun processCommand(command: String) {

        when {

            command.contains("youtube") -> {

                voiceEngine.speak("Abrindo YouTube")

                appLauncherService.openYouTube()
            }

            command.contains("chrome") -> {

                voiceEngine.speak("Abrindo Chrome")

                appLauncherService.openChrome()
            }

            command.contains("whatsapp") -> {

                voiceEngine.speak("Abrindo WhatsApp")

                appLauncherService.openWhatsApp()
            }

            command.contains("biometria") -> {

                voiceEngine.speak("Abrindo sistema biométrico")
            }

            else -> {

                voiceEngine.speak("Não entendi o comando")
            }
        }
    }
}