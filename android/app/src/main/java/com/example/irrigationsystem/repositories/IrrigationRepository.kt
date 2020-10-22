package com.example.irrigationsystem.repositories

import com.example.irrigationsystem.database.IrrigationDao
import com.example.irrigationsystem.models.Plan

class IrrigationRepository(private val dao : IrrigationDao) {

    suspend fun insert(plan : Plan){
        dao.insertPlan(plan)
    }
}