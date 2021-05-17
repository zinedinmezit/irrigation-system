package com.example.irrigationsystem.fragments

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.irrigationsystem.databinding.BottomsheetPlansBinding
import com.example.irrigationsystem.helpers.*
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.models.PlanWateringSchedulerView
import com.example.irrigationsystem.receivers.WateringReceiver
import com.example.irrigationsystem.viewmodels.BottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {

    var listOfPlans: List<Plan>? = null
    var webSocketIpAddress: String? = null

    private var planSchedulerView: PlanWateringSchedulerView? = null
    private var days: List<Int>? = null

    private lateinit var binding: BottomsheetPlansBinding
    private lateinit var alarmManager: AlarmUtil
    private val model: BottomSheetViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = BottomsheetPlansBinding.inflate(inflater, container, false)
        alarmManager = AlarmUtil(requireContext())

        val adapter = PlanAdapter(PlanListener {
            if (!it.IsActive) {
                model.changePlanActiveStatusExceptOne(it.PlanId)
                model.setPlanAsActive(it.PlanId)
            }

        }, DeleteListener {
            model.deletePlan(it)
            this.dismiss()
        })
        binding.planRecyclerview.adapter = adapter

        adapter.submitList(listOfPlans)

        model.planSchedulerView.observe(viewLifecycleOwner, {
            planSchedulerView = it
        })

        model.scheduledDays.observe(viewLifecycleOwner, {
            days = it
        })

        model.isFetched.observe(viewLifecycleOwner, {
            if (it) {
                if (planSchedulerView != null && days != null) {

                    val pair = DateDaysHelper.getDateForCurrentSchedule(
                        days?.toMutableList()!!,
                        planSchedulerView?.TimeString!!
                    )

                    val alarmIntent = Intent(context, WateringReceiver::class.java).let { intent ->
                        intent.putExtra("CHIPS", days?.toIntArray())
                        intent.putExtra("TIMESTRING", planSchedulerView?.TimeString!!)
                        intent.putExtra("IPADDRESS", webSocketIpAddress!!)
                        intent.putExtra("SCHEDULERID", planSchedulerView?.WateringSchedulerId)
                        intent.putExtra("WATERINGDURATION", planSchedulerView?.WateringDuration)
                        PendingIntent.getBroadcast(
                            context,
                            173839173,
                            intent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                        )
                    }

                    model.setWateringTImeNow(
                        pair.first.time,
                        planSchedulerView?.WateringSchedulerId!!
                    )

                    alarmManager.scheduleAlarmManager(pair.first.time, alarmIntent)

                    model.fetchedToFalse()
                    this.dismiss()
                }
            }
        })

        return binding.root
    }
}