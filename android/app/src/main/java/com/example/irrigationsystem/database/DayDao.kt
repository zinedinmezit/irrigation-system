package com.example.irrigationsystem.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface DayDao {

    @Query("SELECT COUNT(DayId) FROM Day")
    suspend fun getWeekDaysNumber() : Int

    @Query("INSERT INTO Day VALUES (1,1,'SUN'),(2,2, 'MON'),(3,3, 'TUE'),(4,4, 'WED'),(5,5, 'THU'),(6,6, 'FRI'),(7,7, 'SAT')")
    suspend fun insertWeekDays()
}