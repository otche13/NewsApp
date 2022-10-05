package ru.otche13.newsapp.notification


import com.google.firebase.messaging.FirebaseMessagingService


class MyFirebaseMessagingService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
       super.onNewToken(token)
    }

}