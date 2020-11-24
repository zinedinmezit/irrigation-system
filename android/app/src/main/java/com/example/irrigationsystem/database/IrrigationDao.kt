package com.example.irrigationsystem.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.irrigationsystem.models.*


@Dao
interface IrrigationDao  {

    /* *****PLAN***** */

    @Query("SELECT * FROM `Plan`")
    fun getAllPlans() : LiveData<List<Plan>>

    @Query("SELECT * FROM `Plan` WHERE IsActive=1")
    fun getActivePlan() : LiveData<Plan>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: Plan): Long

    @Query("UPDATE `Plan` SET IsActive=0 WHERE PlanId!=:planId")
    suspend fun changePlanActiveStatusExceptOne(planId : Int)

    @Query("UPDATE `Plan` SET IsActive=1 WHERE PlanId=:planId")
    suspend fun setPlanAsActive(planId : Int)

    @Query("UPDATE `Plan` SET Name=:name WHERE PlanId=:planId")
    suspend fun updatePlan(planId:Int, name:String)


    /* *****WATERING SCHEDULER***** */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWateringScheduler(wateringScheduler: WateringScheduler): Long

    @Query("UPDATE `WateringScheduler` SET WateringTimeNow=:time")
    suspend fun setWateringTimeNow(time : Long)

    @Query("UPDATE WateringScheduler SET WateringTimeNow=:datetime, TimeString = :timeString WHERE WateringSchedulerId=:wsId")
    suspend fun updateWateringTimeNow(wsId: Int, datetime : Long, timeString: String)


    /* *****WATERING DAYS***** */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWateringSchedulerDay(xy : WateringSchedulerDays)

    @Query("DELETE FROM WateringSchedulerDays WHERE WateringSchedulerId=:wsId")
    suspend fun deleteDaysFromScheduler(wsId : Int)


    /* *****VIEWS***** */

    @Query("SELECT * FROM PlanWateringSchedulerView")
    fun getPlanWateringViewLiveData() : LiveData<PlanWateringSchedulerView>

    @Query("SELECT * FROM ScheduledDaysView")
     fun getScheduledDaysViewLiveData() : LiveData<List<ScheduledDaysView>>

    @Query("SELECT * FROM PlanWateringSchedulerView")
   suspend fun getPlanWatering() : PlanWateringSchedulerView

    @Query("SELECT OrdinalNumber FROM ScheduledDaysView")
   suspend fun getDays() : List<Int>



}