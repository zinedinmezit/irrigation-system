package com.example.irrigationsystem.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.irrigationsystem.receivers.WateringReceiver

fun AlarmManager.scheduleWatering(context: Context, chipsArray : IntArray, timeString : String, scheduledDateLong : Long, ipAdress : String){

   val alarmIntent = Intent(context, WateringReceiver::class.java).let {
        it.putExtra("CHIPS",chipsArray)
        it.putExtra("TIMESTRING",timeString)
       it.putExtra("IPADDRESS", ipAdress)
        PendingIntent.getBroadcast(context,1,it, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    this.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        scheduledDateLong,
        alarmIntent
    )
}