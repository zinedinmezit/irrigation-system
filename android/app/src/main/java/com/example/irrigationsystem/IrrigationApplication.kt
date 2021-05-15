package com.example.irrigationsystem

import android.app.Application
import com.example.irrigationsystem.database.IrrigationSystemDatabase
import com.example.irrigationsystem.repositories.ReceiverRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class IrrigationApplication : Application() {

    private val DispatcherIo = Dispatchers.IO


    val applicationScopeIO by lazy { CoroutineScope(DispatcherIo + Job()) }
    val database by lazy { IrrigationSystemDatabase.getInstance(this) }
    val receiverRepository by lazy { ReceiverRepository(database.IrrigationDatabaseDao) }
}