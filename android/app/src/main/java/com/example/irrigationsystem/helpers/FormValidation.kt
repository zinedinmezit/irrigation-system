package com.example.irrigationsystem.helpers

import com.example.irrigationsystem.databinding.FragmentEditBinding
import com.example.irrigationsystem.databinding.FragmentSecondaryBinding
import com.example.irrigationsystem.databinding.FragmentSetupBinding

object FormValidation{

    fun secondaryFormValidation(planName : String?, timeString : String?, binding : FragmentSecondaryBinding) : Boolean{

        var flag = true


        if(planName.isNullOrBlank()){
            binding.secondaryTextField.error = "Plan name can't be empty"
            flag = false
        }
        else{
            binding.secondaryTextField.error = null
        }

        if(timeString.isNullOrBlank()){
            binding.secondaryEditTextTime.error = "Time can't be empty"
            flag = false
        }
        else{
            binding.secondaryEditTextTime.error = null
        }

        return flag
    }

    fun editFormValidation(planName : String?, timeString : String?, binding : FragmentEditBinding) : Boolean{

        var flag = true


        if(planName.isNullOrBlank()){
            binding.editInputLayout.error = "Plan name can't be empty"
            flag = false
        }
        else{
            binding.editInputLayout.error = null
        }

        if(timeString.isNullOrBlank()){
            binding.editTimeText.error = "Time can't be empty"
            flag = false
        }
        else{
            binding.editTimeText.error = null
        }

        return flag
    }


    fun setupFormValidator(planName : String?,
                             timeString : String?,
                             ipAddress : String?,
                             city : String?,
                             tempMin : String?,
                             tempMax : String?,
                             hummMin : String?,
                             hummMax : String?,
                             binding : FragmentSetupBinding) : Boolean{
        var flag = true
        if(planName.isNullOrBlank()){
            binding.setupTextField.error = "Plan name can't be empty"
            flag = false
        }
        else{
            binding.setupTextField.error = null
        }

        if(timeString.isNullOrBlank()){
            binding.setupTimeString.error = "Time can't be empty"
            flag = false
        }
        else{
            binding.setupTimeString.error = null
        }

        if(ipAddress.isNullOrBlank()){
            binding.setupIpAddress.error = "Websocket IP Address can't be empty"
            flag = false
        }
        else{
            binding.setupIpAddress.error = null
        }

        if(city.isNullOrBlank()){
            binding.setupCity.error = "City can't be empty"
            flag = false
        }
        else{
            binding.setupCity.error = null
        }

        if(tempMin.isNullOrBlank()){
            binding.setupTempMinEditText.error = "Can't be empty"
            flag = false
        }
        else{
            binding.setupTempMinEditText.error = null
        }

        if(tempMax.isNullOrBlank()){
            binding.setupTempMaxEditText.error = "Can't be empty"
            flag = false
        }
        else{
            binding.setupTempMaxEditText.error = null
        }

        if(hummMin.isNullOrBlank()){
            binding.setupHummMinEditText.error = "Can't be empty"
            flag = false
        }
        else{
            binding.setupHummMinEditText.error = null
        }

        if(hummMax.isNullOrBlank()){
            binding.setupHummMaxEditText.error = "Can't be empty"
            flag = false
        }
        else{
            binding.setupHummMaxEditText.error = null
        }

        return flag
    }

}