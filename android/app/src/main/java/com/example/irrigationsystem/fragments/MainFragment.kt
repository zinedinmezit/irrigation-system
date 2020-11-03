package com.example.irrigationsystem.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.irrigationsystem.R
import com.example.irrigationsystem.databinding.FragmentMainBinding
import com.example.irrigationsystem.helpers.DaysAdapter
import com.example.irrigationsystem.network.OkHttpProvider
import com.example.irrigationsystem.viewmodels.MainViewModel
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private val model: MainViewModel by activityViewModels()
    private lateinit var binding : FragmentMainBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Binding setup
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainVM = model


        //Purpose - visible if there is need for reconnection because of failed connection with websocket
         val reconnectButton = binding.buttonReconnect
        reconnectButton.setOnClickListener {
            model.signalCode=0
            lifecycleScope.launch {
                OkHttpProvider.openWebSocketConnection(model.wsListener)
            }
        }

        //Purpose - water plant on button pressed
        val actionButton = binding.buttonAction
        actionButton.setOnClickListener {
            model.signalCode=1
            lifecycleScope.launchWhenStarted {
                OkHttpProvider.openWebSocketConnection(model.wsListener)
            }
        }

        binding.editImageButton.setOnClickListener {
            val planId = model.getPlanId()!!
            val action = MainFragmentDirections.actionMainFragmentToEditFragment(planId)
            this.findNavController().navigate(action)
        }

        //Purpose - switch to selected fragment
        val floatingButton = binding.floatingActionButton
        floatingButton.setOnClickListener{
            val action = MainFragmentDirections.actionMainFragmentToSecondaryFragment()
            this.findNavController().navigate(action)
        }

        val adapter = DaysAdapter()
        binding.recyclerView.adapter = adapter

        model.scheduledDays.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //On fragment apperance, connect with websocket
        lifecycleScope.launch {
            OkHttpProvider.openWebSocketConnection(model.wsListener)
        }
    }

    override fun onStop() {
        super.onStop()
        lifecycleScope.launch {
            OkHttpProvider.closeConnections()
        }
    }
}