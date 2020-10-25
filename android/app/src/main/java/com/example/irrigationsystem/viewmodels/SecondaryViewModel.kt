package com.example.irrigationsystem.viewmodels

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import androidx.lifecycle.*
import com.example.irrigationsystem.database.IrrigationSystemDatabase
import com.example.irrigationsystem.helpers.DateHelper
import com.example.irrigationsystem.helpers.TypeConverters
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.models.WateringScheduler
import com.example.irrigationsystem.models.WateringSchedulerDays
import com.example.irrigationsystem.repositories.IrrigationRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import java.util.*

class SecondaryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : IrrigationRepository
    private var planDeferred = CompletableDeferred<Long>()
    private var wateringSchedulerDeffered = CompletableDeferred<Long>()

    init {
        val irrigationRepositoryDao = IrrigationSystemDatabase.getInstance(application).IrrigationDatabaseDao
        repository = IrrigationRepository(irrigationRepositoryDao)
    }

     fun insertNote(plan : Plan)
    {
        viewModelScope.launch {
          planDeferred.complete(repository.insertPlan(plan))
        }
    }

    fun insertWateringScheduler(list : MutableList<Int>,timeString : String,planId : Int = 0) : Long{
        val pair : Pair<Date, MutableList<Int>> = DateHelper.getDateForCurrentSchedule(list, timeString)
        viewModelScope.launch {
            val ws = WateringScheduler(WateringTimeNow = TypeConverters.dateToTimestamp(pair.first),PlanId_FK = planId, WateringTimeNext = 0)
            wateringSchedulerDeffered.complete(repository.insertWateringScheduler(ws))
        }

        return pair.first.time
    }

    fun insertWateringSchedulerDays(id : Int, list : MutableList<Int>){

        viewModelScope.launch {
            val transformedList = DateHelper.transformListIds(list)

            transformedList.forEach {
                val wsd = WateringSchedulerDays(id,it)
                repository.insertWateringSchDay(wsd)
            }
        }
    }

    suspend fun getLatestPlanId() : Long {
       val id = planDeferred.await()
        planDeferred = CompletableDeferred()

        return id
    }

    suspend fun getLatestWateringSchedulerId() : Long {
        val id = wateringSchedulerDeffered.await()
        wateringSchedulerDeffered = CompletableDeferred()

        return id
    }

}