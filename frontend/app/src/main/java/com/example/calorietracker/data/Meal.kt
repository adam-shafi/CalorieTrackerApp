package com.example.calorietracker.data

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = [
      "name", "logDate"
    ],
    foreignKeys = [ForeignKey(
        entity = DailyLog::class,
        parentColumns = arrayOf("date"),
        childColumns = arrayOf("logDate"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Meal(
    val name: String,
    val logDate: String
)