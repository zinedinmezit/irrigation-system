package com.example.irrigationsystem

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        createChannel(getString(R.string.is_notification_channel_id),
            getString(R.string.is_notification_channel_name),
            getString(R.string.is_notification_channel_id2),
            getString(R.string.is_notification_channel_name2))

        setContentView(R.layout.activity_main)
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

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if(currentFocus != null){
            val imm : InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken,0)
        }
        return super.dispatchTouchEvent(ev)
    }

}