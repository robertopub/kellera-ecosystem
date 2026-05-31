package com.example.kellera.services

import android.content.Context
import android.content.Intent
import android.net.Uri

class AppLauncherService(
    private val context: Context
) {

    fun openYouTube() {

        val intent =
            context.packageManager
                .getLaunchIntentForPackage(
                    "com.google.android.youtube"
                )

        if (intent != null) {

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(intent)

        } else {

            val webIntent =
                Intent(Intent.ACTION_VIEW)

            webIntent.data =
                Uri.parse(
                    "https://www.youtube.com"
                )

            webIntent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
            )

            context.startActivity(webIntent)
        }
    }

    fun openChrome() {

        val intent =
            context.packageManager
                .getLaunchIntentForPackage(
                    "com.android.chrome"
                )

        if (intent != null) {

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(intent)
        }
    }

    fun openWhatsApp() {

        val intent =
            context.packageManager
                .getLaunchIntentForPackage(
                    "com.whatsapp"
                )

        if (intent != null) {

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(intent)
        }
    }
}