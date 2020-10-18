package com.example.irrigationsystem.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.example.irrigationsystem.R
import com.example.irrigationsystem.databinding.FragmentMainBinding
import com.example.irrigationsystem.network.OkHttpProvider
import com.example.irrigationsystem.viewmodels.MainViewModel

class MainFragment : Fragment() {

    private val model: MainViewModel by activityViewModels()
    lateinit var binding : FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainVM = model

        OkHttpProvider.openWebSocketConnection(model.wsListener)

         val reconnectButton = binding.buttonReconnect
        reconnectButton.setOnClickListener {
            model.signalCode=0
            OkHttpProvider.openWebSocketConnection(model.wsListener)
        }

        val actionButton = binding.buttonAction
        actionButton.setOnClickListener {
            model.signalCode=1
            OkHttpProvider.openWebSocketConnection(model.wsListener)
        }

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        OkHttpProvider.closeConnections()
    }
}