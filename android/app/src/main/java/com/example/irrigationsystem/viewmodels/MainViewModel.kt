package com.example.irrigationsystem.viewmodels

import androidx.lifecycle.ViewModel
import com.example.irrigationsystem.network.OkhttpWS

class MainViewModel : ViewModel() {

    val WsInstance : OkhttpWS = OkhttpWS()

}