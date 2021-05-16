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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.irrigationsystem.R
import com.example.irrigationsystem.databinding.FragmentEditBinding
import com.example.irrigationsystem.helpers.DateDaysHelper
import com.example.irrigationsystem.helpers.DaysAdapter
import com.example.irrigationsystem.helpers.FormValidation
import com.example.irrigationsystem.receivers.WateringReceiver
import com.example.irrigationsystem.viewmodels.EditViewModel


class EditFragment : Fragment() {

    private lateinit var binding : FragmentEditBinding

    private val model: EditViewModel by activityViewModels()

    private val args : EditFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val address = args.ipAddress

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.editVM = model
        binding.executePendingBindings()

        val adapter = DaysAdapter()
        binding.recyclerView.adapter = adapter

        model.scheduledDays.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.editTimeText.setOnClickListener {
            TimePickerFragment(2).show(parentFragmentManager,"timePicker")
        }

        binding.editButtonUpdate.setOnClickListener{

            val checkedChipIds = binding.editChipGroup.checkedChipIds
            val transformedChipIds = DateDaysHelper.transformListIds(checkedChipIds,2)
            val planName = binding.editInputText.text.toString()
            val timeString = binding.editTimeText.text.toString()

            val wateringDurationValue = when(binding.editWateringDurationGroup.checkedRadioButtonId){
                R.id.edit_wateringDuration1 -> 2000L
                R.id.edit_wateringDuration2 -> 5000L
                R.id.edit_wateringDuration3 -> 10000L
                else -> 2000L
            }

            if(transformedChipIds.count() > 0) {

                if (FormValidation.editFormValidation(planName, timeString,binding)) {

                    val chipsIntArray = transformedChipIds.toIntArray()

                    model.updatePlan(planName)
                    model.deleteDaysFromScheduler()
                    model.insertWateringSchedulerDays(transformedChipIds)

                    val scheduledDate =
                        model.updateWateringScheduler(transformedChipIds, timeString, wateringDurationValue)
                    //alarmMgr?.scheduleWatering(requireContext(), chipsIntArray, timeString, scheduledDate,address)

                    val alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val alarmIntent = Intent(context, WateringReceiver::class.java).let { intent ->
                        intent.putExtra("CHIPS", chipsIntArray)
                        intent.putExtra("TIMESTRING", timeString)
                        intent.putExtra("IPADDRESS", address)
                        intent.putExtra("SCHEDULERID", model.activePlan.value?.WateringSchedulerId)
                        intent.putExtra("WATERINGDURATION", wateringDurationValue)
                        PendingIntent.getBroadcast(
                            context,
                            173839173,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )
                    }

                    setAlarmManager(alarmMgr, scheduledDate, alarmIntent)

                    this.findNavController().popBackStack()
                }
            }
        }

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