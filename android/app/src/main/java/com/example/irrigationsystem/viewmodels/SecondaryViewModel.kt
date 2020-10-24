package com.example.irrigationsystem.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
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
import java.text.SimpleDateFormat
import java.util.*

class SecondaryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : IrrigationRepository
    private var planDeferred = CompletableDeferred<Long>()
    private var wateringSchedulerDeffered = CompletableDeferred<Long>()

    @SuppressLint("SimpleDateFormat")
    val dateFormat = SimpleDateFormat("dd-MM-yyyy")


    private val chipIdMap = mapOf(
        2131230828 to 2, 2131230829 to 3, 2131230830 to 4,
        2131230831 to 5, 2131230832 to 6, 2131230833 to 7,
        2131230834 to 1
    )

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

    fun insertWateringScheduler(list : MutableList<Int>,planId : Int = 0){
        viewModelScope.launch {
            val pair : Pair<Date, MutableList<Int>> = DateHelper.getDateForCurrentSchedule(list)
            val ws = WateringScheduler(WateringTimeNow = TypeConverters.dateToTimestamp(pair.first),PlanId_FK = planId, WateringTimeNext = 0)
            wateringSchedulerDeffered.complete(repository.insertWateringScheduler(ws))
        }
    }

    fun insertWateringSchedulerDays(id : Int, list : MutableList<Int>){

        viewModelScope.launch {
            val transformedList = DateHelper.transformListIds(list)

            transformedList.forEach { it ->
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