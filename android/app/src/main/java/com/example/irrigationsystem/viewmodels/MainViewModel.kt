package com.example.irrigationsystem.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import okhttp3.*
import okio.ByteString

class MainViewModel : ViewModel() {

    var signalCode : Int = 0
    var client : OkHttpClient = OkHttpClient()

    private val request : Request = Request.Builder().url("ws://192.168.0.102:81").build()
    private val wsListener : WebSocketListener = object :WebSocketListener(){

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.i("OkHttp", "onClosed called")
        }

        override fun onOpen(webSocket: WebSocket, response: Response) {
            Log.i("OkHttp", "onOpen called")
            if(signalCode == 1){
                webSocket.send("ON")
                webSocket.close(1000,null)
            }
            else{
                webSocket.send("Testing closing connection")
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            Log.i("OkHttp", "onClosing called")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val output = text.split(".")
            val tankLevel = output[0]
            val humidityLevel = output[1]
            Log.i("OkHttp","$tankLevel - $humidityLevel")
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