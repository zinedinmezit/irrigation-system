package com.example.irrigationsystem.receivers

import android.app.*
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
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

    private lateinit var alarmIntent: PendingIntent

    override fun onReceive(context: Context, intent: Intent?) {

        val dao = IrrigationSystemDatabase.getInstance(context.applicationContext).IrrigationDatabaseDao
        repository = IrrigationRepository(dao)

        val myBundle = intent?.extras

        val chipIdsArray = myBundle?.getIntArray("CHIPS")
        val chipIds = chipIdsArray?.toMutableList()
        val timeString = myBundle?.getString("TIMESTRING")
        val ipAddress = myBundle?.getString("IPADDRESS")
        val schedulerId = myBundle?.getInt("SCHEDULERID")

        Log.i("Checkup","WateringReceiver/BundleValues (chipIdsArray, chipIds, timeString, ipAddress, schedulerId) : \n$chipIdsArray\n$chipIds\n$timeString\n$ipAddress\n$schedulerId")

        val pairs = DateHelper.getDateForCurrentSchedule(chipIds!!,timeString!!)

            scope.launch {
                repository.setWateringTimeNow(pairs.first.time,schedulerId!!)
            }

       val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmIntent = Intent(context, WateringReceiver::class.java).let {
            it.putExtra("CHIPS",chipIdsArray)
            it.putExtra("TIMESTRING",timeString)
            it.putExtra("IPADDRESS", ipAddress)
            it.putExtra("SCHEDULERID",schedulerId)
            PendingIntent.getBroadcast(context,1,it,FLAG_UPDATE_CURRENT)
        }

        setAlarmManager(alarmMgr,pairs.first.time,alarmIntent)

        Intent(context,WateringService::class.java).also {
            it.putExtra("IPADDRESS", ipAddress)
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(it)
            }
            else
            {
                context.startService(it)
            }
        }
    }

    private fun setAlarmManager(alarmManager : AlarmManager, dateTime : Long, notifyIntent: PendingIntent)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                dateTime,
                notifyIntent
            )
        }
        else{
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                dateTime,
                notifyIntent
            )
        }
    }
}