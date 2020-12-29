package com.example.irrigationsystem.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.irrigationsystem.models.*


@Dao
interface IrrigationDao  {

    /* *****PLAN***** */

    @Query("SELECT * FROM `Plan`")
    fun getAllPlans() : LiveData<List<Plan>>

    @Query("DELETE FROM `Plan` WHERE PlanId=:planId")
    suspend fun deletePlan(planId: Int)

    @Query("SELECT COUNT(PlanId) FROM `Plan`")
    suspend fun getPlansNumber() : Int

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

    @Query("UPDATE `WateringScheduler` SET WateringTimeNow=:time WHERE WateringSchedulerId=:wsId")
    suspend fun setWateringTimeNow(time : Long, wsId: Int)

    @Query("UPDATE WateringScheduler SET WateringTimeNow=:datetime, TimeString = :timeString WHERE WateringSchedulerId=:wsId")
    suspend fun updateWateringTimeNow(wsId: Int, datetime : Long, timeString: String)

    /* *****DAYS***** */
    @Query("SELECT COUNT(DayId) FROM Day")
    suspend fun getWeekDaysNumber() : Int

    @Query("INSERT INTO Day VALUES (1,1,'SUN'),(2,2, 'MON'),(3,3, 'TUE'),(4,4, 'WED'),(5,5, 'THU'),(6,6, 'FRI'),(7,7, 'SAT')")
    suspend fun insertWeekDays()

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
   suspend fun getScheduledDays() : List<Int>

    /* *****WEBSOCKET SERVER***** */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWSServer(wsServer: SetupInfo)

    @Query("SELECT * FROM SetupInfo LIMIT 1")
    suspend fun getSetupInfo() : SetupInfo

    @Query("UPDATE SetupInfo SET " +
            "IpAddress=:address," +
            "City=:city" )
    suspend fun updateSetupInfo(address : String,
                                city : String,
                               )

    @Query("UPDATE SetupInfo SET " +
            "TemperatureMinLimit=:tempMinValue," +
            "TemperatureMaxLimit=:tempMaxValue," +
            "HummidityMinLimit=:hummMinValue," +
            "HummidityMaxLimit=:hummMaxValue")
    suspend fun updateParameterValues(
                                tempMinValue : Double,
                                tempMaxValue : Double,
                                hummMinValue : Double,
                                hummMaxValue : Double)

    @Query("SELECT * FROM SetupInfo")
    fun getSetupInfoLiveData() : LiveData<SetupInfo>


}