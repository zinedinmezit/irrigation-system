package com.example.irrigationsystem.receivers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.irrigationsystem.helpers.DateHelper
import com.example.irrigationsystem.services.WateringService

class WateringReceiver : BroadcastReceiver() {

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    //When onReceive is called, service is started that opens connection with websocket and sends signal to water
    //Also, new alarm manager is setup on the next day we chose
    override fun onReceive(context: Context?, intent: Intent?) {

        val myBundle = intent?.extras
        val chipIdsArray = myBundle?.getIntArray("CHIPS")
        val timeString = myBundle?.getString("TIMESTRING")


        val chipIds = chipIdsArray?.toMutableList()

        Intent(context,WateringService::class.java).also {
            context?.startService(it)
        }

        val pairs = DateHelper.getDateForCurrentSchedule(chipIds!!,timeString!!)

        alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, WateringReceiver::class.java).let {
            it.putExtra("CHIPS",chipIdsArray)
            it.putExtra("TIMESTRING",timeString)
            PendingIntent.getBroadcast(context,0,it,0)
        }

        setAlarmManager(alarmMgr,pairs.first.time,alarmIntent)
    }

    private fun setAlarmManager(alarmManager : AlarmManager?, dateTime : Long, intent: PendingIntent) {
        alarmManager?.set(
            AlarmManager.RTC_WAKEUP,
            dateTime,
            intent
        )
    }
}