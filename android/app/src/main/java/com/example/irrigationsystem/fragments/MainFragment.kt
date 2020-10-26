package com.example.irrigationsystem.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.irrigationsystem.R
import com.example.irrigationsystem.databinding.FragmentMainBinding
import com.example.irrigationsystem.network.OkHttpProvider
import com.example.irrigationsystem.viewmodels.MainViewModel
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private val model: MainViewModel by activityViewModels()
    lateinit var binding : FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Binding setup
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainVM = model

        //On fragment apperance, connect with websocket
        lifecycleScope.launchWhenStarted {
            OkHttpProvider.openWebSocketConnection(model.wsListener)
        }

        //Purpose - visible if there is need for reconnection because of failed connection with websocket
         val reconnectButton = binding.buttonReconnect
        reconnectButton.setOnClickListener {
            model.signalCode=0
            lifecycleScope.launchWhenStarted {
                OkHttpProvider.openWebSocketConnection(model.wsListener)
            }        }

        //Purpose - water plant on button pressed
        val actionButton = binding.buttonAction
        actionButton.setOnClickListener {
            model.signalCode=1
            lifecycleScope.launchWhenStarted {
                OkHttpProvider.openWebSocketConnection(model.wsListener)
            }
        }

        //Purpose - switch to selected fragment
        val floatingButton = binding.floatingActionButton
        floatingButton.setOnClickListener{
            val action = MainFragmentDirections.actionMainFragmentToSecondaryFragment()
            this.findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onStop() {
        super.onStop()
        lifecycleScope.launch {
            OkHttpProvider.closeConnections()
        }
    }
}