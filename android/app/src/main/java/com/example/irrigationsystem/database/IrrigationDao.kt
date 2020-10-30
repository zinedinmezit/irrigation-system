package com.example.irrigationsystem.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.irrigationsystem.models.*


@Dao
interface IrrigationDao  {

    //POST - Plan
    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertPlan(plan: Plan): Long

    //POST - WateringScheduler
    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertWateringScheduler(wateringScheduler: WateringScheduler): Long

    //POST - NotificationScheduler
    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertNotificationScheduler(notificationScheduler : NotificationScheduler)

    //POST- Day
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDays(days : Day)

    //POST - WateringSchedulerDay
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWateringSchedulerDay(xy : WateringSchedulerDays)

    @Query("UPDATE `Plan` SET IsActive=0 WHERE PlanId!=:planId")
    suspend fun changePlanActiveStatusExceptOne(planId : Int)

    @Query("UPDATE `Plan` SET IsActive=1 WHERE PlanId=:planId")
    suspend fun setPlanAsActive(planId : Int)

    @Query("SELECT * FROM `Plan` WHERE IsActive=1")
    fun getActivePlan() : LiveData<Plan>

    @Query("SELECT * FROM PlanWateringSchedulerView")
    fun getPlanWateringView() : LiveData<PlanWateringSchedulerView>

    @Query("SELECT * FROM ScheduledDaysView")
    fun getScheduledDaysView() : LiveData<List<ScheduledDaysView>>

}