package com.example.irrigationsystem.viewmodels

import android.app.Application
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

    private val _planSchedulerView : MutableLiveData<PlanWateringSchedulerView> = MutableLiveData()
            val planSchedulerView : LiveData<PlanWateringSchedulerView>
                get() = _planSchedulerView

    private val _scheduledDays : MutableLiveData<List<Int>> = MutableLiveData()
    val scheduledDays : LiveData<List<Int>>
        get() = _scheduledDays

    private val _isFetched : MutableLiveData<Boolean> = MutableLiveData()
    val isFetched : LiveData<Boolean>
        get() = _isFetched

    init {
        val dao = IrrigationSystemDatabase.getInstance(app).IrrigationDatabaseDao
        repo = PlanRepository(dao)
        _isFetched.value = false
    }

    fun changePlanActiveStatusExceptOne(planId : Int){
        viewModelScope.launch {
            repo.changePlanActiveStatusExceptOne(planId)
        }
    }

    fun setPlanAsActive(planId : Int) {
        viewModelScope.launch {
            repo.setPlanAsActive(planId)
            _planSchedulerView.postValue(repo.getPlanWatering())
            _scheduledDays.postValue(repo.getDays())

            _isFetched.postValue(true)
        }
    }

    fun deletePlan(planId : Int){
        viewModelScope.launch {
            repo.deletePlan(planId)
        }
    }

    fun fetchedToFalse(){
        _isFetched.value = false
    }

    fun setWateringTImeNow(time : Long, wsId : Int){
        viewModelScope.launch {
            repo.setWateringTimeNow(time,wsId)
        }
    }

}
