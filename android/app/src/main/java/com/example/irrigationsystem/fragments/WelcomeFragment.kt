package com.example.irrigationsystem.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.irrigationsystem.R
import com.example.irrigationsystem.databinding.FragmentWelcomeBinding
import com.example.irrigationsystem.viewmodels.WelcomeViewModel

class WelcomeFragment : Fragment() {

    private val model : WelcomeViewModel by activityViewModels()

    private var flag : Boolean = false

    private var address : String? = null

    private var _binding : FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)

        binding.button3.setOnClickListener {
            Log.i("testtest1","$address")
            if(!flag)
                this.findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToMainFragment(address!!))
            else this.findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToSetupFragment())
        }

        model.toSetup.observe(viewLifecycleOwner,{
            value -> flag = value
        })

        model.address.observe(viewLifecycleOwner, {
            address = it
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        model.checkToSetup()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}