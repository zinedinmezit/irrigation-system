package com.example.irrigationsystem.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Plan(
    @PrimaryKey(autoGenerate = true) val PlanId : Int = 0,
    var Name: String,
    var IsActive : Boolean
)