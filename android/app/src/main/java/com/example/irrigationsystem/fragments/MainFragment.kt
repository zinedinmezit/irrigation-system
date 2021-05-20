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

    private val bottomSheetFragment = BottomSheetFragment()


     private val args : MainFragmentArgs by navArgs()
     private var webSocketIpAddress : String? = null
     var city : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Assigned from arguments (Navigation : WelcomeFragment -> MainFragment(IpAddress:String, City:String))
        setWebSocketIpAddress(args.ipAddress)
        setForecastCity(args.cIty)

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main,container,false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainVM = model
        binding.executePendingBindings()

        model.openWebSocketConnection(webSocketIpAddress!!)

        /*   ***OBSERVERS***   */
        observeForecastApiRecycler()

        observeSetupInfoChanges()

        observeScheduledDaysRecycler()

        observeAllPlansBottomSheet()

        /*   ***CLICK LISTENERS***   */
        appBarMenuItemListener()

        selectPlanListener()

        webSocketReconnectListener()

        wateringActionListener()

        navigateToEditPlanListener()

        navigateToCreatePlanListener()

        return binding.root
    }

    private fun setWebSocketIpAddress(addressValue : String){
        if(webSocketIpAddress == null)
            webSocketIpAddress = addressValue
    }

    private fun setForecastCity(cityName : String){
        if(city == null)
            city = cityName
    }

    private fun observeForecastApiRecycler(){
        val weatherAdapter = ForecastAdapter()
        binding.weatherRecycler.adapter = weatherAdapter
        model.apiResponse.observe(viewLifecycleOwner,{
            it?.forecast.let { forecast ->
                weatherAdapter.submitList(forecast)
            }
        })
    }

    private fun observeSetupInfoChanges(){
        model.setupInfo.observe(viewLifecycleOwner,{
            if(city != it.City) {
                model.responseFlag = true
                city = it.City
                model.getApiResponse(city!!)
            }

            if(webSocketIpAddress != it.IpAddress){
                webSocketIpAddress = it.IpAddress
                bottomSheetFragment.webSocketIpAddress = it.IpAddress
            }
        })
    }

    private fun observeScheduledDaysRecycler(){
        val adapter = DaysAdapter()
        binding.recyclerView.adapter = adapter
        model.scheduledDays.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })
    }

    private fun observeAllPlansBottomSheet(){
        model.allPlans.observe(viewLifecycleOwner, {
            bottomSheetFragment.listOfPlans = it
            bottomSheetFragment.webSocketIpAddress = webSocketIpAddress
        })
    }

    private fun appBarMenuItemListener(){

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
    }

    private fun selectPlanListener(){
        binding.mainButtonSelectPlan.setOnClickListener {
            bottomSheetFragment.show(requireActivity().supportFragmentManager, "BottomSheetFragment")
        }
    }

    private fun webSocketReconnectListener(){
        binding.buttonReconnect.setOnClickListener {
            model.signalCode=0
            model.openWebSocketConnection(webSocketIpAddress!!)
        }
    }

    private fun wateringActionListener(){
        binding.buttonWater.setOnClickListener {
            showSuccessDialog()
        }
    }

    private fun navigateToEditPlanListener(){
        binding.planName.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToEditFragment(webSocketIpAddress!!)
            this.findNavController().navigate(action)
        }
    }

    private fun navigateToCreatePlanListener(){
        binding.extendedFab.setOnClickListener{
            val action = MainFragmentDirections.actionMainFragmentToSecondaryFragment(webSocketIpAddress!!)
            this.findNavController().navigate(action)
        }
    }

    private fun showSuccessDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.watering_dialog_title))
            .setMessage(resources.getString(R.string.watering_dialog_text))
            .setPositiveButton(resources.getString(R.string.watering_dialog_positive_button_label)){ _, _ ->
                model.signalCode=1
                model.openWebSocketConnection(webSocketIpAddress!!)
            }
            .setNegativeButton(resources.getString(R.string.watering_dialog_negative_button_label)){ dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        model.getApiResponse(city!!)
    }


    override fun onStop() {
        super.onStop()
        model.closeWebSocketConnection()
    }


}

//TODO Testirati refactoring (observeri u posebnim funkcijama)
