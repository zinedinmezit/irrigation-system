package com.example.irrigationsystem.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.irrigationsystem.database.IrrigationSystemDatabase
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.repositories.IrrigationRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class SecondaryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : IrrigationRepository
    private var planDeferred = CompletableDeferred<Long>()

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

    suspend fun getLatestPlanId() : Long {
       val id = planDeferred.await()
        planDeferred = CompletableDeferred()

        return id
    }
}