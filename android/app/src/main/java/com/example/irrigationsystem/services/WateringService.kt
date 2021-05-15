package com.example.irrigationsystem.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.irrigationsystem.R
import com.example.irrigationsystem.network.OkHttpProvider
import kotlinx.coroutines.*
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.internal.notify
import java.text.SimpleDateFormat
import java.util.*

class WateringService : Service() {

    private val scope = CoroutineScope(context = Dispatchers.IO)
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

    
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {

        createChannel(getString(R.string.is_notification_channel_id),
                      getString(R.string.is_notification_channel_name),
                      getString(R.string.is_notification_channel_id2),
                      getString(R.string.is_notification_channel_name2))

        val notification1 : Notification = NotificationCompat.Builder(this,applicationContext.getString(R.string.is_notification_channel_id))
            .setContentTitle("Irrigation system service")
            .setContentText("Watering action started")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .build()

        val notificationSummaryBuilder  = NotificationCompat.Builder(this,applicationContext.getString(R.string.is_notification_channel_id2))
            .setContentTitle("Watering action summary")
            .setContentText("Watering action executed at ${dateFormat.format(Calendar.getInstance().time)}")
            .setSmallIcon(R.drawable.ic_baseline_broken_image_24)
            .setGroup("WATERING_GROUP")
            .setOngoing(false)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(Calendar.getInstance().get(Calendar.DATE), notificationSummaryBuilder.build())
        }

        startForeground(1, notification1)

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val myBundle = intent?.extras
        val address = myBundle?.getString("IPADDRESS")!!
        val wateringDurationValue = myBundle.getLong("WATERINGDURATION")
        Log.i("serviceTest", "Address - $address")
        Log.i("serviceTest", "WateringDurationValue - $wateringDurationValue")


        val wsListener : WebSocketListener = object :WebSocketListener(){

            override fun onOpen(webSocket: WebSocket, response: Response) {
                webSocket.send(wateringDurationValue.toString())
                Log.i("serviceTest", "wsListener - onOpen - wateringDurationValue - $wateringDurationValue")
                webSocket.close(1000,null)
            }
        }

        scope.launch {
            Log.i("serviceTest", "before Opening - Address - $address")
            OkHttpProvider.openWebSocketConnection(wsListener, address)
            stopForeground(false)
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel("Scope in service is done so it is canceled")
    }

    private fun createChannel(channelId1 : String, channelName1 : String, channelId2 : String, channelName2 : String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel1 = NotificationChannel(
                channelId1,
                channelName1,
                NotificationManager.IMPORTANCE_HIGH
            ).also {
                it.enableLights(true)
                it.lightColor = Color.RED
                it.enableVibration(true)
                it.description = "Watering service fired up"
            }

            val notificationChannel2 = NotificationChannel(
                channelId2,
                channelName2,
                NotificationManager.IMPORTANCE_HIGH
            ).also {
                it.enableLights(true)
                it.lightColor = Color.RED
                it.enableVibration(false)
                it.description = "Watering service time representation"
            }

            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager?.createNotificationChannels(listOf(notificationChannel1,notificationChannel2))
        }
    }
}