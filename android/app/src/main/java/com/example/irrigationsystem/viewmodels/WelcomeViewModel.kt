package com.example.irrigationsystem.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.irrigationsystem.database.IrrigationSystemDatabase
import com.example.irrigationsystem.models.SetupInfo
import com.example.irrigationsystem.repositories.SetupRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class WelcomeViewModel(app : Application) : AndroidViewModel(app) {

    private val repo : SetupRepository
    private val _toSetup : MutableLiveData<Boolean> = MutableLiveData()
            val toSetup : LiveData<Boolean> get() = _toSetup

    private val _address : MutableLiveData<SetupInfo> = MutableLiveData()
    val address : LiveData<SetupInfo> get() = _address

    init {
        val dao = IrrigationSystemDatabase.getInstance(app.applicationContext).IrrigationDatabaseDao
        repo = SetupRepository(dao)
        checkToSetup()
    }

    fun checkToSetup(){
        viewModelScope.launch {
            val totalPlans = repo.getPlanCount()
            val totalDays = repo.getWeekDays()
            if(totalPlans > 0 && totalDays == 7) {
                _toSetup.postValue(false)
                getAddress()
            }
            else _toSetup.postValue(true)
        }
    }

    private fun getAddress(){
        viewModelScope.launch {
            _address.postValue(repo.getSetupInfo())
        }
    }
}