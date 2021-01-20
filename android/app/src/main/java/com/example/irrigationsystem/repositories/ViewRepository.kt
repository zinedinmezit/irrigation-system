package com.example.irrigationsystem.repositories

import com.example.irrigationsystem.database.ViewsDao
import com.example.irrigationsystem.models.PlanWateringSchedulerView

class ViewRepository(private val dao : ViewsDao) {

    val getPlanWateringViewLiveData = dao.getPlanWateringViewLiveData()
    val getScheduledDaysViewLiveData = dao.getScheduledDaysViewLiveData()

    suspend fun getPlanWatering() : PlanWateringSchedulerView = dao.getPlanWatering()

    suspend fun getScheduledDays() : List<Int> = dao.getScheduledDays()

}