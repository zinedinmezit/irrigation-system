package com.example.irrigationsystem.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import okhttp3.*

class MainViewModel : ViewModel() {

   var client : OkHttpClient = OkHttpClient()

    fun runWebSocket(){

        val request : Request = Request.Builder().url("ws://192.168.0.102:81").build()

        client.newWebSocket(request, object : WebSocketListener(){

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.i("OkHttp", "onClosed called")
            }

            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.i("OkHttp", "onOpen called")
                webSocket.send("ON")
                webSocket.close(1000,null)
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.i("OkHttp", "onClosing called")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.i("OkHttp", text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.i("OkHttp", "onFailure called")
            }

        })
    }
}