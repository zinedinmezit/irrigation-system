package com.example.irrigationsystem.repositories

import androidx.lifecycle.LiveData
import com.example.irrigationsystem.database.IrrigationDao
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.models.WateringScheduler
import com.example.irrigationsystem.models.WateringSchedulerDays

class IrrigationRepository(private val dao : IrrigationDao) {

    suspend fun insertPlan(plan : Plan) : Long{
      return dao.insertPlan(plan)
    }

    suspend fun insertWateringSchDay(xy : WateringSchedulerDays){
        dao.insertWateringSchedulerDay(xy)
    }

    suspend fun insertWateringScheduler(scheduler : WateringScheduler): Long{
        return dao.insertWateringScheduler(scheduler)
    }

    suspend fun changePlanActiveStatusExceptOne(planId : Int){
        dao.changePlanActiveStatusExceptOne(planId)
    }

    suspend fun setPlanAsActive(planId : Int){
        dao.setPlanAsActive(planId)
    }
}