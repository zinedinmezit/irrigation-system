package com.example.irrigationsystem.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.irrigationsystem.R
import com.example.irrigationsystem.databinding.FragmentSecondaryBinding
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.viewmodels.SecondaryViewModel
import kotlinx.coroutines.launch
import java.util.*


class SecondaryFragment : Fragment() {

    lateinit var binding : FragmentSecondaryBinding
    private val model : SecondaryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_secondary, container, false)

        binding.editTextTime.setOnClickListener {
            TimePickerFragment().show(parentFragmentManager,"timePicker")
        }

        binding.button.setOnClickListener{

            val checkedChipsIds = binding.chipGroup.checkedChipIds

            /*val checkedChipsIds = binding.chipGroup.checkedChipIds
            model.insertWateringScheduler(checkedChipsIds)*/
            if(checkedChipsIds.count() > 0) {
                lifecycleScope.launchWhenStarted {

                    val plan = Plan(
                        Name = "Test",
                        IsActive = true
                    )

                    model.insertNote(plan)
                    val planId = model.getLatestPlanId().toInt()

                    model.insertWateringScheduler(checkedChipsIds, planId)
                    val wateringSchedulerId = model.getLatestWateringSchedulerId().toInt()

                    model.insertWateringSchedulerDays(wateringSchedulerId, checkedChipsIds)
                }
            }
        }
        // Inflate the layout for this fragment
        return binding.root
    }

}