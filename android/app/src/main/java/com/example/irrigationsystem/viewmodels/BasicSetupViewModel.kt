package com.example.irrigationsystem.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.irrigationsystem.database.IrrigationSystemDatabase
import com.example.irrigationsystem.repositories.SetupRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class BasicSetupViewModel(app : Application) : AndroidViewModel(app) {

    private val repository : SetupRepository
    private val scope = CoroutineScope(context = Dispatchers.IO)

    init {
        val dao = IrrigationSystemDatabase.getInstance(app).IrrigationDatabaseDao
        repository = SetupRepository(dao)
    }


    fun updateCity(city : String){
        scope.launch {
            repository.updateCity(city)
        }
    }

    fun updateServerIpAddress(address : String){
        scope.launch {
            repository.updateServerIpAddress(address)
        }
    }

    fun updateParameterValues(
                        tempMinValue : Double,
                        tempMaxValue : Double,
                        hummMinValue : Double,
                        hummMaxValue : Double){
        scope.launch {
            repository.updateParameterValues(tempMinValue, tempMaxValue, hummMinValue, hummMaxValue)
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}