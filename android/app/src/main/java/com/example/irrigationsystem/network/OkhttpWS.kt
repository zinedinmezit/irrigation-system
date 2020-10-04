package com.example.irrigationsystem.network

import android.util.Log
import okhttp3.*
import okio.IOException

class OkhttpWS
{

    private var client : OkHttpClient = OkHttpClient()

    fun runWS()
    {
        val request = Request.Builder().url("https://publicobject.com/helloworld.txt").build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) Log.i("OkHttp","Doesnt work")

                    for ((name, value) in response.headers) {
                        println("$name: $value")
                    }

                    Log.i("OkHttp","Works")
                }
            }
        })
    }


        /*client?.newWebSocket(request,object : WebSocketListener(){
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.i("OkHttp", "onFailure called")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.i("OkHttp", "onClosed called")
            }

            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.i("OkHttp", "onOpen called")
                webSocket.send("Realllllllllyyyyyy")
                webSocket.close(1000,"Goooooodbyeeee")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.i("OkHttp", "onClosing called")
                webSocket.close(1000,null)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.i("OkHttp", "onMessage called")
            }
        })*/
}
