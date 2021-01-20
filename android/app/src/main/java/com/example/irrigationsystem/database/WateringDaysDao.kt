package com.example.irrigationsystem.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.irrigationsystem.models.WateringSchedulerDays

@Dao
interface WateringDaysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWateringSchedulerDay(xy : WateringSchedulerDays)

    @Query("DELETE FROM WateringSchedulerDays WHERE WateringSchedulerId=:wsId")
    suspend fun deleteDaysFromScheduler(wsId : Int)
}