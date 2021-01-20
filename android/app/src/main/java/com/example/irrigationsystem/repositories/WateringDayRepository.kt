package com.example.irrigationsystem.repositories

import com.example.irrigationsystem.database.WateringDaysDao
import com.example.irrigationsystem.models.WateringSchedulerDays

class WateringDayRepository(private val dao : WateringDaysDao) {

    suspend fun insertWateringSchedulerDay(xy : WateringSchedulerDays) = dao.insertWateringSchedulerDay(xy)

    suspend fun deleteDaysFromScheduler(wsId : Int) = dao.deleteDaysFromScheduler(wsId)


}