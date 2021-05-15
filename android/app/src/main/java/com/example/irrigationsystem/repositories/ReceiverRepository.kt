package com.example.irrigationsystem.repositories

import com.example.irrigationsystem.database.IrrigationDao
import com.example.irrigationsystem.models.PlanWateringSchedulerView
import com.example.irrigationsystem.models.SetupInfo

class ReceiverRepository(val dao : IrrigationDao) {


    suspend fun getScheduledDays() : List<Int>{
        return dao.getScheduledDays()
    }

    suspend fun getPlanSchedulerView() : PlanWateringSchedulerView {
        return dao.getPlanWatering()
    }

    suspend fun getSetupInfo() : SetupInfo{
        return dao.getSetupInfo()
    }

}