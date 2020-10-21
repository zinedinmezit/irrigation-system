package com.example.irrigationsystem.models

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class WateringSchedulerWithDays(
    @Embedded val WateringScheduler: WateringScheduler,
    @Relation(
        parentColumn = "WateringSchedulerId",
        entityColumn = "DayId",
        associateBy = Junction(WateringSchedulerDays::class)
    )
    val days: List<Day>
)