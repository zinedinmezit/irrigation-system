package com.example.irrigationsystem.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.irrigationsystem.R
import com.example.irrigationsystem.databinding.FragmentEditBinding


class EditFragment : Fragment() {

    private lateinit var binding : FragmentEditBinding
    val args : EditFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_edit,container,false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.textView.text = args.planId.toString()
        // Inflate the layout for this fragment
        return binding.root
    }

}