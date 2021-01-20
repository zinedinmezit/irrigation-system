package com.example.irrigationsystem.repositories

import com.example.irrigationsystem.database.WateringSchedulerDao
import com.example.irrigationsystem.models.WateringScheduler

class WateringSchedulerRepository(private val dao : WateringSchedulerDao) {

    suspend fun insertWateringScheduler(wateringScheduler: WateringScheduler): Long = dao.insertWateringScheduler(wateringScheduler)

    suspend fun setWateringTimeNow(time : Long, wsId: Int) = dao.setWateringTimeNow(time, wsId)

    suspend fun updateWateringTimeNow(wsId: Int,
                                      datetime : Long,
                                      timeString: String,
                                      wateringDuration : Long) = dao.updateWateringTimeNow(wsId, datetime, timeString, wateringDuration)


}