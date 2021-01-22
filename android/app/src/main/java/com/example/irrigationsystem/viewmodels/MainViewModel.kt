package com.example.irrigationsystem.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.irrigationsystem.database.IrrigationSystemDatabase
import com.example.irrigationsystem.models.Plan
import com.example.irrigationsystem.models.PlanWateringSchedulerView
import com.example.irrigationsystem.models.ScheduledDaysView
import com.example.irrigationsystem.models.SetupInfo
import com.example.irrigationsystem.models.weatherapi.WeatherObject
import com.example.irrigationsystem.network.MoshiProvider
import com.example.irrigationsystem.network.OkHttpProvider
import com.example.irrigationsystem.network.WeatherApi
import com.example.irrigationsystem.repositories.IrrigationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import okhttp3.*

class MainViewModel(application: Application) : AndroidViewModel(application) {



    private val repository : IrrigationRepository
    val activePlan : LiveData<PlanWateringSchedulerView>
    val scheduledDays : LiveData<List<ScheduledDaysView>>
    val allPlans : LiveData<List<Plan>>
    val setupInfo : LiveData<SetupInfo>

    private val scope = CoroutineScope(context = Dispatchers.IO)


    private var _isConnectionEstablished : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
            val isConnectionEstablished : LiveData<Boolean>
                get() = _isConnectionEstablished

    private val _hummidityPercentageValue = MutableLiveData<String>()
            val hummidityPercentageValue: LiveData<String>
              get() = _hummidityPercentageValue

    private val _dht11HummidityPercentageValue = MutableLiveData<String>()
    val dht11HummidityPercentageValue: LiveData<String>
        get() = _dht11HummidityPercentageValue

    private val _dht11TemperatureValue = MutableLiveData<String>()
    val dht11TemperatureValue: LiveData<String>
        get() = _dht11TemperatureValue

    private val _apiResponse : MutableLiveData<WeatherObject> = MutableLiveData()
            val apiResponse : LiveData<WeatherObject>
              get() = _apiResponse

    var responseFlag = true


    init {
        val dao = IrrigationSystemDatabase.getInstance(application).IrrigationDatabaseDao
        _isConnectionEstablished.value = false
        repository = IrrigationRepository(dao)

        activePlan = repository.activePlan
        scheduledDays = repository.schedulerDays
        allPlans = repository.allPlans
        setupInfo = repository.setupInfo
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
                        webSocket.send("${activePlan.value?.WateringDuration}")
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

            val sensorValuesObject = MoshiProvider.jsonAdapter.fromJson(text)

            Log.i("ttt", "$sensorValuesObject")

            _hummidityPercentageValue.postValue(sensorValuesObject?.moistureValue)
            _dht11HummidityPercentageValue.postValue(sensorValuesObject?.dhtHummValue)
            _dht11TemperatureValue.postValue(sensorValuesObject?.dhtTempValue)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            if(_isConnectionEstablished.value!!) {
                _isConnectionEstablished.postValue(false)
            }
        }
    }

    fun openWebSocketConnection(ipAddress : String){
        scope.launch {
            OkHttpProvider.openWebSocketConnection(wsListener, ipAddress)
        }
    }

    fun closeWebSocketConnection(){
        scope.launch {
            OkHttpProvider.closeConnections()
        }
    }

     fun getApiResponse(city : String) {
        scope.launch {
            try {
                if(responseFlag) {
                    val response = WeatherApi.retrofitService.getWeatherForCity(city)
                    responseFlag = false
                    _apiResponse.postValue(response)
                }
            } catch (t: Throwable) {
                Log.i("testtest1", "${t.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}

//TODO Optimizirati tranzicije kada konekcija izmedju klijenta i servera prelazi iz jednog stanja u drugo
//TODO Inject-ad Moshi builder