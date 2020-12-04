package com.example.irrigationsystem.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SetupInfo(
    @PrimaryKey(autoGenerate = true) val ServerId : Int = 0,
    var IpAddress : String,
    var City : String
    )