package com.example.calorietracker.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Meal::class,
        parentColumns = arrayOf("mealId"),
        childColumns = arrayOf("mealId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Food(
    @PrimaryKey
    val foodId: Int,

    val mealId: Int,
    val name: String,
    val calories: Float,
    val protein: Float,
    val carbs: Float,
    val fat: Float,
    val iconId: Int,
    val servingAmount: Float,
    val servingUnits: String
)