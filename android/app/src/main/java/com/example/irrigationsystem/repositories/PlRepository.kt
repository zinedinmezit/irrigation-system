package com.example.irrigationsystem.repositories

import com.example.irrigationsystem.database.PlanDao
import com.example.irrigationsystem.models.Plan

class PlRepository(private val dao : PlanDao) {

    val allPlans = dao.getAllPlans()
    val activePlan = dao.getActivePlan()

    suspend fun insertPlan(plan : Plan) : Long = dao.insertPlan(plan)

    suspend fun updatePlan(planId: Int, name: String) = dao.updatePlan(planId,name)

    suspend fun deletePlan(planId : Int) = dao.deletePlan(planId)

    suspend fun getPlanCount() : Int =  dao.getPlanCount()

    suspend fun changePlanActiveStatusExceptOne(planId : Int) = dao.updateAllStatusExceptChosen(planId)

    suspend fun setPlanAsActive(planId : Int) = dao.setPlanAsActive(planId)


}