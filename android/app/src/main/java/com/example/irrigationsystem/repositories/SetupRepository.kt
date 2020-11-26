package com.example.irrigationsystem.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.irrigationsystem.database.IrrigationDao

class SetupRepository(private val dao : IrrigationDao) {


    suspend fun getWeekDays() : Int{
        return dao.getWeekDaysNumber()
    }

    suspend fun getPlanTotalNumber() : Int{
        return dao.getPlansNumber()
    }

    suspend fun getAddress() : String{
        return dao.getIpAddress()
    }

}