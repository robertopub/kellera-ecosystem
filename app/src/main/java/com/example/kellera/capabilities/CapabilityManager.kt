package com.example.kellera.capabilities

import com.example.kellera.core.KelleraMode

class CapabilityManager {

    fun route(mode: KelleraMode) {

        when (mode) {

            KelleraMode.VISION -> {

                println("Capabilities do Vision ativadas")
            }

            KelleraMode.BIOSYSTEM -> {

                println("Capabilities do BioSystem ativadas")
            }

            KelleraMode.HOME -> {

                println("Capabilities do Home ativadas")
            }

            KelleraMode.MOBILITY -> {

                println("Capabilities do Mobility ativadas")
            }

            KelleraMode.EMERGENCY -> {

                println("Capabilities do Emergency ativadas")
            }
        }
    }
}