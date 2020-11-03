package com.example.irrigationsystem.receivers

import android.app.*
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.irrigationsystem.database.IrrigationSystemDatabase
import com.example.irrigationsystem.helpers.DateHelper
import com.example.irrigationsystem.repositories.IrrigationRepository
import com.example.irrigationsystem.services.WateringService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WateringReceiver : BroadcastReceiver() {

    private val scope = CoroutineScope(context = Dispatchers.IO)
    private lateinit var repository : IrrigationRepository

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent


    //When onReceive is called, service is started that opens connection with websocket and sends signal to water
    //Also, new alarm manager is setup on the next day we chose
    override fun onReceive(context: Context, intent: Intent?) {

        val dao = IrrigationSystemDatabase.getInstance(context.applicationContext).IrrigationDatabaseDao
        repository = IrrigationRepository(dao)

        val myBundle = intent?.extras

        val chipIdsArray = myBundle?.getIntArray("CHIPS")
        val chipIds = chipIdsArray?.toMutableList()
        val timeString = myBundle?.getString("TIMESTRING")

        val pairs = DateHelper.getDateForCurrentSchedule(chipIds!!,timeString!!)

            scope.launch {
                repository.setWateringTimeNow(pairs.first.time)
            }

        alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, WateringReceiver::class.java).let {
            it.putExtra("CHIPS",chipIdsArray)
            it.putExtra("TIMESTRING",timeString)
            PendingIntent.getBroadcast(context,1,it,FLAG_UPDATE_CURRENT)
        }

        setAlarmManager(alarmMgr,pairs.first.time,alarmIntent)

        Intent(context,WateringService::class.java).also {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                context.startForegroundService(it)
            else
            {
                context.startService(it)
            }
        }
    }

    private fun setAlarmManager(alarmManager : AlarmManager?, dateTime : Long, intent: PendingIntent) {
        alarmManager?.set(
            AlarmManager.RTC_WAKEUP,
            dateTime,
            intent
        )
    }
}