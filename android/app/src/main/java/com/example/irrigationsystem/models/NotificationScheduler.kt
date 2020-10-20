package com.example.irrigationsystem.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NotificationScheduler(
    @PrimaryKey(autoGenerate = true) val Id : Int,
    var TimeToNotify : Long,
    val PlanId : Int
)