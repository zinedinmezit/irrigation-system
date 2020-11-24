package com.example.irrigationsystem.repositories

import androidx.lifecycle.LiveData
import com.example.irrigationsystem.database.IrrigationDao
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.models.PlanWateringSchedulerView
import com.example.irrigationsystem.models.ScheduledDaysView
import com.example.irrigationsystem.models.WateringScheduler

class PlanRepository(private val dao : IrrigationDao) {

    suspend fun changePlanActiveStatusExceptOne(planId : Int){
        dao.changePlanActiveStatusExceptOne(planId)
    }

    suspend fun setPlanAsActive(planId : Int){
        dao.setPlanAsActive(planId)
    }

    suspend fun getPlanWatering() : PlanWateringSchedulerView{
       return dao.getPlanWatering()
    }

    suspend fun getDays() : List<Int>{
        return dao.getDays()
    }

}