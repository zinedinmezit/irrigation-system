package com.example.irrigationsystem.fragments

import android.app.AlarmManager
import android.content.Context
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
import com.example.irrigationsystem.databinding.FragmentSetupBinding
import com.example.irrigationsystem.helpers.DateHelper
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.models.SetupInfo
import com.example.irrigationsystem.viewmodels.SetupViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SetupFragment : Fragment() {

    lateinit var binding : FragmentSetupBinding

    private val model : SetupViewModel by activityViewModels()
    private var alarmMgr: AlarmManager? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_setup,container,false)
        binding.lifecycleOwner = this


        binding.setupTimeString.setOnClickListener {
            TimePickerFragment(3).show(parentFragmentManager,"timePicker")
        }

        binding.setupCreateButton.setOnClickListener{

            val chips = binding.setupChipGroup.checkedChipIds
            val checkedChipsIds = DateHelper.transformListIds(chips,3)

            val planName : String = binding.setupTextFieldText.text.toString()
            val timeString = binding.setupTimeString.text.toString()
            val wsIpAddress = binding.setupIpAddressText.text.toString()
            val city = binding.setupCityText.text.toString()

            if(checkedChipsIds.count() > 0) {

                val chipsIntArray = checkedChipsIds.toIntArray()

                val plan = Plan(
                    Name = planName,
                    IsActive = true
                )

                val server = SetupInfo(IpAddress = wsIpAddress, City = city)

                lifecycleScope.launchWhenStarted {
                    model.insertWeekDays()
                    model.insertNote(plan)
                    model.insertServer(server)

                    val planId = model.getLatestPlanId().toInt()
                    model.changePlanActiveStatusExceptOne(planId)
                    val scheduledDate = model.insertWateringScheduler(checkedChipsIds, timeString, planId)
                    Log.i("testtest","Scheduled date - $scheduledDate")
                    val wateringSchedulerId = model.getLatestWateringSchedulerId().toInt()
                    model.insertWateringSchedulerDays(wateringSchedulerId, checkedChipsIds)

                    alarmMgr = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    alarmMgr?.scheduleWatering(requireContext(), chipsIntArray,timeString, scheduledDate,wsIpAddress)
                }

                showSuccessDialog()
            }
        }

        return binding.root
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