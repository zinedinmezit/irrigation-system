package com.example.irrigationsystem.repositories

import com.example.irrigationsystem.database.SetupInfoDao
import com.example.irrigationsystem.models.SetupInfo

class SetRepository(private val dao : SetupInfoDao) {

    val setupInfoLiveData = dao.getSetupInfoLiveData()

    suspend fun getSetupInfo() : SetupInfo = dao.getSetupInfo()

    suspend fun insertSetupInfo(setupInfo : SetupInfo) = dao.insertSetupInfo(setupInfo)

    suspend fun updateSetupInfo(address : String, city : String) = dao.updateSetupInfo(address, city)

    suspend fun updateParameterValues(
        tempMinValue : Double,
        tempMaxValue : Double,
        hummMinValue : Double,
        hummMaxValue : Double) = dao.updateParameterValues(tempMinValue, tempMaxValue, hummMinValue, hummMaxValue)
}