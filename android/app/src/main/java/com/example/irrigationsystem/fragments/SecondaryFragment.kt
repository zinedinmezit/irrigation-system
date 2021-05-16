package com.example.irrigationsystem.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.irrigationsystem.R
import com.example.irrigationsystem.databinding.FragmentSecondaryBinding
import com.example.irrigationsystem.helpers.DateDaysHelper
import com.example.irrigationsystem.helpers.FormValidation
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.receivers.WateringReceiver
import com.example.irrigationsystem.viewmodels.SecondaryViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SecondaryFragment : Fragment() {

    lateinit var binding : FragmentSecondaryBinding
    private val model : SecondaryViewModel by activityViewModels()

    val args : SecondaryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val address = args.ipAddress

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_secondary, container, false)

        binding.secondaryEditTextTime.setOnClickListener {
            TimePickerFragment(1).show(parentFragmentManager,"timePicker")
        }

        binding.secondaryButtonCreate.setOnClickListener{

            val chips = binding.chipGroup.checkedChipIds
            val checkedChipsIds = DateDaysHelper.transformListIds(chips)
            val planName : String = binding.secondaryTextFieldText.text.toString()
            val timeString = binding.secondaryEditTextTime.text.toString()

            val wateringDurationValue = when(binding.secondaryWateringDurationGroup.checkedRadioButtonId){
                R.id.secondary_wateringDuration1 -> 2000L
                R.id.secondary_wateringDuration2 -> 5000L
                R.id.secondary_wateringDuration3 -> 10000L
                else -> 2000L
            }

            if(checkedChipsIds.count() > 0) {

                if (FormValidation.secondaryFormValidation(planName, timeString,binding)) {
                    val chipsIntArray = checkedChipsIds.toIntArray()

                    val plan = Plan(
                        Name = planName,
                        IsActive = true
                    )

                    lifecycleScope.launchWhenStarted {

                        model.insertNote(plan)
                        val planId = model.getLatestPlanId().toInt()
                        model.changePlanActiveStatusExceptOne(planId)

                        val scheduledDate = model.insertWateringScheduler(checkedChipsIds, timeString, wateringDurationValue, planId)
                        val wateringSchedulerId = model.getLatestWateringSchedulerId().toInt()
                        model.insertWateringSchedulerDays(wateringSchedulerId, checkedChipsIds)

                        val alarmMgr =
                            context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        val alarmIntent =
                            Intent(context, WateringReceiver::class.java).let { intent ->
                                intent.putExtra("CHIPS", chipsIntArray)
                                intent.putExtra("TIMESTRING", timeString)
                                intent.putExtra("IPADDRESS", address)
                                intent.putExtra("SCHEDULERID", wateringSchedulerId)
                                intent.putExtra("WATERINGDURATION", wateringDurationValue)
                                PendingIntent.getBroadcast(
                                    context,
                                    173839173,
                                    intent,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                                )
                            }
                        setAlarmManager(alarmMgr, scheduledDate, alarmIntent)
                    }
                    showSuccessDialog()
                }
            }
        }
        return binding.root
    }

    private fun showSuccessDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.dialog_title))
            .setMessage(resources.getString(R.string.dialog_text))
            .setCancelable(false)
            .setNeutralButton(resources.getString(R.string.dialog_button_neutral_text)){ _, _ ->
                this.findNavController().popBackStack()
            }
            .show()
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