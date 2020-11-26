package com.example.irrigationsystem.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocketListener

object OkHttpProvider{

    private val instance : OkHttpClient = OkHttpClient()

     fun openWebSocketConnection(wsListener : WebSocketListener, ipAddress : String){
             val request : Request = Request.Builder().url("ws://$ipAddress:81").build()
             instance.newWebSocket(request, wsListener)
    }

      fun closeConnections(){
        instance.dispatcher.cancelAll()
    }
}