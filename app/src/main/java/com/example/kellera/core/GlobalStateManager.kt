package com.example.kellera.core

import android.util.Log

/**
 * GlobalStateManager is a singleton that maintains the shared state of the KELLERA system.
 * It integrates various modules like MainActivity, KelleraAccessibilityService, and VoiceEngine.
 */
object GlobalStateManager {
    @Volatile
    var userName: String = "Usuário"
        private set

    @Volatile
    var currentApp: String = ""
        private set

    @Volatile
    var currentScreen: String = ""
        private set

    @Volatile
    var lastCommand: String = ""
        private set

    @Volatile
    var conversationState: String = "IDLE"
        private set

    @Volatile
    var lastAnnouncement: String = ""
        private set

    fun updateUserName(name: String) {
        userName = name
        try {
            Log.d("GlobalStateManager", "UserName updated: $name")
        } catch (e: Exception) {}
    }

    fun updateCurrentApp(app: String) {
        currentApp = app
        try {
            Log.d("GlobalStateManager", "CurrentApp updated: $app")
        } catch (e: Exception) {}
    }

    fun updateCurrentScreen(screen: String) {
        currentScreen = screen
        try {
            Log.d("GlobalStateManager", "CurrentScreen updated: $screen")
        } catch (e: Exception) {}
    }

    fun updateLastCommand(command: String) {
        lastCommand = command
        try {
            Log.d("GlobalStateManager", "LastCommand updated: $command")
        } catch (e: Exception) {}
    }

    fun updateConversationState(state: String) {
        conversationState = state
        try {
            Log.d("GlobalStateManager", "ConversationState updated: $state")
        } catch (e: Exception) {}
    }

    /**
     * Checks if the given announcement is different from the last one to prevent loops.
     * Updates [lastAnnouncement] if it's new.
     */
    fun shouldAnnounce(announcement: String): Boolean {
        synchronized(this) {
            if (announcement == lastAnnouncement) {
                return false
            }
            lastAnnouncement = announcement
            try {
                Log.d("GlobalStateManager", "New announcement registered: $announcement")
            } catch (e: Exception) {}
            return true
        }
    }

    fun clearLastAnnouncement() {
        synchronized(this) {
            lastAnnouncement = ""
        }
    }
}
