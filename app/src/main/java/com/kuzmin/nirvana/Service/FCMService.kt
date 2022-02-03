package com.kuzmin.nirvana.Service

import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kuzmin.nirvana.other.Helper

class FCMService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        val recipientId = message.data["recipientId"]
        val title = message.data["title"]
        val body = message.data["body"]


        println(body)
        println(recipientId)
        println(title)
        try {
            if (title != null) {
                if (recipientId != null) {
                    if(recipientId.toInt() == -1){
                        Helper.welcomUser(this, title)
                    }else{
                        Helper.postIsLike(this, title, recipientId.toLong())
                    }

                }
            }
        }catch (e:Exception){
            Toast.makeText(this , "ERROR", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onNewToken(token: String) {
        println(token)
    }
}