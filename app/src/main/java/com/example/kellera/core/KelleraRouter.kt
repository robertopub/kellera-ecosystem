package com.example.kellera.core

class KelleraRouter {

    fun startMode(mode: KelleraMode) {

        when (mode) {

            KelleraMode.VISION -> {
                println("Iniciando modo Vision")
            }

            KelleraMode.BIOSYSTEM -> {
                println("Iniciando modo BioSystem")
            }

            KelleraMode.HOME -> {
                println("Iniciando modo Home")
            }

            KelleraMode.MOBILITY -> {
                println("Iniciando modo Mobility")
            }

            KelleraMode.EMERGENCY -> {
                println("Iniciando modo Emergency")
            }
        }
    }
}