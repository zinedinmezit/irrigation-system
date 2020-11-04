package com.example.irrigationsystem.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import com.example.irrigationsystem.helpers.DateHelper
import com.example.irrigationsystem.helpers.DaysAdapter
import com.example.irrigationsystem.receivers.WateringReceiver
import com.example.irrigationsystem.viewmodels.EditViewModel


class EditFragment : Fragment() {

    private lateinit var binding : FragmentEditBinding

    private val model: EditViewModel by activityViewModels()

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.editVM = model

        val adapter = DaysAdapter()
        binding.recyclerView.adapter = adapter

        model.scheduledDays.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
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

            if(transformedChipIds.count() > 0){

                val chipsIntArray = transformedChipIds.toIntArray()

                alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmIntent = Intent(context, WateringReceiver::class.java).let {
                    it.putExtra("CHIPS",chipsIntArray)
                    it.putExtra("TIMESTRING",timeString)
                    PendingIntent.getBroadcast(context,1,it, PendingIntent.FLAG_UPDATE_CURRENT)
                }

                model.updatePlan(planName)
                model.deleteDaysFromScheduler()
                model.insertWateringSchedulerDays(transformedChipIds)
                val scheduledDate = model.updateWateringScheduler(transformedChipIds, timeString)

                setAlarmManager(alarmMgr, scheduledDate, alarmIntent)

                this.findNavController().popBackStack()
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setAlarmManager(alarmManager : AlarmManager?, dateTime : Long, intent: PendingIntent) {
        alarmManager?.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            dateTime,
            intent
        )
    }

}