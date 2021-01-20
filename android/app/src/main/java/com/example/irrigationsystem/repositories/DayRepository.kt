package com.example.irrigationsystem.repositories

import com.example.irrigationsystem.database.DayDao

class DayRepository(private val dao : DayDao) {

    suspend fun getWeekDaysNumber() : Int = dao.getWeekDaysNumber()

    suspend fun insertWeekDays() = dao.insertWeekDays()

}