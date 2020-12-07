package com.example.irrigationsystem.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.example.irrigationsystem.helpers.DateHelper
import com.example.irrigationsystem.helpers.DaysAdapter
import com.example.irrigationsystem.receivers.WateringReceiver
import com.example.irrigationsystem.viewmodels.EditViewModel


class EditFragment : Fragment() {

    private lateinit var binding : FragmentEditBinding

    private val model: EditViewModel by activityViewModels()

    val args : EditFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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
            val transformedChipIds = DateHelper.transformListIds(checkedChipIds,2)

            val planName = binding.editInputText.text.toString()
            val timeString = binding.editTimeText.text.toString()

            if(transformedChipIds.count() > 0) {

                if (validateForm(planName, timeString)) {

                    val chipsIntArray = transformedChipIds.toIntArray()

                    model.updatePlan(planName)
                    model.deleteDaysFromScheduler()
                    model.insertWateringSchedulerDays(transformedChipIds)

                    val scheduledDate =
                        model.updateWateringScheduler(transformedChipIds, timeString)
                    //alarmMgr?.scheduleWatering(requireContext(), chipsIntArray, timeString, scheduledDate,address)

                    val alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    Log.i("testtest1", "${model.activePlan.value?.WateringSchedulerId}")
                    Log.i("Checkup", "EditFragment(activePlan) : \n${model.activePlan.value}")
                    val alarmIntent = Intent(context, WateringReceiver::class.java).let { intent ->
                        intent.putExtra("CHIPS", chipsIntArray)
                        intent.putExtra("TIMESTRING", timeString)
                        intent.putExtra("IPADDRESS", address)
                        intent.putExtra("SCHEDULERID", model.activePlan.value?.WateringSchedulerId)
                        PendingIntent.getBroadcast(
                            context,
                            1,
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

    private fun validateForm(planName : String?, timeString : String?) : Boolean{
        var flag = true


        if(planName.isNullOrBlank()){
            binding.editInputLayout.error = "Plan name can't be empty"
            flag = false
        }
        else{
            binding.editInputLayout.error = null
        }

        if(timeString.isNullOrBlank()){
            binding.editTimeText.error = "Time can't be empty"
            flag = false
        }
        else{
            binding.editTimeText.error = null
        }

        return flag
    }
}