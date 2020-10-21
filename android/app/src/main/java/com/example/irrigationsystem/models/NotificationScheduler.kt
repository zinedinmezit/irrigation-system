package com.example.irrigationsystem.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NotificationScheduler(
    @PrimaryKey(autoGenerate = true) val NotificationSchedulerId : Int,
    var TimeToNotify : Long,
    val PlanId_FK : Int
)