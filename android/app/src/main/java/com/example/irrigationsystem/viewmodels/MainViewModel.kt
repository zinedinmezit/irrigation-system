package com.example.irrigationsystem.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.irrigationsystem.database.IrrigationSystemDatabase
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.models.PlanWateringSchedulerView
import com.example.irrigationsystem.models.ScheduledDaysView
import com.example.irrigationsystem.repositories.IrrigationRepository
import kotlinx.coroutines.launch
import okhttp3.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : IrrigationRepository
    private var _isConnectionEstablished : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
            val isConnectionEstablished : LiveData<Boolean>
                get() = _isConnectionEstablished

    private val _isConnectionEstablishedString = MutableLiveData<String>()
    val isConnectionEstablishedString: LiveData<String>
        get() = _isConnectionEstablishedString

   private val _hummidityPercentageValue = MutableLiveData<String>()
    val hummidityPercentageValue: LiveData<String>
        get() = _hummidityPercentageValue

    val activePlan : LiveData<PlanWateringSchedulerView>
    val scheduledDays : LiveData<List<ScheduledDaysView>>

    init {
        val dao = IrrigationSystemDatabase.getInstance(application).IrrigationDatabaseDao
        _isConnectionEstablished.value = false
        _isConnectionEstablishedString.value = "DISCONNECTED"
        repository = IrrigationRepository(dao)

        activePlan = repository.activePlan
        scheduledDays = repository.schedulerDays
    }

    var signalCode : Int = 0

    var currentEvent : String = "Initial"

     val wsListener : WebSocketListener = object :WebSocketListener(){

        override fun onOpen(webSocket: WebSocket, response: Response) {
            when(signalCode){
                0 -> {
                    _isConnectionEstablishedString.postValue("CONNECTED")
                    _isConnectionEstablished.postValue(true)
                }
                1 -> {
                        currentEvent = "Command"
                        signalCode = 0
                        webSocket.send("ON")
                        webSocket.close(1000, null)
                }
            }
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            if(currentEvent == "Initial"){
                _isConnectionEstablished.postValue(false)
                _isConnectionEstablishedString.postValue("DISCONNECTED")
            }
            else
            {
                currentEvent = "Initial"
            }

        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            _hummidityPercentageValue.postValue(text)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            if(_isConnectionEstablished.value!!) {
                _isConnectionEstablished.postValue(false)
                _isConnectionEstablishedString.postValue("DISCONNECTED")
            }
        }
    }

    fun getPlanId() : Int? = activePlan.value?.PlanId
}

//TODO Extract stringove u enumeracij (npr.DISCONNECTED i CONNECTED)
//TODO Optimizirati tranzicije kada konekcija izmedju klijenta i servera prelazi iz jednog stanja u drugo