# KELLERA Architecture Integration

This document describes the integration of the KELLERA Android architecture using a centralized `GlobalStateManager`.

## Existing Components

The following components were already implemented in the project:

- **MainActivity**: The main entry point, handling voice recognition and user interface.
- **KelleraAccessibilityService**: Monitors window state changes and provides voice feedback based on the current app.
- **VoiceEngine**: Wraps Android's TextToSpeech (TTS) for simplified voice output.
- **CommandProcessor**: Processes recognized voice commands and delegates actions.
- **AppLauncherService**: Handles opening external applications like WhatsApp, YouTube, and Chrome.
- **KelleraAccessManager**: Manages biometric authentication.
- **ContextManager**: Analyzes the current operating mode (Vision, Biosystem, etc.).

## New Component: GlobalStateManager

The `GlobalStateManager` is a thread-safe singleton that serves as the "source of truth" for the entire application. It stores:

- `userName`: The name of the current user.
- `currentApp`: The package name of the application currently in the foreground.
- `currentScreen`: The class name of the current activity/screen.
- `lastCommand`: The last voice command recognized by the system.
- `conversationState`: The current state of the interaction (e.g., IDLE, AUTHENTICATED, PROCESSING_COMMAND, AWAITING_INPUT).
- `lastAnnouncement`: The last text spoken by the system, used to prevent redundant loops.

## Integration Points

### 1. Unified State Updates
- **KelleraAccessibilityService** updates `currentApp` and `currentScreen` whenever a `TYPE_WINDOW_STATE_CHANGED` event occurs.
- **MainActivity** and **CommandProcessor** update `lastCommand` and `conversationState`.
- **KelleraAccessManager** updates `conversationState` upon successful biometric authentication.

### 2. Prevention of Redundant Announcements
The `GlobalStateManager.shouldAnnounce(text)` method is used by both `KelleraAccessibilityService` and `VoiceEngine` to ensure that the same message is not repeated consecutively. This prevents "accessibility loops" where a window change might trigger multiple redundant voice feedbacks.

### 3. Synchronization
All components now coordinate through the `GlobalStateManager`, allowing for a more cohesive user experience. For example, `VoiceEngine` automatically tracks what was last said, regardless of which component requested the speech.

## Modified Files

- `app/src/main/java/com/example/kellera/core/GlobalStateManager.kt` (New)
- `app/src/main/java/com/example/kellera/core/MainActivity.kt`
- `app/src/main/java/com/example/kellera/services/KelleraAccessibilityService.kt`
- `app/src/main/java/com/example/kellera/services/VoiceEngine.kt`
- `app/src/main/java/com/example/kellera/services/CommandProcessor.kt`
- `app/src/main/java/com/example/kellera/security/KelleraAccessManager.kt`
