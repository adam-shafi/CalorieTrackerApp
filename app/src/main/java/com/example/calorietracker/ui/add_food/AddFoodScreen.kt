package com.example.calorietracker.ui.add_food

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.calorietracker.R
import com.example.calorietracker.ui.theme.dimen_8dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFoodScreen(
    viewModel: AddFoodViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchText by rememberSaveable { mutableStateOf("") }
    var isMealSelectOpen by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { onBackClick() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Navigate Back"
                        )
                    }
                },
                title = {
                    TextButton(
                        onClick = { isMealSelectOpen = true }
                    ) {
                        Text(
                            uiState.selectedMealName,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 20.sp
                        )
                        Spacer(Modifier.width(dimen_8dp))
                        Icon(
                            imageVector = if (isMealSelectOpen) ImageVector.vectorResource(id = R.drawable.arrow_drop_up) else Icons.Filled.ArrowDropDown,
                            contentDescription = "Dropdown",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    DropdownMenu(
                        expanded = isMealSelectOpen,
                        onDismissRequest = { isMealSelectOpen = false }) {
                        uiState.mealNames.forEach { mealName ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        mealName,
                                        fontSize = 16.sp
                                    )
                                },
                                onClick = {
                                    viewModel.updateSelectedMealName(newMealName = mealName)
                                    isMealSelectOpen = false
                                }
                            )
                        }
                    }


                },
                actions = {

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.barcode_scanner),
                    contentDescription = "Barcode Scanner"
                )
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.padding(paddingValues)
        ) {
            Column {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimen_8dp),
                    value = searchText,
                    onValueChange = {
                        searchText = it
                    },
                    label = {
                        Text(text = "Search for food")
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "Search Icon")
                    },
                    trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            IconButton(onClick = { searchText = "" }) {
                                Icon(imageVector = ImageVector.vectorResource(R.drawable.cancel), contentDescription = "Clear")
                            }
                        }
                    },
                    shape = RoundedCornerShape(50)
                )
            }
        }
    }
}