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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_secondary, container, false)

        binding.editTextTime.setOnClickListener {
            TimePickerFragment(1).show(parentFragmentManager,"timePicker")
        }

        binding.button.setOnClickListener{

            val chips = binding.chipGroup.checkedChipIds
            val checkedChipsIds = DateHelper.transformListIds(chips)

            val planName : String = binding.textFieldText.text.toString()
            val timeString = binding.editTextTime.text.toString()

            if(checkedChipsIds.count() > 0) {

                val chipsIntArray = checkedChipsIds.toIntArray()

                val plan = Plan(
                    Name = planName,
                    IsActive = true
                )

                lifecycleScope.launchWhenStarted {

                    model.insertNote(plan)

                    val planId = model.getLatestPlanId().toInt()
                    model.changePlanActiveStatusExceptOne(planId)
                    val scheduledDate = model.insertWateringScheduler(checkedChipsIds, timeString, planId)
                    Log.i("testtest","Scheduled date - $scheduledDate")
                    val wateringSchedulerId = model.getLatestWateringSchedulerId().toInt()
                    model.insertWateringSchedulerDays(wateringSchedulerId, checkedChipsIds)

                    alarmMgr = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmMgr?.scheduleWatering(requireContext(), chipsIntArray,timeString, scheduledDate)
                }
                showSuccessDialog()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createChannel(
            getString(R.string.is_notification_channel_id),
            getString(R.string.is_notification_channel_name))
    }

    private fun createChannel(channelId : String, channelName : String) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
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

    private fun showSuccessDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.dialog_title))
            .setMessage(resources.getString(R.string.dialog_text))
            .setNeutralButton(resources.getString(R.string.dialog_button_neutral_text)){ _, _ ->
                this.findNavController().popBackStack()
            }
            .show()
    }
}