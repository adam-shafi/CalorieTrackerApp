package com.example.calorietracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DailyLog (
    @PrimaryKey
    val date: Long,
)

