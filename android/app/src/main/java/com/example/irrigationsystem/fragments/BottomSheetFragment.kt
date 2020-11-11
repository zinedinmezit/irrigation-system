package com.example.irrigationsystem.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.irrigationsystem.databinding.BottomsheetPlansBinding
import com.example.irrigationsystem.helpers.PlanAdapter
import com.example.irrigationsystem.helpers.PlanListener
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.viewmodels.BottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment() : BottomSheetDialogFragment() {

     var  plans : List<Plan>? = null

    private lateinit var binding : BottomsheetPlansBinding

    private val model: BottomSheetViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = BottomsheetPlansBinding.inflate(inflater, container, false)

        val adapter = PlanAdapter(PlanListener {
             model.changePlanActiveStatusExceptOne(it)
             model.setPlanAsActive(it)
            this.dismiss()
        })
        binding.planRecyclerview.adapter = adapter

        adapter.submitList(plans)

        return binding.root
    }
}