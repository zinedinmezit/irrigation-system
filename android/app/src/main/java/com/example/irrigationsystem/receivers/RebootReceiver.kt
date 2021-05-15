package com.example.irrigationsystem.receivers

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.irrigationsystem.IrrigationApplication
import com.example.irrigationsystem.helpers.AlarmUtil
import com.example.irrigationsystem.helpers.DateDaysHelper
import kotlinx.coroutines.launch

class RebootReceiver : BroadcastReceiver() {

    private lateinit var alarmManager : AlarmUtil

    override fun onReceive(context: Context, intent: Intent) {

        alarmManager = AlarmUtil(context)
        val irrigationApp = context.applicationContext as IrrigationApplication
        val receiverRepository = irrigationApp.receiverRepository

        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            // Set the alarm here.
            irrigationApp.applicationScopeIO.launch {
                val scheduledDays = receiverRepository.getScheduledDays()
                val planScheduler = receiverRepository.getPlanSchedulerView()
                val setupInfo = receiverRepository.getSetupInfo()

                val pair = DateDaysHelper.getDateForCurrentSchedule(scheduledDays.toMutableList(),planScheduler.TimeString)


                val alarmIntent = Intent(context, WateringReceiver::class.java).let { intent ->
                    intent.putExtra("CHIPS",scheduledDays.toIntArray())
                    intent.putExtra("TIMESTRING",planScheduler.TimeString)
                    intent.putExtra("IPADDRESS", setupInfo.IpAddress)
                    intent.putExtra("SCHEDULERID", planScheduler.WateringSchedulerId)
                    intent.putExtra("WATERINGDURATION", planScheduler.WateringDuration)
                    PendingIntent.getBroadcast(context,173839173,intent, PendingIntent.FLAG_UPDATE_CURRENT)
                }

                alarmManager.scheduleAlarmManager(pair.first.time, alarmIntent)
            }

        }
    }
}