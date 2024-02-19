package com.example.calorietracker.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = DailyLog::class,
        parentColumns = arrayOf("date"),
        childColumns = arrayOf("logDate"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Meal(
    @PrimaryKey
    val mealId: Int,
    val name: String,
    val logDate: String
)