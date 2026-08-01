package com.example.habittracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.habittracker.ui.AddHabitSheet
import com.example.habittracker.ui.HabitViewModel
import com.example.habittracker.ui.HistoryScreen
import com.example.habittracker.ui.TodayScreen
import com.example.habittracker.ui.theme.HabitTrackerTheme
import com.example.habittracker.ui.theme.Ink
import com.example.habittracker.ui.theme.InkFaint

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitTrackerTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppRoot()
                }
            }
        }
    }
}

private enum class Tab { Today, History }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppRoot(vm: HabitViewModel = viewModel()) {
    var tab by remember { mutableStateOf(Tab.Today) }
    var showAdd by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            NavigationBar(containerColor = Color.White, tonalElevation = 0.dp) {
                NavigationBarItem(
                    selected = tab == Tab.Today,
                    onClick = { tab = Tab.Today },
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Today", modifier = Modifier.size(22.dp)) },
                    label = { Text("Today", fontSize = 11.sp) },
                    colors = navColors()
                )
                // Center add button
                NavigationBarItem(
                    selected = false,
                    onClick = { showAdd = true },
                    icon = {
                        Box(
                            Modifier.size(40.dp).clip(RoundedCornerShape(12.dp)).background(Ink),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "New habit", tint = Color.White, modifier = Modifier.size(22.dp))
                        }
                    },
                    colors = navColors()
                )
                NavigationBarItem(
                    selected = tab == Tab.History,
                    onClick = { tab = Tab.History },
                    icon = { Icon(Icons.Filled.DateRange, contentDescription = "History", modifier = Modifier.size(22.dp)) },
                    label = { Text("History", fontSize = 11.sp) },
                    colors = navColors()
                )
            }
        }
    ) { inner ->
        Box(Modifier.fillMaxSize().padding(inner)) {
            when (tab) {
                Tab.Today -> TodayScreen(vm, onAdd = { showAdd = true })
                Tab.History -> HistoryScreen(vm)
            }
        }
    }

    if (showAdd) {
        ModalBottomSheet(
            onDismissRequest = { showAdd = false },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            AddHabitSheet(
                onSave = { name, emoji ->
                    vm.addHabit(name, emoji)
                    showAdd = false
                },
                onCancel = { showAdd = false }
            )
        }
    }
}

@Composable
private fun navColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = Ink,
    selectedTextColor = Ink,
    unselectedIconColor = InkFaint,
    unselectedTextColor = InkFaint,
    indicatorColor = Color.Transparent
)
