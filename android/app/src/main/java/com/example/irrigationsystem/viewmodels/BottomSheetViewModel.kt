package com.example.irrigationsystem.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.irrigationsystem.database.IrrigationSystemDatabase
import com.example.irrigationsystem.repositories.PlanRepository
import kotlinx.coroutines.launch

class BottomSheetViewModel(app : Application) : AndroidViewModel(app) {

    private val repo : PlanRepository

    init {
        val dao = IrrigationSystemDatabase.getInstance(app).IrrigationDatabaseDao
        repo = PlanRepository(dao)
    }

    fun changePlanActiveStatusExceptOne(planId : Int){
        viewModelScope.launch {
            repo.changePlanActiveStatusExceptOne(planId)
        }
    }

    fun setPlanAsActive(planId : Int) {
        viewModelScope.launch {
            repo.setPlanAsActive(planId)
        }
    }

}
