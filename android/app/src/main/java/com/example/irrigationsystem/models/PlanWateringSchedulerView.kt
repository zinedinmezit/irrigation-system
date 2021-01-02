package com.example.irrigationsystem.models

import androidx.room.DatabaseView

@DatabaseView("SELECT p.PlanId,p.Name,p.IsActive,ws.WateringSchedulerId,ws.WateringTimeNow,ws.WateringDuration,ws.TimeString " +
        "FROM `Plan` as p INNER JOIN WateringScheduler as ws ON ws.PlanId_FK=p.PlanId WHERE p.IsActive=1")
data class PlanWateringSchedulerView(
    val PlanId : Int,
    val Name : String?,
    val IsActive : Boolean,
    val WateringSchedulerId : Int,
    val WateringTimeNow : Long,
    val WateringDuration : Long,
    val TimeString : String
)
