package com.example.kellera.services

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale

class VoiceEngine(
    context: Context
) {

    private var tts: TextToSpeech

    init {

        tts = TextToSpeech(
            context
        ) { status ->

            if (
                status ==
                TextToSpeech.SUCCESS
            ) {

                tts.language =
                    Locale("pt", "BR")
            }
        }
    }

    fun speak(
        text: String
    ) {

        tts.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            null
        )
    }

    fun speakAndCallback(
        text: String,
        onDone: () -> Unit
    ) {

        val utteranceId =
            "KELLERA_CALLBACK"

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

                    onDone()
                }

                override fun onError(
                    utteranceId: String?
                ) {
                }
            }
        )

        tts.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            utteranceId
        )
    }

    fun stop() {

        tts.stop()

        tts.shutdown()
    }
}