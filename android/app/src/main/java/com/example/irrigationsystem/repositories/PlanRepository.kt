package com.example.irrigationsystem.repositories

import com.example.irrigationsystem.database.IrrigationDao
import com.example.irrigationsystem.models.Plan

class PlanRepository(private val dao : IrrigationDao) {

    val scheduledDays = dao.getOrdinalNumbersFromScheduledDays()

    suspend fun changePlanActiveStatusExceptOne(planId : Int){
        dao.changePlanActiveStatusExceptOne(planId)
    }

    suspend fun setPlanAsActive(planId : Int){
        dao.setPlanAsActive(planId)
    }


}