package com.example.irrigationsystem.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.irrigationsystem.models.PlanAndNotifying
import com.example.irrigationsystem.models.PlanAndWatering
import com.example.irrigationsystem.models.WateringSchedulerWithDays


@Dao
interface IrrigationDao  {

    //GET - Plan 1:1 WateringScheduler
    @Transaction
    @Query("SELECT * FROM `Plan`")
    fun getPlanWatering(): List<PlanAndWatering>

    //GET - Plan 1:1 NotificationScheduler
    @Transaction
    @Query("SELECT * FROM `Plan`")
    fun getPlanNotifying(): List<PlanAndNotifying>

    //GET - WateringScheduler N:N Days
    @Transaction
    @Query("SELECT * FROM WateringScheduler")
    fun getWateringDays(): List<WateringSchedulerWithDays>

}