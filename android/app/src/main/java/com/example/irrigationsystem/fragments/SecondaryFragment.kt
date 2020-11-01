package com.example.irrigationsystem.fragments

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.irrigationsystem.R
import com.example.irrigationsystem.databinding.FragmentSecondaryBinding
import com.example.irrigationsystem.helpers.DateHelper
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.receivers.WateringReceiver
import com.example.irrigationsystem.viewmodels.SecondaryViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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

            val chips = binding.chipGroup.checkedChipIds

            val checkedChipsIds = DateHelper.transformListIds(chips)

            val planName : String = binding.textFieldText.text.toString()
            val timeString = binding.editTextTime.text.toString()

            if(checkedChipsIds.count() > 0) {

                //Conversion to IntArray because we want to putExtra chip ids in Intent and send it to Receiver
                val chipsIntArray = checkedChipsIds.toIntArray()
                //Every plan created will be set as active
                val plan = Plan(
                    Name = planName,
                    IsActive = true
                )
                Log.i("servicetest","Secondary fragment, chipsIntArray $chipsIntArray")
                lifecycleScope.launchWhenStarted {

                    model.insertNote(plan)

                    //Part where we create schedule and pair schedule with days that we chose
                    val planId = model.getLatestPlanId().toInt()
                    model.changePlanActiveStatusExceptOne(planId)
                    val scheduledDate = model.insertWateringScheduler(checkedChipsIds, timeString, planId)
                    Log.i("testtest","Scheduled date - $scheduledDate")
                    val wateringSchedulerId = model.getLatestWateringSchedulerId().toInt()
                    model.insertWateringSchedulerDays(wateringSchedulerId, checkedChipsIds)


                    //Set alarm and when the time comes, "wake up" receiver
                    alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmIntent = Intent(context, WateringReceiver::class.java).let {
                        it.putExtra("CHIPS",chipsIntArray)
                        it.putExtra("TIMESTRING",timeString)
                        PendingIntent.getBroadcast(context,1,it,FLAG_UPDATE_CURRENT)
                    }

                    setAlarmManager(alarmMgr,scheduledDate,alarmIntent)
                }

                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(resources.getString(R.string.dialog_title))
                    .setMessage(resources.getString(R.string.dialog_text))
                    .setNeutralButton(resources.getString(R.string.dialog_button_neutral_text)){ _, _ ->
                        this.findNavController().popBackStack()
                    }
                    .show()
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createChannel(
            getString(R.string.is_notification_channel_id),
            getString(R.string.is_notification_channel_name))

    }

    private fun setAlarmManager(alarmManager : AlarmManager?, dateTime : Long, intent: PendingIntent) {
        alarmManager?.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            dateTime,
            intent
        )
    }

    private fun createChannel(channelId : String, channelName : String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "You got things to do"

            val notificationManager = requireActivity().getSystemService(
                NotificationManager::class.java
            )

            notificationManager?.createNotificationChannel(notificationChannel)
        }
    }
}