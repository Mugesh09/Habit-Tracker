package com.example.habittracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habittracker.data.Habit
import com.example.habittracker.ui.theme.BorderStrong
import com.example.habittracker.ui.theme.HoverStrong
import com.example.habittracker.ui.theme.Ink
import com.example.habittracker.ui.theme.InkFaint
import com.example.habittracker.ui.theme.InkLight
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TodayScreen(vm: HabitViewModel, onAdd: () -> Unit) {
    val habits by vm.habits.collectAsState()
    val completions by vm.completions.collectAsState()
    val today = LocalDate.now()

    val doneCount = habits.count { today in (completions[it.id] ?: emptySet()) }
    val total = habits.size
    val pct = if (total == 0) 0f else doneCount.toFloat() / total

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(start = 22.dp, end = 22.dp, top = 14.dp, bottom = 40.dp)
    ) {
        item {
            Text(
                text = today.format(DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.getDefault())).uppercase(),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = InkFaint,
                letterSpacing = 0.5.sp
            )
            Text(
                text = greeting(),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Ink
            )
            if (total > 0) {
                Spacer(Modifier.height(18.dp))
                ProgressRow(doneCount, total, pct)
            }
            Spacer(Modifier.height(10.dp))
        }

        items(habits, key = { it.id }) { habit ->
            val done = today in (completions[habit.id] ?: emptySet())
            val streak = currentStreak(completions[habit.id] ?: emptySet(), today)
            HabitRow(habit, done, streak) { vm.toggle(habit.id) }
        }

        item {
            NewHabitRow(onAdd)
        }
    }
}

@Composable
private fun ProgressRow(done: Int, total: Int, pct: Float) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            buildString { append("$done of $total done") },
            fontSize = 14.sp, color = InkLight
        )
        Text("${(pct * 100).toInt()}%", fontSize = 13.sp, color = InkFaint)
    }
    Spacer(Modifier.height(8.dp))
    Box(
        Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(6.dp)).background(HoverStrong)
    ) {
        Box(Modifier.fillMaxWidth(pct).height(6.dp).clip(RoundedCornerShape(6.dp)).background(Ink))
    }
}

@Composable
private fun HabitRow(habit: Habit, done: Boolean, streak: Int, onToggle: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .clickable { onToggle() }
            .padding(horizontal = 10.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(30.dp).clip(RoundedCornerShape(6.dp)).background(HoverStrong),
            contentAlignment = Alignment.Center
        ) { Text(habit.emoji, fontSize = 18.sp) }

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Text(
                habit.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = if (done) InkFaint else Ink,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (streak > 0) {
                Text(
                    "\uD83D\uDD25 $streak day${if (streak > 1) "s" else ""}",
                    fontSize = 12.5.sp,
                    color = InkFaint
                )
            }
        }

        CheckBox(done)
    }
}

@Composable
private fun CheckBox(done: Boolean) {
    Box(
        Modifier
            .size(22.dp)
            .clip(RoundedCornerShape(5.dp))
            .then(
                if (done) Modifier.background(Ink)
                else Modifier.border(1.5.dp, BorderStrong, RoundedCornerShape(5.dp))
            ),
        contentAlignment = Alignment.Center
    ) {
        if (done) Icon(Icons.Filled.Check, contentDescription = null, tint = androidx.compose.ui.graphics.Color.White, modifier = Modifier.size(14.dp))
    }
}

@Composable
private fun NewHabitRow(onAdd: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .clickable { onAdd() }
            .padding(horizontal = 10.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(30.dp), contentAlignment = Alignment.Center) {
            Text("+", fontSize = 20.sp, color = InkFaint)
        }
        Spacer(Modifier.width(12.dp))
        Text("New habit", fontSize = 15.sp, color = InkFaint)
    }
}

private fun greeting(): String {
    val h = java.time.LocalTime.now().hour
    return when {
        h < 12 -> "Good morning"
        h < 18 -> "Good afternoon"
        else -> "Good evening"
    }
}
