package com.example.irrigationsystem.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.irrigationsystem.R
import com.example.irrigationsystem.databinding.FragmentMainBinding
import com.example.irrigationsystem.helpers.DaysAdapter
import com.example.irrigationsystem.helpers.ForecastAdapter
import com.example.irrigationsystem.viewmodels.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainFragment : Fragment(){

    private val model: MainViewModel by activityViewModels()
    private lateinit var binding : FragmentMainBinding

    val args : MainFragmentArgs by navArgs()
    lateinit var webSocketIpAddress : String
     var city : String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Assigned from arguments (Navigation : WelcomeFragment -> MainFragment(IpAddress:String, City:String))
        webSocketIpAddress = args.ipAddress
        if(city == null)
        city = args.cIty

        /*   ***BINDING SETUP***   */
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainVM = model
        binding.executePendingBindings()



        //Open websocket connection with signal code 0
        model.openWebSocketConnection(webSocketIpAddress)



        //Forecast is presented using recyclerview
        val weatherAdapter = ForecastAdapter()
        binding.weatherRecycler.adapter = weatherAdapter
        model.apiResponse.observe(viewLifecycleOwner,{
            it?.forecast.let { forecast ->
                weatherAdapter.submitList(forecast)
            }
        })

        //Register livedata to track if models.SetupInfo is updated
        //If true, update variables and send network request to refresh forecast
        model.setupInfo.observe(viewLifecycleOwner,{
            if(city != it.City) {
                model.responseFlag = true
                city = it.City
                webSocketIpAddress = it.IpAddress
                model.getApiResponse(city!!)
            }
        })



        //Active's plan scheduled days are displayed using recyclerview
        val adapter = DaysAdapter()
        binding.recyclerView.adapter = adapter
        model.scheduledDays.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })



        //Initialize BottomSheetFragment and as soon is required info for BottomSheetFragment available, ship it
        val bottomSheetFragment = BottomSheetFragment()
        model.allPlans.observe(viewLifecycleOwner, {
            bottomSheetFragment.listOfPlans = it
            bottomSheetFragment.webSocketIpAddress = webSocketIpAddress
        })



        /*   ***CLICK LISTENERS***   */

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->

            when (menuItem.itemId) {
                R.id.parameter_item -> {
                    val dialogFragment = ParamsSetupDialogFragment()
                    dialogFragment.show(childFragmentManager, "ParamsSetupDialogFragment")
                    true
                }
                R.id.city_item -> {
                    val dialogFragment = BasicSetupDialogFragment()
                    dialogFragment.show(childFragmentManager, "BasicSetupDialogFragment")
                    true
                }
                R.id.ip_item -> {
                    val dialogFragment = IpAddressDialogFragment()
                    dialogFragment.show(childFragmentManager, "IpAddressDialogFragment")
                    true
                }
                else -> false
            }
        }


        binding.mainButtonSelectPlan.setOnClickListener {
            bottomSheetFragment.show(requireActivity().supportFragmentManager, "BottomSheetFragment")
        }

        binding.buttonReconnect.setOnClickListener {
            model.signalCode=0
            model.openWebSocketConnection(webSocketIpAddress)
        }

        binding.buttonWater.setOnClickListener {
            showSuccessDialog()
        }

        binding.planName.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToEditFragment(webSocketIpAddress)
            this.findNavController().navigate(action)
        }

        binding.extendedFab.setOnClickListener{
            val action = MainFragmentDirections.actionMainFragmentToSecondaryFragment(webSocketIpAddress)
            this.findNavController().navigate(action)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        model.getApiResponse(city!!)
    }

    override fun onStop() {
        super.onStop()
        model.closeWebSocketConnection()
    }

    private fun showSuccessDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.watering_dialog_title))
            .setMessage(resources.getString(R.string.watering_dialog_text))
            .setPositiveButton(resources.getString(R.string.watering_dialog_positive_button_label)){ _, _ ->
                model.signalCode=1
                model.openWebSocketConnection(webSocketIpAddress)
            }
            .setNegativeButton(resources.getString(R.string.watering_dialog_negative_button_label)){ dialog, par ->
                dialog.dismiss()
            }
            .show()
    }

}