package com.example.irrigationsystem.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.irrigationsystem.database.IrrigationSystemDatabase
import com.example.irrigationsystem.models.PlanWateringSchedulerView
import com.example.irrigationsystem.repositories.PlanRepository
import kotlinx.coroutines.launch

class BottomSheetViewModel(app : Application) : AndroidViewModel(app) {

    private val repo : PlanRepository

    private val _scheduler : MutableLiveData<PlanWateringSchedulerView> = MutableLiveData()
            val scheduler : LiveData<PlanWateringSchedulerView>
                get() = _scheduler

    private val _days : MutableLiveData<List<Int>> = MutableLiveData()
    val days : LiveData<List<Int>>
        get() = _days

    private val _fetched : MutableLiveData<Boolean> = MutableLiveData()
    val fetched : LiveData<Boolean>
        get() = _fetched

    init {
        val dao = IrrigationSystemDatabase.getInstance(app).IrrigationDatabaseDao
        repo = PlanRepository(dao)
        _fetched.value = false
    }

    fun changePlanActiveStatusExceptOne(planId : Int){
        viewModelScope.launch {
            repo.changePlanActiveStatusExceptOne(planId)
        }
    }

    fun setPlanAsActive(planId : Int) {
        viewModelScope.launch {
            repo.setPlanAsActive(planId)
            _scheduler.postValue(repo.getPlanWatering())
            _days.postValue(repo.getDays())

            _fetched.postValue(true)
        }
    }

    fun fetchedToFalse(){
        _fetched.value = false
    }

}
