package com.example.irrigationsystem.models

import androidx.room.Embedded
import androidx.room.Relation

data class PlanAndNotifying(
    @Embedded val Plan : Plan,
    @Relation(
        parentColumn = "PlanId",
        entityColumn = "PlanId_FK"
    )
    val NotificationScheduler : NotificationScheduler
)