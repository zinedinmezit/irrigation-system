package com.example.irrigationsystem.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.irrigationsystem.database.IrrigationDao
import com.example.irrigationsystem.models.SetupInfo
import kotlinx.coroutines.launch

class SetupRepository(private val dao : IrrigationDao) {


    suspend fun getWeekDays() : Int{
        return dao.getWeekDaysNumber()
    }

    suspend fun getPlanCount() : Int{
        return dao.getPlanCount()
    }

    suspend fun getSetupInfo() : SetupInfo{
        return dao.getSetupInfo()
    }

    suspend fun updateSetupInfo(address:String,
                                city:String){
        dao.updateSetupInfo(address,city)
    }

   suspend fun updateCity(city : String){
        dao.updateCity(city)
    }

   suspend fun updateServerIpAddress(address : String){
       dao.updateServerIpAddress(address)
    }


    suspend fun updateParameterValues(
                                tempMinValue : Double,
                                tempMaxValue : Double,
                                hummMinValue : Double,
                                hummMaxValue : Double){
        dao.updateParameterValues(tempMinValue, tempMaxValue, hummMinValue, hummMaxValue)
    }

}