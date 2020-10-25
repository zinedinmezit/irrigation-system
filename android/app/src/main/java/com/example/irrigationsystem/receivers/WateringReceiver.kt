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
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("servicetest","Receiver - onRecieve start")
        val myBundle = intent?.extras
        val chipIdsArray = myBundle?.getIntArray("CHIPS")
        val timeString = myBundle?.getString("TIMESTRING")

        Log.i("servicetest","Receiver - chipIdsArray $chipIdsArray")
        Log.i("servicetest","Receiver - timeString $timeString")
        val chipIds = chipIdsArray?.toMutableList()
        Log.i("servicetest","Receiver - chipIdsMutableList $chipIds")

        Intent(context,WateringService::class.java).also {
            Log.i("servicetest","Receiver - inside intent before starting service")
            context?.startService(it)
            Log.i("servicetest","Receiver - inside intent after starting service")
        }
        Log.i("servicetest","Receiver - outside starting service")

        val pairs = DateHelper.getDateForCurrentSchedule(chipIds!!,timeString!!)
        Log.i("servicetest","Receiver - pairs values - ${pairs.first} ${pairs.second}")
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