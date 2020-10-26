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

        //Display time picker when edit field is pressed
        binding.editTextTime.setOnClickListener {
            TimePickerFragment().show(parentFragmentManager,"timePicker")
        }

        //When create plan button is pressed
        binding.button.setOnClickListener{

            //Since chip ids have strange unique id values and each chip represents day where days, based on Calendar class, have their
            //own ids from 1 to 7 starting with Sunday as 1, this method just transforms these ids to be meaningful with Calendar class
            val checkedChipsIds = DateHelper.transformListIds(binding.chipGroup.checkedChipIds)

            val planName : String = binding.textFieldText.text.toString()
            val timeString = binding.editTextTime.text.toString()

            if(checkedChipsIds.count() > 0) {

                //Conversion to IntArray because we want to putExtra chip ids in Intent and send it to Receiver
                val chipsIntArray = checkedChipsIds.toIntArray()
                Log.i("servicetest","Secondary fragment, chipsIntArray $chipsIntArray")
                lifecycleScope.launchWhenStarted {

                    //Every plan created will be set as active
                    val plan = Plan(
                        Name = planName,
                        IsActive = true
                    )
                    model.insertNote(plan)

                    //Part where we create schedule and pair schedule with days that we chose
                    val planId = model.getLatestPlanId().toInt()
                    val wateringSchedulerId = model.getLatestWateringSchedulerId().toInt()
                    model.insertWateringSchedulerDays(wateringSchedulerId, checkedChipsIds)

                    //Intent needs to have some kind of id so the best way to have unique id without creating anything else
                    //is to set id as datetime (might remove this)
                    val scheduledDate = model.insertWateringScheduler(checkedChipsIds, timeString, planId)

                    //Set alarm and when the time comes, "wake up" receiver
                    alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmIntent = Intent(context, WateringReceiver::class.java).let {
                        it.putExtra("CHIPS",chipsIntArray)
                        it.putExtra("TIMESTRING",timeString)
                        it.action = scheduledDate.toString()
                        PendingIntent.getBroadcast(context,1,it,0) //Consider replacing flag with FLAG_UPDATE_CURRENT
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