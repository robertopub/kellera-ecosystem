package com.example.kellera.services

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.view.accessibility.AccessibilityEvent

class KelleraAccessibilityService :
    AccessibilityService() {

    override fun onAccessibilityEvent(
        event: AccessibilityEvent?
    ) {
    }

    override fun onInterrupt() {
    }

    fun performUnlockSwipe() {

        val path = Path()

        path.moveTo(
            500f,
            1800f
        )

        path.lineTo(
            500f,
            300f
        )

        val gesture =
            GestureDescription.Builder()

                .addStroke(

                    GestureDescription.StrokeDescription(
                        path,
                        0,
                        300
                    )
                )

                .build()

        dispatchGesture(
            gesture,
            null,
            null
        )
    }
}