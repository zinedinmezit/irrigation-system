package com.example.irrigationsystem.fragments

import android.app.AlarmManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.irrigationsystem.databinding.BottomsheetPlansBinding
import com.example.irrigationsystem.helpers.DateHelper
import com.example.irrigationsystem.helpers.DeleteListener
import com.example.irrigationsystem.helpers.PlanAdapter
import com.example.irrigationsystem.helpers.PlanListener
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.models.PlanWateringSchedulerView
import com.example.irrigationsystem.models.WateringScheduler
import com.example.irrigationsystem.viewmodels.BottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch

class BottomSheetFragment() : BottomSheetDialogFragment() {

    var  plans : List<Plan>? = null
     var ipAddress : String? = null

    var scheduler : PlanWateringSchedulerView? = null
    var days : List<Int>? = null

    private lateinit var binding : BottomsheetPlansBinding

    private val model: BottomSheetViewModel by activityViewModels()

    private var alarmMgr: AlarmManager? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = BottomsheetPlansBinding.inflate(inflater, container, false)

        val adapter = PlanAdapter(PlanListener {
            if(!it.IsActive) {
                model.changePlanActiveStatusExceptOne(it.PlanId)
                model.setPlanAsActive(it.PlanId)
            }

        }, DeleteListener {
            model.deletePlan(it)
            this.dismiss()
        })
        binding.planRecyclerview.adapter = adapter

        adapter.submitList(plans)

        model.scheduler.observe(viewLifecycleOwner, Observer {
           scheduler = it
        })

        model.days.observe(viewLifecycleOwner, Observer {
            days = it
        })

        model.fetched.observe(viewLifecycleOwner, Observer {
            if(it){
                if(scheduler != null && days != null){

                    val pair = DateHelper.getDateForCurrentSchedule(days?.toMutableList()!!,scheduler?.TimeString!!)
                    alarmMgr = this.requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmMgr?.scheduleWatering(requireContext(), days?.toIntArray()!!,scheduler?.TimeString!!, pair.first.time, ipAddress!!)
                    model.fetchedToFalse()
                    this.dismiss()
                }
            }
        })

        return binding.root
    }
}