package com.example.irrigationsystem.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.irrigationsystem.models.WateringScheduler

@Dao
interface WateringSchedulerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWateringScheduler(wateringScheduler: WateringScheduler): Long

    @Query("UPDATE `WateringScheduler` SET WateringTimeNow=:time WHERE WateringSchedulerId=:wsId")
    suspend fun setWateringTimeNow(time : Long, wsId: Int)

    @Query("UPDATE WateringScheduler SET WateringTimeNow=:datetime, TimeString = :timeString, WateringDuration = :wateringDuration WHERE WateringSchedulerId=:wsId")
    suspend fun updateWateringTimeNow(wsId: Int, datetime : Long, timeString: String, wateringDuration : Long)

}