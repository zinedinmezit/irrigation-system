package com.example.irrigationsystem.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.*
import java.util.*

class MainViewModel : ViewModel() {

    private var _isConnectionEstablished : MutableLiveData<Boolean> = MutableLiveData<Boolean>()
            val isConnectionEstablished : LiveData<Boolean>
                get() = _isConnectionEstablished

    private val _isConnectionEstablishedString = MutableLiveData<String>()
    val isConnectionEstablishedString: LiveData<String>
        get() = _isConnectionEstablishedString

   private val _hummidityPercentageValue = MutableLiveData<String>()
    val hummidityPercentageValue: LiveData<String>
        get() = _hummidityPercentageValue

    init {
        _isConnectionEstablished.value = false
        _isConnectionEstablishedString.value = "DISCONNECTED"
    }

    var signalCode : Int = 0
    var client : OkHttpClient = OkHttpClient()

    var currentEvent : String = "Initial"

    private val request : Request = Request.Builder().url("ws://192.168.0.102:81").build()
    private val wsListener : WebSocketListener = object :WebSocketListener(){

        override fun onOpen(webSocket: WebSocket, response: Response) {
            when(signalCode){
                0 -> {
                    _isConnectionEstablishedString.postValue("CONNECTED")
                    _isConnectionEstablished.postValue(true)
                }
                1 -> {
                    currentEvent = "Command"
                    webSocket.send("ON")
                    webSocket.close(1000,null)
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

    fun runWebSocket()
    {
        client.newWebSocket(request, wsListener)
    }
}

//TODO Extract stringove u enumeracij (npr.DISCONNECTED i CONNECTED)
//TODO Optimizirati tranzicije kada konekcija izmedju klijenta i servera prelazi iz jednog stanja u drugo