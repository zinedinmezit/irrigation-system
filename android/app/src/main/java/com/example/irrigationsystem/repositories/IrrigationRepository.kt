package com.example.irrigationsystem.repositories

import com.example.irrigationsystem.database.IrrigationDao
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.models.WateringScheduler
import com.example.irrigationsystem.models.WateringSchedulerDays

class IrrigationRepository(private val dao : IrrigationDao) {

    val activePlan = dao.getPlanWateringView()
    val schedulerDays = dao.getScheduledDaysView()

    val allPlans = dao.getAllPlans()

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

    suspend fun setWateringTimeNow(time : Long){
        dao.setWateringTimeNow(time)
    }


    suspend fun deleteDaysFromScheduler(wsId : Int){
        dao.deleteDaysFromScheduler(wsId)
    }

    suspend fun updatePlan(planId: Int, name: String){
        dao.updatePlan(planId,name)
    }

    suspend fun updateWateringScheduler(wsId: Int, time: Long){
        dao.updateWateringTimeNow(wsId, time)
    }
}