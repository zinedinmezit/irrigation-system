package com.example.irrigationsystem.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Day(
    @PrimaryKey(autoGenerate = true) val DayId : Int,
    val OrdinalNumber : Int,
    val Name : String
)