package com.example.irrigationsystem.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.irrigationsystem.R
import com.example.irrigationsystem.databinding.BottomsheetPlansBinding
import com.example.irrigationsystem.helpers.PlanAdapter
import com.example.irrigationsystem.helpers.PlanListener
import com.example.irrigationsystem.models.Plan
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment() : BottomSheetDialogFragment() {

     var  plans : List<Plan>? = null

    private lateinit var binding : BottomsheetPlansBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = BottomsheetPlansBinding.inflate(inflater, container, false)

        val adapter = PlanAdapter(PlanListener {
            it -> Toast.makeText(context,"$it",Toast.LENGTH_SHORT).show()
        })
        binding.planRecyclerview.adapter = adapter

        adapter.submitList(plans)

        return binding.root
    }
}