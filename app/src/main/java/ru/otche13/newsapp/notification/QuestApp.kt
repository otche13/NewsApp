package ru.otche13.newsapp.notification

import android.app.Application
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging


class QuestApp : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {

                return@addOnCompleteListener
            }

            val token = task.result
            Log.d("TAG","$token" )

        }
    }
}
