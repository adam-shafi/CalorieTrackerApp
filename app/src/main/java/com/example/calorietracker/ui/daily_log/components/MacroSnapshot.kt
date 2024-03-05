package com.example.calorietracker.ui.daily_log.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calorietracker.R
import com.example.calorietracker.ui.components.CircularProgressbar
import com.example.calorietracker.ui.components.LinearProgressBar
import com.example.calorietracker.ui.components.TitleText
import com.example.calorietracker.ui.components.VerticalText
import com.example.calorietracker.ui.daily_log.DailyLogUiState
import com.example.calorietracker.ui.daily_log.DerivedMacroInfoState
import com.example.calorietracker.ui.theme.CactusGreen
import com.example.calorietracker.ui.theme.CarbRed
import com.example.calorietracker.ui.theme.CarbRedGradient
import com.example.calorietracker.ui.theme.ChillRed
import com.example.calorietracker.ui.theme.DescriptionGray
import com.example.calorietracker.ui.theme.FatGreen
import com.example.calorietracker.ui.theme.FatGreenGradient
import com.example.calorietracker.ui.theme.ProteinBlue
import com.example.calorietracker.ui.theme.ProteinBlueGradient
import com.example.calorietracker.ui.theme.dimen_10dp
import com.example.calorietracker.ui.theme.dimen_16dp
import com.example.calorietracker.ui.theme.dimen_8dp
import kotlin.math.roundToInt

//@Composable
//fun MacroSnapshot(
//    uiState: DailyLogUiState,
//    calorieInfoState: DerivedMacroInfoState,
//    proteinInfoState: DerivedMacroInfoState,
//    carbInfoState: DerivedMacroInfoState,
//    fatInfoState: DerivedMacroInfoState
//) {
//    MacroSnapshot(
//        calorieBudget = uiState.totalCalories.budget.roundToInt(),
//        calorieEaten = uiState.totalCalories.foodEaten.roundToInt(),
//        calorieRemainder = calorieInfoState.remainder,
//        caloriePercent = calorieInfoState.percent,
//        isCalorieOver = calorieInfoState.isOver,
//        proteinEaten = uiState.totalProtein.foodEaten.roundToInt(),
//        proteinBudget = uiState.totalProtein.budget.roundToInt(),
//        proteinPercent = proteinInfoState.percent.roundToInt(),
//        carbsEaten = uiState.totalCarbs.foodEaten.roundToInt(),
//        carbsBudget = uiState.totalCarbs.budget.roundToInt(),
//        carbPercent = carbInfoState.percent.roundToInt(),
//        fatEaten = uiState.totalFat.foodEaten.roundToInt(),
//        fatBudget = uiState.totalFat.budget.roundToInt(),
//        fatPercent = fatInfoState.percent.roundToInt()
//    )
//}

//@Composable
//fun MacroSnapshot(
//    calorieBudget: Int,
//    calorieEaten: Int,
//    calorieRemainder: Float,
//    caloriePercent: Float,
//    isCalorieOver: Boolean,
//    proteinEaten: Int,
//    proteinBudget: Int,
//    proteinPercent: Int,
//    carbsEaten: Int,
//    carbsBudget: Int,
//    carbPercent: Int,
//    fatEaten: Int,
//    fatBudget: Int,
//    fatPercent: Int
//) {
//
//    Surface(
//        modifier = Modifier
//            .fillMaxWidth(),
//        color = MaterialTheme.colorScheme.secondaryContainer
//    ) {
//        Column(
//            modifier = Modifier.padding(dimen_10dp),
//            verticalArrangement = Arrangement.SpaceBetween
//        ) {
//            TitleText(text = "Calories")
//            CalorieInfo(
//                budget = calorieBudget,
//                remainder = calorieRemainder,
//                foodEaten = calorieEaten,
//                percent = caloriePercent,
//                isOver = isCalorieOver
//            )
//            Spacer(modifier = Modifier.height(4.dp))
//            TitleText(text = "Macros")
//            MacroInfo(
//                proteinBudget = proteinBudget,
//                proteinEaten = proteinEaten,
//                proteinPercent = proteinPercent,
//                carbsBudget = carbsBudget,
//                carbsEaten = carbsEaten,
//                carbPercent = carbPercent,
//                fatBudget = fatBudget,
//                fatEaten = fatEaten,
//                fatPercent = fatPercent
//            )
//        }
//
//
//    }
//}

@Composable
fun CalorieInfo(
    budget: Int,
    foodEaten: Int,
    remainder: Float,
    percent: Float,
    isOver: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        VerticalText(title = budget.toString(), description = stringResource(id = R.string.budget))
        CircularProgressbar(
            name = remainder,
            size = 140.dp,
            backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
            description = if (isOver) stringResource(id = R.string.over) else stringResource(
                id = R.string.remaining
            ),
            dataUsage = percent,
            titleTextStyle = TextStyle(
                fontSize = 24.sp,
                color = if (isOver) ChillRed else CactusGreen
            ),
            descriptionTextStyle = TextStyle(
                fontSize = 20.sp,
                color = DescriptionGray
            )
        )
        VerticalText(title = foodEaten.toString(), description = stringResource(id = R.string.food))
    }
}

//@Composable
//fun MacroInfo(
//    proteinEaten: Int,
//    proteinBudget: Int,
//    proteinPercent: Int,
//    carbsEaten: Int,
//    carbsBudget: Int,
//    carbPercent: Int,
//    fatEaten: Int,
//    fatBudget: Int,
//    fatPercent: Int
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        MacroElement(
//            title = stringResource(id = R.string.protein),
//            current = proteinEaten,
//            percent = proteinPercent,
//            total = proteinBudget,
//            color = ProteinBlue,
//            gradientColor = ProteinBlueGradient
//        )
//        MacroElement(
//            title = stringResource(id = R.string.carbs),
//            current = carbsEaten,
//            percent = carbPercent,
//            total = carbsBudget,
//            color = CarbRed,
//            gradientColor = CarbRedGradient
//        )
//        MacroElement(
//            title = stringResource(id = R.string.fat),
//            current = fatEaten,
//            percent = fatPercent,
//            total = fatBudget,
//            color = FatGreen,
//            gradientColor = FatGreenGradient
//        )
//    }
//
//
//}


//@Composable
//fun RowScope.MacroElement(
//    title: String,
//    current: Int,
//    total: Int,
//    percent: Int,
//    color: Color,
//    gradientColor: Color
//) {
//    val modifier = Modifier
//        .weight(1f)
//    Column(
//        modifier = modifier,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(
//            modifier = Modifier.padding(bottom = dimen_16dp),
//            text = title,
//            fontSize = 20.sp,
//            color = color
//        )
//        LinearProgressBar(
//            modifier = Modifier.padding(horizontal = dimen_8dp),
//            gradientColors = listOf(color, gradientColor),
//            indicatorNumber = percent
//        )
//        Text(text = "$current / $total g", fontSize = 14.sp)
//    }
//}