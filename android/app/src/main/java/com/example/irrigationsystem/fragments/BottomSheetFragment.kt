package com.example.irrigationsystem.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.irrigationsystem.databinding.BottomsheetPlansBinding
import com.example.irrigationsystem.helpers.DateHelper
import com.example.irrigationsystem.helpers.DeleteListener
import com.example.irrigationsystem.helpers.PlanAdapter
import com.example.irrigationsystem.helpers.PlanListener
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.models.PlanWateringSchedulerView
import com.example.irrigationsystem.models.WateringScheduler
import com.example.irrigationsystem.receivers.WateringReceiver
import com.example.irrigationsystem.viewmodels.BottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class BottomSheetFragment() : BottomSheetDialogFragment() {

    var  listOfPlans : List<Plan>? = null
     var webSocketIpAddress : String? = null

    var planSchedulerView : PlanWateringSchedulerView? = null
    var days : List<Int>? = null

    private lateinit var binding : BottomsheetPlansBinding

    private val model: BottomSheetViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = BottomsheetPlansBinding.inflate(inflater, container, false)

        val adapter = PlanAdapter(PlanListener {
            if(!it.IsActive) {
                model.changePlanActiveStatusExceptOne(it.PlanId)
                model.setPlanAsActive(it.PlanId)
            }

        }, DeleteListener {
            model.deletePlan(it)
            this.dismiss()
        })
        binding.planRecyclerview.adapter = adapter

        adapter.submitList(listOfPlans)

        model.planSchedulerView.observe(viewLifecycleOwner, {
            planSchedulerView = it
        })

        model.scheduledDays.observe(viewLifecycleOwner, {
            days = it
        })

        model.isFetched.observe(viewLifecycleOwner, {
            if(it){
                if(planSchedulerView != null && days != null){

                    val pair = DateHelper.getDateForCurrentSchedule(days?.toMutableList()!!,planSchedulerView?.TimeString!!)

                    Log.i("Checkup","BottomSheetFragment/Ids(scheduler, days) : \n$planSchedulerView \n$days")

                    val alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val alarmIntent = Intent(context, WateringReceiver::class.java).let { intent ->
                        intent.putExtra("CHIPS",days?.toIntArray())
                        intent.putExtra("TIMESTRING",planSchedulerView?.TimeString!!)
                        intent.putExtra("IPADDRESS", webSocketIpAddress!!)
                        intent.putExtra("SCHEDULERID", planSchedulerView?.WateringSchedulerId)
                        PendingIntent.getBroadcast(context,173839173,intent, PendingIntent.FLAG_UPDATE_CURRENT)
                    }

                    model.setWateringTImeNow(pair.first.time, planSchedulerView?.WateringSchedulerId!!)

                    setAlarmManager(alarmMgr, pair.first.time, alarmIntent)

                    model.fetchedToFalse()
                    this.dismiss()
                }
            }
        })

        return binding.root
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