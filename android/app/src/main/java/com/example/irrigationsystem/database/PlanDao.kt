package com.example.irrigationsystem.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.irrigationsystem.models.Plan

@Dao
interface PlanDao {

    @Query("SELECT * FROM `Plan`")
    fun getAllPlans() : LiveData<List<Plan>>

    @Query("DELETE FROM `Plan` WHERE PlanId=:planId")
    suspend fun deletePlan(planId: Int)

    @Query("SELECT COUNT(PlanId) FROM `Plan`")
    suspend fun getPlanCount() : Int

    @Query("SELECT * FROM `Plan` WHERE IsActive=1")
    fun getActivePlan() : LiveData<Plan>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: Plan): Long

    @Query("UPDATE `Plan` SET IsActive=0 WHERE PlanId!=:planId")
    suspend fun updateAllStatusExceptChosen(planId : Int)

    @Query("UPDATE `Plan` SET IsActive=1 WHERE PlanId=:planId")
    suspend fun setPlanAsActive(planId : Int)

    @Query("UPDATE `Plan` SET Name=:name WHERE PlanId=:planId")
    suspend fun updatePlan(planId:Int, name:String)

}