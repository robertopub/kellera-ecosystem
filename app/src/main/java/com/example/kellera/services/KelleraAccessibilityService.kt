package com.example.kellera.services

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.speech.tts.TextToSpeech
import com.example.kellera.core.GlobalStateManager
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

            val className =
                event.className?.toString() ?: ""

            // ATUALIZA ESTADO GLOBAL
            GlobalStateManager.updateCurrentApp(packageName)
            GlobalStateManager.updateCurrentScreen(className)

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

                val msg = "Celular desbloqueado. Tela inicial detectada. O que deseja fazer agora?"
                if (GlobalStateManager.shouldAnnounce(msg)) {
                    falar(msg)
                }
            }

            // ==========================================
            // WHATSAPP
            // ==========================================

            else if (
                packageName.contains("whatsapp")
            ) {

                val msg = "WhatsApp aberto."
                if (GlobalStateManager.shouldAnnounce(msg)) {
                    falar(msg)
                }
            }

            // ==========================================
            // YOUTUBE
            // ==========================================

            else if (
                packageName.contains("youtube")
            ) {

                val msg = "YouTube aberto."
                if (GlobalStateManager.shouldAnnounce(msg)) {
                    falar(msg)
                }
            }

            // ==========================================
            // CHROME
            // ==========================================

            else if (
                packageName.contains("chrome")
            ) {

                val msg = "Google Chrome aberto."
                if (GlobalStateManager.shouldAnnounce(msg)) {
                    falar(msg)
                }
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