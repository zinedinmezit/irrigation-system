package com.example.irrigationsystem.models

import androidx.room.Embedded
import androidx.room.Relation

data class PlanAndWatering(
    @Embedded val Plan : Plan,
    @Relation(
        parentColumn = "PlanId",
        entityColumn = "PlanId_FK"
    )
    val WateringScheduler : WateringScheduler
)