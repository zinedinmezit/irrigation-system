package com.example.irrigationsystem.models

import androidx.room.PrimaryKey

data class Days(
    @PrimaryKey(autoGenerate = true) val Id : Int,
    val OrdinalNumber : Int,
    val Name : String
)