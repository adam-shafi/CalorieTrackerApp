package com.example.calorietracker.ui.daily_log.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import com.example.calorietracker.R
import com.example.calorietracker.ui.daily_log.Swipe
import com.example.calorietracker.ui.theme.dimen_8dp
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerBar(
    date: String,
    updateDate: (Long) -> Unit,
    updateDateBySwipe: (Swipe) -> Unit
) {

    var isModalOpen by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { updateDateBySwipe(Swipe.Left) }) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "Navigate Back"
                )
            }
            TextButton(onClick = {
                isModalOpen = true
            }) {
                Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Date")
                Spacer(Modifier.width(dimen_8dp))
                Text(text = date, fontSize = 16.sp)
            }
            IconButton(onClick = { updateDateBySwipe(Swipe.Right) }) {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = "Navigate forward"
                )
            }
        }
    }
    if (isModalOpen) {
        val datePickerState =
            rememberDatePickerState()
        val confirmEnabled by remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }
        DatePickerDialog(
            onDismissRequest = {
                isModalOpen = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        isModalOpen = false
                        datePickerState.selectedDateMillis?.let { millis ->
                            updateDate(millis)
                        }

                        val instant = Instant.ofEpochMilli(datePickerState.selectedDateMillis ?: 0L)
                        val zonedDateTime = ZonedDateTime.ofInstant(instant, ZoneId.of("UTC"))
                         Log.d("Selected date timestamp 1", ": ${datePickerState.selectedDateMillis}" )
                        Log.d("Selected date timestamp 2", ": ${zonedDateTime.dayOfMonth}-${zonedDateTime.month.value}-${zonedDateTime.year}" )
                    },
                    enabled = confirmEnabled
                ) {
                    Text(stringResource(id = R.string.ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isModalOpen = false
                    }
                ) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        )
        {
            DatePicker(state = datePickerState)
        }
    }
}