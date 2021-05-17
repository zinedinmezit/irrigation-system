package com.example.irrigationsystem.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocketListener
import java.lang.IllegalArgumentException

object OkHttpProvider{

    private val instance : OkHttpClient = OkHttpClient()

     fun openWebSocketConnection(wsListener : WebSocketListener, ipAddress : String) : String{
         return try {
             val request: Request = Request.Builder().url("ws://$ipAddress:81").build()
             instance.newWebSocket(request, wsListener)

             "Connection successful"
         } catch(ex : IllegalArgumentException){

             "Failed to connect"
         }
    }

      fun closeConnections(){
        instance.dispatcher.cancelAll()
    }
}