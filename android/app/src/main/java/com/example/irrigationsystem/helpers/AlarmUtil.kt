package com.example.irrigationsystem.helpers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.os.Build

class AlarmUtil(context : Context) {

    private val alarmManager : AlarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager

    fun scheduleAlarmManager(dateTime : Long, pendingIntent : PendingIntent){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                dateTime,
                pendingIntent
            )
        }
        else{
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                dateTime,
                pendingIntent
            )
        }
    }
}