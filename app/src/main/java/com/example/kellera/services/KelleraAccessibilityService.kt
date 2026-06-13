package com.example.kellera.services

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.speech.tts.TextToSpeech
import java.util.Locale

class KelleraAccessibilityService : AccessibilityService() {

    private lateinit var tts: TextToSpeech

    private var ultimaTela = ""

    override fun onServiceConnected() {

        super.onServiceConnected()

        tts = TextToSpeech(this) { status ->

            if (status == TextToSpeech.SUCCESS) {

                tts.language = Locale("pt", "BR")

            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        if (event == null) return

        // ==========================================
        // DETECTA TROCA DE TELA
        // ==========================================

        if (
            event.eventType ==
            AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
        ) {

            val packageName =
                event.packageName?.toString() ?: return

            // EVITA REPETIR
            if (packageName == ultimaTela) return

            ultimaTela = packageName

            // ==========================================
            // HOME / LAUNCHER
            // ==========================================

            if (

                packageName.contains("launcher") ||
                packageName.contains("home")

            ) {

                falar(

                    "Celular desbloqueado. Tela inicial detectada. O que deseja fazer agora?"

                )
            }

            // ==========================================
            // WHATSAPP
            // ==========================================

            else if (
                packageName.contains("whatsapp")
            ) {

                falar(
                    "WhatsApp aberto."
                )
            }

            // ==========================================
            // YOUTUBE
            // ==========================================

            else if (
                packageName.contains("youtube")
            ) {

                falar(
                    "YouTube aberto."
                )
            }

            // ==========================================
            // CHROME
            // ==========================================

            else if (
                packageName.contains("chrome")
            ) {

                falar(
                    "Google Chrome aberto."
                )
            }
        }
    }

    // ==========================================
    // FALA
    // ==========================================

    private fun falar(texto: String) {

        tts.speak(
            texto,
            TextToSpeech.QUEUE_FLUSH,
            null,
            null
        )
    }

    override fun onInterrupt() {

    }

    override fun onDestroy() {

        super.onDestroy()

        tts.stop()

        tts.shutdown()
    }
}