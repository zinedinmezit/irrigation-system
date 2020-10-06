package com.example.irrigationsystem.network

import android.util.Log
import okhttp3.*

class OkhttpWS
{

    private var client : OkHttpClient = OkHttpClient()

    fun runWS() {
        val request : Request = Request.Builder().url("ws://192.168.0.103:81").build()

        client.newWebSocket(request, object : WebSocketListener(){

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

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.i("OkHttp", "onFailure called")
            }
        })
    }
}
