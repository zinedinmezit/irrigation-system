package com.example.irrigationsystem.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import okhttp3.*

class MainViewModel : ViewModel() {

   private val _hummidityPercentageValue = MutableLiveData<String>()
    val hummidityPercentageValue: LiveData<String>
        get() = _hummidityPercentageValue

    var signalCode : Int = 0
    var client : OkHttpClient = OkHttpClient()

    private val request : Request = Request.Builder().url("ws://192.168.0.102:81").build()
    private val wsListener : WebSocketListener = object :WebSocketListener(){

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.i("OkHttp", "onClosed called")
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            when(signalCode){
                0 -> {webSocket.send("Open CONN without CLOSING")}
                1 -> {
                    webSocket.send("ON")
                    webSocket.close(1000,null)
                }
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.i("OkHttp", "onClosing called")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            _hummidityPercentageValue.postValue(text)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.i("OkHttp", "onFailure called")
        }
    }

    fun runWebSocket()
    {
        client.newWebSocket(request, wsListener)
    }
}