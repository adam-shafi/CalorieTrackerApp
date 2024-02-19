package com.example.calorietracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NutritionBudget (
    @PrimaryKey
    val id: Int = 1,
    val caloriesBudget: Float,
    val proteinBudget: Float,
    val carbsBudget: Float,
    val fatBudget: Float,
)
