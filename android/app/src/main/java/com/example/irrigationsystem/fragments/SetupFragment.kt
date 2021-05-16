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
import com.example.irrigationsystem.R
import com.example.irrigationsystem.databinding.FragmentSetupBinding
import com.example.irrigationsystem.helpers.DateDaysHelper
import com.example.irrigationsystem.helpers.FormValidation
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.models.SetupInfo
import com.example.irrigationsystem.receivers.WateringReceiver
import com.example.irrigationsystem.viewmodels.SetupViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SetupFragment : Fragment() {

    lateinit var binding : FragmentSetupBinding

    private val model : SetupViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_setup,container,false)
        binding.lifecycleOwner = this


        binding.setupTimeString.setOnClickListener {
            TimePickerFragment(3).show(parentFragmentManager,"timePicker")
        }

        binding.setupCreateButton.setOnClickListener{

            val chips = binding.setupChipGroup.checkedChipIds
            val checkedChipsIds = DateDaysHelper.transformListIds(chips,3)
            val planName : String = binding.setupTextFieldText.text.toString()
            val timeString = binding.setupTimeString.text.toString()
            val wsIpAddress = binding.setupIpAddressText.text.toString()
            val city = binding.setupCityText.text.toString()

            val tempMin  = binding.setupTempMinEditText.text.toString()
            val tempMax  = binding.setupTempMaxEditText.text.toString()
            val hummMin  = binding.setupHummMinEditText.text.toString()
            val hummMax  = binding.setupHummMaxEditText.text.toString()

            val wateringDurationValue = when(binding.setupWateringDurationGroup.checkedRadioButtonId){
                R.id.setup_wateringDuration1 -> 2000L
                R.id.setup_wateringDuration2 -> 5000L
                R.id.setup_wateringDuration3 -> 10000L
                else -> 2000L
            }

            if(checkedChipsIds.count() > 0) {

                if (FormValidation.setupFormValidator(planName, timeString, wsIpAddress, city,tempMin,tempMax,hummMin,hummMax,binding)) {
                    val chipsIntArray = checkedChipsIds.toIntArray()

                    val plan = Plan(
                        Name = planName,
                        IsActive = true
                    )

                    val server = SetupInfo(
                        IpAddress = wsIpAddress,
                        City = city,
                        TemperatureMinLimit = tempMin.toDouble(),
                        TemperatureMaxLimit = tempMax.toDouble(),
                        HummidityMinLimit = hummMin.toDouble(),
                        HummidityMaxLimit = hummMax.toDouble())

                    lifecycleScope.launchWhenStarted {
                        model.insertWeekDays()
                        model.insertNote(plan)
                        model.insertServer(server)

                        val planId = model.getLatestPlanId().toInt()
                        model.changePlanActiveStatusExceptOne(planId)
                        val scheduledDate =
                            model.insertWateringScheduler(checkedChipsIds, timeString, wateringDurationValue, planId)
                        val wateringSchedulerId = model.getLatestWateringSchedulerId().toInt()
                        model.insertWateringSchedulerDays(wateringSchedulerId, checkedChipsIds)

                        val alarmMgr =
                            context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        val alarmIntent =
                            Intent(context, WateringReceiver::class.java).let { intent ->
                                intent.putExtra("CHIPS", chipsIntArray)
                                intent.putExtra("TIMESTRING", timeString)
                                intent.putExtra("IPADDRESS", wsIpAddress)
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