package com.example.kellera.capabilities

import com.example.kellera.core.KelleraMode

class ContextManager {

    private var currentMode = KelleraMode.VISION

    fun updateMode(mode: KelleraMode) {

        currentMode = mode

        println("Contexto atualizado para: $mode")
    }

    fun getCurrentMode(): KelleraMode {

        return currentMode
    }

    fun analyzeContext() {

        when (currentMode) {

            KelleraMode.VISION -> {

                println("Analisando contexto visual")
            }

            KelleraMode.BIOSYSTEM -> {

                println("Analisando contexto biométrico")
            }

            KelleraMode.MOBILITY -> {

                println("Analisando mobilidade")
            }

            KelleraMode.EMERGENCY -> {

                println("Analisando situação de emergência")
            }

            KelleraMode.HOME -> {

                println("Analisando contexto residencial")
            }
        }
    }
}