package com.example.kellera.core

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.example.kellera.biosystem.BiometricActivity
import com.example.kellera.capabilities.CapabilityManager
import com.example.kellera.capabilities.ContextManager
import com.example.kellera.security.KelleraAccessManager
import com.example.kellera.services.CommandProcessor
import com.example.kellera.services.VoiceEngine
import com.example.kellera.vision.VisionEngine
import java.util.Locale

class MainActivity : FragmentActivity() {

    private var currentMode =
        KelleraMode.VISION

    private lateinit var voiceEngine:
            VoiceEngine

    private lateinit var accessManager:
            KelleraAccessManager

    private lateinit var visionEngine:
            VisionEngine

    private lateinit var capabilityManager:
            CapabilityManager

    private lateinit var contextManager:
            ContextManager

    private lateinit var commandProcessor:
            CommandProcessor

    var spokenTextState =
        mutableStateOf(
            "KELLERA iniciado..."
        )

    var currentApp =
        mutableStateOf("")

    lateinit var tts: TextToSpeech

    // =====================================================
    // ESCUTA
    // =====================================================

    fun iniciarEscuta(
        context: Activity
    ) {

        val intent =
            Intent(
                RecognizerIntent
                    .ACTION_RECOGNIZE_SPEECH
            )

        intent.putExtra(
            RecognizerIntent
                .EXTRA_LANGUAGE_MODEL,

            RecognizerIntent
                .LANGUAGE_MODEL_FREE_FORM
        )

        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            "pt-BR"
        )

        context.startActivityForResult(
            intent,
            1
        )
    }

    // =====================================================
    // ON CREATE
    // =====================================================

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        // 🌍 GLOBAL STATE
        GlobalStateManager.updateUserName("Usuário")

        // 🔊 VOICE ENGINE
        voiceEngine =
            VoiceEngine(this)

        // 🔐 ACCESS MANAGER
        accessManager =
            KelleraAccessManager(this)

        accessManager.authenticate {

            val welcomeMsg = "Celular desbloqueado. O que deseja fazer agora?"
            if (GlobalStateManager.shouldAnnounce(welcomeMsg)) {
                voiceEngine.speakAndCallback(welcomeMsg) {
                    runOnUiThread {
                        iniciarEscuta(this)
                    }
                }
            } else {
                runOnUiThread {
                    iniciarEscuta(this)
                }
            }
        }

        // 🔥 COMMAND PROCESSOR
        commandProcessor =
            CommandProcessor(
                this,
                voiceEngine
            )

        // 🔥 ENGINES
        visionEngine =
            VisionEngine()

        capabilityManager =
            CapabilityManager()

        contextManager =
            ContextManager()

        val router =
            KelleraRouter()

        // 🔥 FLUXO
        router.startMode(currentMode)

        capabilityManager.route(
            currentMode
        )

        contextManager.updateMode(
            currentMode
        )

        contextManager.analyzeContext()

        visionEngine.start()

        val startMsg = "Kellera iniciado"
        if (GlobalStateManager.shouldAnnounce(startMsg)) {
            voiceEngine.speak(startMsg)
        }

        enableEdgeToEdge()

        // =====================================================
        // TTS
        // =====================================================

        tts = TextToSpeech(this) { status ->

            if (
                status ==
                TextToSpeech.SUCCESS
            ) {

                tts.language =
                    Locale("pt", "BR")

                tts.setOnUtteranceProgressListener(

                    object :
                        UtteranceProgressListener() {

                        override fun onStart(
                            utteranceId: String?
                        ) {
                        }

                        override fun onDone(
                            utteranceId: String?
                        ) {

                            if (
                                utteranceId ==
                                "ID_YOUTUBE"
                            ) {

                                runOnUiThread {

                                    iniciarEscuta(
                                        this@MainActivity
                                    )
                                }
                            }
                        }

                        override fun onError(
                            utteranceId: String?
                        ) {
                        }
                    }
                )
            }
        }

        // =====================================================
        // INTERFACE
        // =====================================================

        setContent {

            val spokenText =
                spokenTextState.value

            Column(

                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),

                verticalArrangement =
                    Arrangement.Center

            ) {

                Text(
                    text = spokenText
                )
            }
        }
    }

    // =====================================================
    // RESULTADO VOZ
    // =====================================================

    override fun onActivityResult(

        requestCode: Int,
        resultCode: Int,
        data: Intent?

    ) {

        super.onActivityResult(

            requestCode,
            resultCode,
            data

        )

        if (

            requestCode == 1 &&
            resultCode == RESULT_OK

        ) {

            val result =

                data?.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS
                )

            val spokenText =

                result?.get(0)
                    ?.lowercase() ?: ""

            // 🔥 PROCESSA COMANDO
            GlobalStateManager.updateLastCommand(spokenText)
            GlobalStateManager.updateConversationState("PROCESSING_COMMAND")

            commandProcessor.processCommand(
                spokenText
            )

            spokenTextState.value =
                spokenText

            Log.d(
                "KELLERA",
                "COMANDO RECEBIDO: $spokenText"
            )

            // 🔥 CONVERSA CONTÍNUA
            val reply = "Você disse $spokenText. O que deseja fazer agora?"
            GlobalStateManager.updateConversationState("AWAITING_INPUT")

            voiceEngine.speakAndCallback(reply) {
                runOnUiThread {
                    iniciarEscuta(this)
                }
            }

            // 🔥 BIOSYSTEM
            if (

                spokenText.contains(
                    "biometria"
                ) ||

                spokenText.contains(
                    "batimentos"
                ) ||

                spokenText.contains(
                    "sensor"
                )

            ) {

                spokenTextState.value =
                    "Abrindo sistema biométrico..."

                voiceEngine.speak(
                    "Abrindo sistema biométrico"
                )

                val intent =

                    Intent(
                        this,
                        BiometricActivity::class.java
                    )

                startActivity(intent)
            }
        }
    }

    // =====================================================
    // DESTROY
    // =====================================================

    override fun onDestroy() {

        super.onDestroy()

        tts.stop()

        tts.shutdown()
    }
}