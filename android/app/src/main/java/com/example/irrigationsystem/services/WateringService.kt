package com.example.irrigationsystem.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.irrigationsystem.R
import com.example.irrigationsystem.database.IrrigationSystemDatabase
import com.example.irrigationsystem.network.OkHttpProvider
import com.example.irrigationsystem.repositories.IrrigationRepository
import kotlinx.coroutines.*
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WateringService : Service() {

    private val scope = CoroutineScope(context = Dispatchers.IO)

    
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {

        createChannel(getString(R.string.is_notification_channel_id),
                      getString(R.string.is_notification_channel_name))

        val notification: Notification = NotificationCompat.Builder(this,applicationContext.getString(R.string.is_notification_channel_id))
            .setContentTitle("Irrigation system service")
            .setContentText("Watering action started")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .build()

        startForeground(1,notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val myBundle = intent?.extras
        val address = myBundle?.getString("IPADDRESS")!!
        val wateringDurationValue = myBundle.getLong("WATERINGDURATION")

        val wsListener : WebSocketListener = object :WebSocketListener(){

            override fun onOpen(webSocket: WebSocket, response: Response) {
                webSocket.send(wateringDurationValue.toString())
                webSocket.close(1000,null)
            }

            /* override fun onMessage(webSocket: WebSocket, text: String) {
             }

             override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {

             }*/
        }

        scope.launch {
            OkHttpProvider.openWebSocketConnection(wsListener, address)
            stopForeground(false)
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel("Scope in service is done so it is canceled")
    }

    private fun createChannel(channelId : String, channelName : String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "You got things to do"

            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }
}