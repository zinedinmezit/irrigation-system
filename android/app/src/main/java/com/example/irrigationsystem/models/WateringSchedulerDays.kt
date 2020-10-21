package com.example.irrigationsystem.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

@Entity(primaryKeys = ["WateringSchedulerId", "DayId"])
data class WateringSchedulerDays(
    val WateringSchedulerId: Int,
    val DayId: Int
)

