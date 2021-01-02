package com.example.irrigationsystem.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.irrigationsystem.database.IrrigationSystemDatabase
import com.example.irrigationsystem.helpers.DateDaysHelper
import com.example.irrigationsystem.helpers.TypeConverters
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.models.WateringScheduler
import com.example.irrigationsystem.models.WateringSchedulerDays
import com.example.irrigationsystem.models.SetupInfo
import com.example.irrigationsystem.repositories.IrrigationRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class SetupViewModel(app : Application) : AndroidViewModel(app) {

    private val repository : IrrigationRepository
    private var planDeferred = CompletableDeferred<Long>()
    private var wateringSchedulerDeffered = CompletableDeferred<Long>()

    init {
        val irrigationRepositoryDao = IrrigationSystemDatabase.getInstance(app).IrrigationDatabaseDao
        repository = IrrigationRepository(irrigationRepositoryDao)
    }

    fun insertNote(plan : Plan)
    {
        viewModelScope.launch {
            planDeferred.complete(repository.insertPlan(plan))
        }
    }

    fun insertServer(server : SetupInfo)
    {
        viewModelScope.launch {
            repository.insertServer(server)
        }
    }

    fun insertWateringScheduler(list : MutableList<Int>,timeString : String, wateringDuration : Long, planId : Int = 0) : Long{
        val pair : Pair<Date, MutableList<Int>> = DateDaysHelper.getDateForCurrentSchedule(list, timeString)
        viewModelScope.launch {
            val ws = WateringScheduler(WateringTimeNow = TypeConverters.dateToTimestamp(pair.first),PlanId_FK = planId,TimeString = timeString, WateringDuration = wateringDuration)
            wateringSchedulerDeffered.complete(repository.insertWateringScheduler(ws))
        }

        return pair.first.time
    }

    fun insertWateringSchedulerDays(id : Int, list : MutableList<Int>){
            try {
                Log.i("testtest","Inside try catch")
                list.forEach {
                    val wsd = WateringSchedulerDays(id, it)
                    viewModelScope.launch {
                        repository.insertWateringSchDay(wsd)
                    }
                }
            }catch (ex : Exception){
                Log.i("testtest","aaaaa ${ex.message}")
            }

    }

    suspend fun getLatestPlanId() : Long {
        val id = planDeferred.await()
        planDeferred = CompletableDeferred()

        return id
    }

     fun insertWeekDays(){
        viewModelScope.launch {
            repository.insertWeekDays()
        }
    }

    suspend fun getLatestWateringSchedulerId() : Long {
        val id = wateringSchedulerDeffered.await()
        wateringSchedulerDeffered = CompletableDeferred()

        return id
    }

    fun changePlanActiveStatusExceptOne(planId : Int){
        viewModelScope.launch {
            repository.changePlanActiveStatusExceptOne(planId)
        }
    }
}