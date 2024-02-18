package com.example.calorietracker.ui.daily_log

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.calorietracker.R
import com.example.calorietracker.ui.components.VerticalText
import com.example.calorietracker.ui.theme.CarbRed
import com.example.calorietracker.ui.theme.FatGreen
import com.example.calorietracker.ui.theme.ProteinBlue
import com.example.calorietracker.ui.theme.dimen_10dp
import com.example.calorietracker.ui.theme.dimen_16dp
import com.example.calorietracker.ui.theme.dimen_8dp

@Composable
fun MealCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = dimen_16dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(dimen_10dp),
            verticalArrangement = Arrangement.spacedBy(dimen_8dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Breakfast", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = "410 cal")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                VerticalText(title = "50 g", description = "Protein", color = ProteinBlue)
                VerticalText(title = "25 g", description = "Carbs", color = CarbRed)
                VerticalText(title = "16 g", description = "Fat", color = FatGreen)
            }
            MealItem(
                food = "Ramen",
                serving = "152 grams",
                calories = "160 cal",
                painter = painterResource(id = R.drawable.chinese_food)
            )
            MealItem(
                food = "Rice",
                serving = "72 grams",
                calories = "120 cal",
                painter = painterResource(id = R.drawable.hot_food)
            )
            MealItem(
                food = "Canned Tuna",
                serving = "1 can",
                calories = "130 cal",
                painter = painterResource(id = R.drawable.canned_fish_food)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = { /*TODO*/ }) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
                        Text("Add")
                    }
                }
            }

        }
    }
}


@Composable
fun MealItem(
    food: String,
    serving: String,
    calories: String,
    painter: Painter
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painter,
                tint = Color.Unspecified,
                contentDescription = "Chinese Food"
            )
            Spacer(modifier = Modifier.width(dimen_8dp))
            Column {
                Text(text = food)
                Text(text = serving)
            }
        }
        Text(text = calories)
    }
}