package com.example.irrigationsystem.helpers


import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.irrigationsystem.R

class NotificationBuilder(val context: Context, val channelId:String) {

    fun buildNotification(textTitle:String, textContent:String){
        var builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
    }

}

