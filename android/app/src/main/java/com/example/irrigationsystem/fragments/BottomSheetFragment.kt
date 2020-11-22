package com.example.irrigationsystem.fragments

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.irrigationsystem.databinding.BottomsheetPlansBinding
import com.example.irrigationsystem.helpers.PlanAdapter
import com.example.irrigationsystem.helpers.PlanListener
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.receivers.WateringReceiver
import com.example.irrigationsystem.viewmodels.BottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment() : BottomSheetDialogFragment() {

    var  plans : List<Plan>? = null
    lateinit var scheduledDays : IntArray


    private lateinit var binding : BottomsheetPlansBinding

    private val model: BottomSheetViewModel by activityViewModels()

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = BottomsheetPlansBinding.inflate(inflater, container, false)

        val adapter = PlanAdapter(PlanListener {
             model.changePlanActiveStatusExceptOne(it)
             model.setPlanAsActive(it)

            alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            model.scheduledDays.observe(viewLifecycleOwner, Observer {
                ld -> scheduledDays = ld.toIntArray()
            })

           /* alarmIntent = Intent(context, WateringReceiver::class.java).let { int ->
                int.putExtra("CHIPS",scheduledDays)
                int.putExtra("TIMESTRING",timeString)
                PendingIntent.getBroadcast(context,1,int, PendingIntent.FLAG_UPDATE_CURRENT)
            }*/

            this.dismiss()
        })
        binding.planRecyclerview.adapter = adapter

        adapter.submitList(plans)

        return binding.root
    }
}