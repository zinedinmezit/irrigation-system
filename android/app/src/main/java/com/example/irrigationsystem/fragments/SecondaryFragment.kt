package com.example.irrigationsystem.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.irrigationsystem.R
import com.example.irrigationsystem.databinding.FragmentSecondaryBinding
import com.example.irrigationsystem.helpers.DateHelper
import com.example.irrigationsystem.helpers.TypeConverters
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.receivers.WateringReceiver
import com.example.irrigationsystem.viewmodels.SecondaryViewModel
import java.util.*

class SecondaryFragment : Fragment() {

    lateinit var binding : FragmentSecondaryBinding
    private val model : SecondaryViewModel by activityViewModels()

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_secondary, container, false)

        binding.editTextTime.setOnClickListener {
            TimePickerFragment().show(parentFragmentManager,"timePicker")
        }

        binding.button.setOnClickListener{


            val checkedChipsIds = DateHelper.transformListIds(binding.chipGroup.checkedChipIds)

            val planName : String = binding.textFieldText.text.toString()
            val timeString = binding.editTextTime.text.toString()
            if(checkedChipsIds.count() > 0) {
                val chipsIntArray = checkedChipsIds.toIntArray()
                Log.i("servicetest","Secondary fragment, chipsIntArray $chipsIntArray")
                lifecycleScope.launchWhenStarted {

                    val plan = Plan(
                        Name = planName,
                        IsActive = true
                    )

                    model.insertNote(plan)
                    val planId = model.getLatestPlanId().toInt()
                   val scheduledDate = model.insertWateringScheduler(checkedChipsIds, timeString, planId)
                    val wateringSchedulerId = model.getLatestWateringSchedulerId().toInt()
                    Log.i("servicetest","Secondary fragment - scheduled date $scheduledDate")
                    model.insertWateringSchedulerDays(wateringSchedulerId, checkedChipsIds)

                    alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmIntent = Intent(context, WateringReceiver::class.java).let {
                        it.putExtra("CHIPS",chipsIntArray)
                        it.putExtra("TIMESTRING",timeString)
                        it.action = scheduledDate.toString()
                        PendingIntent.getBroadcast(context,1,it,0)
                    }

                    setAlarmManager(alarmMgr,scheduledDate,alarmIntent)
                }
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setAlarmManager(alarmManager : AlarmManager?, dateTime : Long, intent: PendingIntent) {
        alarmManager?.set(
            AlarmManager.RTC_WAKEUP,
            dateTime,
            intent
        )
    }
}