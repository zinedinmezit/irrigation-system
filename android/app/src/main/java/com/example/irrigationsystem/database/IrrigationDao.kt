package com.example.irrigationsystem.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.irrigationsystem.models.*


@Dao
interface IrrigationDao  {


    //GET - Get info about active plan (There can be only 1 active plan)
    @Query("SELECT * FROM `Plan` WHERE IsActive=1")
    fun getActivePlan() : LiveData<Plan>

    //GET - View that gives us info about relation between active plan and his scheduler
    @Query("SELECT * FROM PlanWateringSchedulerView")
    fun getPlanWateringView() : LiveData<PlanWateringSchedulerView>

    //GET - View that gives us info about active plan's scheduled days
    @Query("SELECT * FROM ScheduledDaysView")
    fun getScheduledDaysView() : LiveData<List<ScheduledDaysView>>

    //POST - Plan
    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertPlan(plan: Plan): Long

    //POST - WateringScheduler
    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertWateringScheduler(wateringScheduler: WateringScheduler): Long

    //POST - NotificationScheduler
    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun insertNotificationScheduler(notificationScheduler : NotificationScheduler)

    //POST - WateringSchedulerDay
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWateringSchedulerDay(xy : WateringSchedulerDays)

    //UPDATE - Set all plans inactive except targeted
    @Query("UPDATE `Plan` SET IsActive=0 WHERE PlanId!=:planId")
    suspend fun changePlanActiveStatusExceptOne(planId : Int)

    //UPDATE - Set targeted plan as active
    @Query("UPDATE `Plan` SET IsActive=1 WHERE PlanId=:planId")
    suspend fun setPlanAsActive(planId : Int)

    @Query("UPDATE `WateringScheduler` SET WateringTimeNow=:time")
    suspend fun setWateringTimeNow(time : Long)

}