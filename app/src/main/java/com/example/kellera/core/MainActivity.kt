package com.example.kellera.core

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.kellera.biosystem.BiometricActivity
import java.util.Locale

class MainActivity : FragmentActivity() {
    private var currentMode = KelleraMode.VISION
    var spokenTextState = mutableStateOf("Toque no botão e fale...")
    var currentApp = mutableStateOf("")
    lateinit var tts: TextToSpeech

    lateinit var biometricPrompt: BiometricPrompt
    lateinit var promptInfo: BiometricPrompt.PromptInfo

    // 🔥 FUNÇÃO DE ESCUTA
    fun iniciarEscuta(context: Activity) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR")

        context.startActivityForResult(intent, 1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val router = KelleraRouter()

        router.startMode(currentMode)
        enableEdgeToEdge()

        // 🔐 BIOMETRIA
        val executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(
            this,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)

                    spokenTextState.value = "Celular desbloqueado"

                    tts.speak(
                        "Celular desbloqueado. O que deseja fazer agora?",
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        null
                    )
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()

                    spokenTextState.value = "Falha na autenticação"

                    tts.speak(
                        "Não reconheci você",
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        null
                    )
                }
            }
        )

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Kellera Security")
            .setSubtitle("Reconhecimento biométrico")
            .setNegativeButtonText("Cancelar")
            .build()

        // 🔥 ABRE BIOMETRIA AUTOMATICAMENTE
        biometricPrompt.authenticate(promptInfo)


        // 🔊 Inicializa voz
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale("pt", "BR")

                // 🔥 LISTENER (QUANDO TERMINA DE FALAR)
                tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {}

                    override fun onDone(utteranceId: String?) {
                        if (utteranceId == "ID_YOUTUBE") {
                            runOnUiThread {
                                iniciarEscuta(this@MainActivity)
                            }
                        }
                    }

                    override fun onError(utteranceId: String?) {}
                })

                tts.speak("Kellera iniciado", TextToSpeech.QUEUE_FLUSH, null, null)
            }
        }

        setContent {
            val spokenText = spokenTextState.value
            val context = LocalContext.current

            Column(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {

                Text(text = spokenText)

                Spacer(modifier = Modifier.Companion.height(20.dp))

                Button(onClick = {
                    iniciarEscuta(context as Activity)
                }) {
                    Text("Falar")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = result?.get(0)?.lowercase() ?: ""

            spokenTextState.value = spokenText

            tts.speak("Você disse $spokenText", TextToSpeech.QUEUE_FLUSH, null, null)

            // 🔥 PASSO 1 — ABRIR YOUTUBE
            // 🔥 ABRIR TELA BIOMÉTRICA
            if (
                spokenText.contains("biometria") ||
                spokenText.contains("batimentos") ||
                spokenText.contains("sensor")
            ) {

                spokenTextState.value = "Abrindo sistema biométrico..."

                tts.speak(
                    "Abrindo sistema biométrico",
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    null
                )

                val intent = Intent(this, BiometricActivity::class.java)
                startActivity(intent)
            }

            if (spokenText.contains("youtube") && currentApp.value != "youtube") {

                currentApp.value = "youtube"

                spokenTextState.value = "Abrindo YouTube..."

                tts.speak(
                    "YouTube aberto. O que você deseja fazer agora?",
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    "ID_YOUTUBE" // 🔥 ESSENCIAL
                )

                val intent = packageManager.getLaunchIntentForPackage("com.google.android.youtube")

                if (intent != null) {
                    startActivity(intent)
                } else {
                    val webIntent = Intent(Intent.ACTION_VIEW)
                    webIntent.data = Uri.parse("https://www.youtube.com")
                    startActivity(webIntent)
                }
            }

            // 🔥 PASSO 2 — COMANDOS DENTRO DO YOUTUBE
            else if (currentApp.value == "youtube") {

                val query = spokenText
                    .replace("abrir", "")
                    .replace("youtube", "")
                    .replace("vídeo", "")
                    .replace("video", "")
                val searchIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/results?search_query=$query")
                )

                startActivity(searchIntent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        tts.stop()
        tts.shutdown()
    }
}