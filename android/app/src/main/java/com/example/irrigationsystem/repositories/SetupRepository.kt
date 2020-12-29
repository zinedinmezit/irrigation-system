package com.example.irrigationsystem.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.irrigationsystem.database.IrrigationDao
import com.example.irrigationsystem.models.SetupInfo

class SetupRepository(private val dao : IrrigationDao) {


    suspend fun getWeekDays() : Int{
        return dao.getWeekDaysNumber()
    }

    suspend fun getPlanTotalNumber() : Int{
        return dao.getPlansNumber()
    }

    suspend fun getSetupInfo() : SetupInfo{
        return dao.getSetupInfo()
    }

    suspend fun updateSetupInfo(address:String,
                                city:String){
        dao.updateSetupInfo(address,city)
    }


    suspend fun updateParameterValues(
                                tempMinValue : Double,
                                tempMaxValue : Double,
                                hummMinValue : Double,
                                hummMaxValue : Double){
        dao.updateParameterValues(tempMinValue, tempMaxValue, hummMinValue, hummMaxValue)
    }

}