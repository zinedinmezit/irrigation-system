package com.example.irrigationsystem.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.irrigationsystem.R
import com.example.irrigationsystem.databinding.FragmentMainBinding
import com.example.irrigationsystem.helpers.DaysAdapter
import com.example.irrigationsystem.network.OkHttpProvider
import com.example.irrigationsystem.viewmodels.MainViewModel
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private val model: MainViewModel by activityViewModels()
    private lateinit var binding : FragmentMainBinding

    val args : MainFragmentArgs by navArgs()
    lateinit var IpAddress : String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        IpAddress = args.ipAddress

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainVM = model
        binding.executePendingBindings()


        val bottomSheetFragment = BottomSheetFragment()
        model.allPlans.observe(viewLifecycleOwner, Observer {
            bottomSheetFragment.plans = it
            bottomSheetFragment.ipAddress = IpAddress
        })

        binding.button2.setOnClickListener {
            bottomSheetFragment.show(requireActivity().supportFragmentManager, "test")
        }

         val reconnectButton = binding.buttonReconnect
        reconnectButton.setOnClickListener {
            model.signalCode=0
            lifecycleScope.launch {
                OkHttpProvider.openWebSocketConnection(model.wsListener,IpAddress!!)
            }
        }

        val actionButton = binding.buttonAction
        actionButton.setOnClickListener {
            model.signalCode=1
            lifecycleScope.launchWhenStarted {
                OkHttpProvider.openWebSocketConnection(model.wsListener,IpAddress!!)
            }
        }

        binding.editImageButton.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToEditFragment(IpAddress!!)
            this.findNavController().navigate(action)
        }

        val floatingButton = binding.floatingActionButton
        floatingButton.setOnClickListener{
            val action = MainFragmentDirections.actionMainFragmentToSecondaryFragment(IpAddress!!)
            this.findNavController().navigate(action)
        }

        val adapter = DaysAdapter()
        binding.recyclerView.adapter = adapter

        model.scheduledDays.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lifecycleScope.launch {
            OkHttpProvider.openWebSocketConnection(model.wsListener,IpAddress!!)
        }
    }

    override fun onStop() {
        super.onStop()
        lifecycleScope.launch {
            OkHttpProvider.closeConnections()
        }
    }
}