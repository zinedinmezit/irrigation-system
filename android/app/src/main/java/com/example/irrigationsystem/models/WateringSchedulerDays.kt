package com.example.irrigationsystem.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

@Entity(primaryKeys = ["WateringSchedulerId", "DaysId"])
data class WateringSchedulerDays(
    val WateringSchedulerId: Int,
    val DaysId: Int
)

data class WateringSchedulerWithDays(
    @Embedded val WateringScheduler: WateringScheduler,
    @Relation(
        parentColumn = "WateringSchedulerId",
        entityColumn = "DaysId",
        associateBy = Junction(WateringSchedulerDays::class)
    )
    val songs: List<Days>
)