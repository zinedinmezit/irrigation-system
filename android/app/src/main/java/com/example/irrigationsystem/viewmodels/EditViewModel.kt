package com.example.irrigationsystem.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.irrigationsystem.database.IrrigationSystemDatabase
import com.example.irrigationsystem.helpers.DateDaysHelper
import com.example.irrigationsystem.models.PlanWateringSchedulerView
import com.example.irrigationsystem.models.ScheduledDaysView
import com.example.irrigationsystem.models.WateringSchedulerDays
import com.example.irrigationsystem.repositories.IrrigationRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import java.util.*

class EditViewModel(application : Application) : AndroidViewModel(application){

    private val irrigationRepository : IrrigationRepository

    val activePlan : LiveData<PlanWateringSchedulerView>
    val scheduledDays : LiveData<List<ScheduledDaysView>>

    init {
        val dao = IrrigationSystemDatabase.getInstance(application).IrrigationDatabaseDao
        irrigationRepository = IrrigationRepository(dao)
        activePlan = irrigationRepository.activePlan
        scheduledDays = irrigationRepository.schedulerDays
    }

    fun updatePlan(name : String){
        viewModelScope.launch {
            irrigationRepository.updatePlan(activePlan.value?.PlanId!!, name)
        }
    }

    fun deleteDaysFromScheduler(){
        viewModelScope.launch {
            irrigationRepository.deleteDaysFromScheduler(activePlan.value?.WateringSchedulerId!!)
        }
    }

    fun insertWateringSchedulerDays(list : MutableList<Int>){
        val id = activePlan.value?.WateringSchedulerId!!
            try {
                list.forEach {
                    val wsd = WateringSchedulerDays(id, it)
                    viewModelScope.launch {
                        irrigationRepository.insertWateringSchDay(wsd)
                    }
                }
            }catch (ex : Exception){ }
    }

    fun updateWateringScheduler(list : MutableList<Int>, timeString : String, wateringDuration : Long) : Long{
        val pair : Pair<Date, MutableList<Int>> = DateDaysHelper.getDateForCurrentSchedule(list, timeString)
        viewModelScope.launch {
            irrigationRepository.updateWateringScheduler(activePlan.value?.WateringSchedulerId!!,pair.first.time,timeString, wateringDuration)
        }

        return pair.first.time
    }
}