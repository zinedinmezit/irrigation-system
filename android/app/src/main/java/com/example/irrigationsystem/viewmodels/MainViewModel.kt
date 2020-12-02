package com.example.irrigationsystem.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.irrigationsystem.database.IrrigationSystemDatabase
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.models.PlanWateringSchedulerView
import com.example.irrigationsystem.models.ScheduledDaysView
import com.example.irrigationsystem.models.weatherapi.WeatherObject
import com.example.irrigationsystem.network.WeatherApi
import com.example.irrigationsystem.network.WeatherApiService
import com.example.irrigationsystem.repositories.IrrigationRepository
import kotlinx.coroutines.launch
import retrofit2.Callback
import okhttp3.*
import retrofit2.Call

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : IrrigationRepository
    private var _isConnectionEstablished : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
            val isConnectionEstablished : LiveData<Boolean>
                get() = _isConnectionEstablished

   private val _hummidityPercentageValue = MutableLiveData<String>()
    val hummidityPercentageValue: LiveData<String>
        get() = _hummidityPercentageValue

    val activePlan : LiveData<PlanWateringSchedulerView>
    val scheduledDays : LiveData<List<ScheduledDaysView>>

    val allPlans : LiveData<List<Plan>>

    private val _apiResponse : MutableLiveData<WeatherObject> = MutableLiveData()
            val apiResponse : LiveData<WeatherObject> get() = _apiResponse

    init {
        val dao = IrrigationSystemDatabase.getInstance(application).IrrigationDatabaseDao
        _isConnectionEstablished.value = false
        repository = IrrigationRepository(dao)

        activePlan = repository.activePlan
        scheduledDays = repository.schedulerDays
        allPlans = repository.allPlans
    }

    var signalCode : Int = 0

    var currentEvent : String = "Initial"

     val wsListener : WebSocketListener = object :WebSocketListener(){

        override fun onOpen(webSocket: WebSocket, response: Response) {
            when(signalCode){
                0 -> {
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
            }
        }
    }

    fun getPlanId() : Int? = activePlan.value?.PlanId

     fun getApiResponse() {
        viewModelScope.launch {
            try {
                val response = WeatherApi.retrofitService.getWeatherForCity("Sarajevo")
                _apiResponse.value = response
            } catch (t: Throwable) {
                Log.i("testtest1", "${t.message}")
            }
        }
    }
}

//TODO Optimizirati tranzicije kada konekcija izmedju klijenta i servera prelazi iz jednog stanja u drugo