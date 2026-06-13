package com.example.kellera.security

import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.kellera.core.GlobalStateManager

class KelleraAccessManager(
    private val activity: FragmentActivity
) {

    fun authenticate(
        onSuccess: () -> Unit
    ) {

        val executor =
            ContextCompat.getMainExecutor(activity)

        val biometricPrompt =
            BiometricPrompt(
                activity,
                executor,

                object : BiometricPrompt.AuthenticationCallback() {

                    override fun onAuthenticationSucceeded(
                        result: BiometricPrompt.AuthenticationResult
                    ) {

                        super.onAuthenticationSucceeded(result)

                        GlobalStateManager.updateConversationState("AUTHENTICATED")
                        onSuccess()
                    }
                }
            )

        val promptInfo =
            BiometricPrompt.PromptInfo.Builder()

                .setTitle("KELLERA ACCESS")

                .setSubtitle(
                    "Autentique para iniciar o KELLERA"
                )

                .setNegativeButtonText("Cancelar")

                .build()

        biometricPrompt.authenticate(promptInfo)
    }
}