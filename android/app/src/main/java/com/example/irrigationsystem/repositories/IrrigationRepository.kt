package com.example.irrigationsystem.repositories

import androidx.lifecycle.LiveData
import com.example.irrigationsystem.database.IrrigationDao
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.models.WateringScheduler

class IrrigationRepository(private val dao : IrrigationDao) {

    suspend fun insertPlan(plan : Plan) : Long{
      return dao.insertPlan(plan)
    }
}