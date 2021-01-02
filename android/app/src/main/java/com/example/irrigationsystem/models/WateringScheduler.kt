package com.example.irrigationsystem.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WateringScheduler(
    @PrimaryKey(autoGenerate = true) val WateringSchedulerId : Int = 0,
    var WateringTimeNow : Long,
    var TimeString : String,
    var WateringDuration : Long,
    val PlanId_FK : Int
    )