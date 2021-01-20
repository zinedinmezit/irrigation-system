package com.example.irrigationsystem.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.irrigationsystem.models.PlanWateringSchedulerView
import com.example.irrigationsystem.models.ScheduledDaysView

@Dao
interface ViewsDao {

    @Query("SELECT * FROM PlanWateringSchedulerView")
    fun getPlanWateringViewLiveData() : LiveData<PlanWateringSchedulerView>

    @Query("SELECT * FROM ScheduledDaysView")
    fun getScheduledDaysViewLiveData() : LiveData<List<ScheduledDaysView>>

    @Query("SELECT * FROM PlanWateringSchedulerView")
    suspend fun getPlanWatering() : PlanWateringSchedulerView

    @Query("SELECT OrdinalNumber FROM ScheduledDaysView")
    suspend fun getScheduledDays() : List<Int>
}