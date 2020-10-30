package com.example.irrigationsystem.models

import androidx.room.DatabaseView

@DatabaseView("SELECT d.OrdinalNumber,d.Name " +
        "FROM WateringScheduler as ws INNER JOIN WateringSchedulerDays as wsd ON wsd.WateringSchedulerId=ws.WateringSchedulerId " +
        "INNER JOIN Day as d ON d.DayId=wsd.DayId INNER JOIN `Plan` as p ON p.PlanId=ws.PlanId_FK WHERE p.IsActive=1")
data class ScheduledDaysView(
    val OrdinalNumber : Int?,
    val Name : String?
)