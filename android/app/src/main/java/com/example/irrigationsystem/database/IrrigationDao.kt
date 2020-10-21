package com.example.irrigationsystem.database

import androidx.room.*
import com.example.irrigationsystem.models.*


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

    //POST - Plan
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlan(plan : Plan) : Long
    //POST - WateringScheduler
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWateringScheduler(wateringScheduler : WateringScheduler)
    //POST - NotificationScheduler
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotificationScheduler(notificationScheduler : NotificationScheduler)
    //POST- Day
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDays(days : Day)
    //POST - WateringSchedulerDay
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWateringSchedulerDay(xy : WateringSchedulerDays)



}