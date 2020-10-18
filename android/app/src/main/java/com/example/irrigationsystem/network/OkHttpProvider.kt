package com.example.irrigationsystem.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocketListener

object OkHttpProvider{

    private val instance : OkHttpClient = OkHttpClient()
    private val request : Request = Request.Builder().url("ws://192.168.0.102:81").build()

    fun openWebSocketConnection(wsListener : WebSocketListener){
        instance.newWebSocket(request, wsListener)
    }

    fun closeConnections(){
        instance.dispatcher.cancelAll()
    }
}