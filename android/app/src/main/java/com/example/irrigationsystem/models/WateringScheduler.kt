package com.example.irrigationsystem.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WateringScheduler(
    @PrimaryKey(autoGenerate = true) val Id : Int,
    var WateringTimeNow : Long,
    var WateringTimeNext : Long,
    val PlanId : Int
    )