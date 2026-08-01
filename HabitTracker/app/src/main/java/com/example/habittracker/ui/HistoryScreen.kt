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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.habittracker.ui.theme.BorderCol
import com.example.habittracker.ui.theme.BorderStrong
import com.example.habittracker.ui.theme.HoverStrong
import com.example.habittracker.ui.theme.Ink
import com.example.habittracker.ui.theme.InkFaint
import com.example.habittracker.ui.theme.InkLight
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HistoryScreen(vm: HabitViewModel) {
    val habits by vm.habits.collectAsState()
    val completions by vm.completions.collectAsState()

    if (habits.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Add a habit to start tracking your history.", color = InkFaint, fontSize = 14.sp)
        }
        return
    }

    var selectedId by remember { mutableStateOf(habits.first().id) }
    if (habits.none { it.id == selectedId }) selectedId = habits.first().id
    var month by remember { mutableStateOf(YearMonth.now()) }

    val selected = habits.first { it.id == selectedId }
    val done = completions[selectedId] ?: emptySet()

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(14.dp))
        Text(
            "INSIGHTS",
            fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = InkFaint,
            letterSpacing = 0.5.sp, modifier = Modifier.padding(start = 22.dp)
        )
        Text(
            "History",
            fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Ink,
            modifier = Modifier.padding(start = 22.dp)
        )
        Spacer(Modifier.height(18.dp))

        // ---- habit switcher chips (horizontal scroll) ----
        LazyRow(
            contentPadding = PaddingValues(horizontal = 22.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(habits, key = { it.id }) { h ->
                val sel = h.id == selectedId
                Row(
                    Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .then(if (sel) Modifier.background(Ink) else Modifier.border(1.dp, BorderCol, RoundedCornerShape(999.dp)))
                        .clickable { selectedId = h.id }
                        .padding(start = 9.dp, end = 13.dp, top = 7.dp, bottom = 7.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(h.emoji, fontSize = 15.sp)
                    Spacer(Modifier.width(7.dp))
                    Text(
                        h.name,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (sel) Color.White else InkLight,
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(Modifier.height(18.dp))

        // ---- calendar card ----
        Column(
            Modifier
                .padding(horizontal = 22.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, BorderCol, RoundedCornerShape(12.dp))
                .padding(18.dp)
        ) {
            // nav
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    month.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault())),
                    fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Ink
                )
                Row {
                    ArrowBtn(Icons.Filled.KeyboardArrowLeft) { month = month.minusMonths(1) }
                    val canNext = month.isBefore(YearMonth.now())
                    ArrowBtn(Icons.Filled.KeyboardArrowRight, enabled = canNext) {
                        if (canNext) month = month.plusMonths(1)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // weekday header
            Row(Modifier.fillMaxWidth()) {
                listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa").forEach {
                    Text(
                        it, modifier = Modifier.weight(1f), textAlign = TextAlign.Center,
                        fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = InkFaint
                    )
                }
            }
            Spacer(Modifier.height(6.dp))

            // grid
            val today = LocalDate.now()
            val leading = month.atDay(1).dayOfWeek.value % 7 // Sunday = 0
            val days = month.lengthOfMonth()
            val cells = leading + days
            val rows = (cells + 6) / 7
            val monthDone = (1..days).count { month.atDay(it) in done }

            for (r in 0 until rows) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (c in 0 until 7) {
                        val index = r * 7 + c
                        val dayNum = index - leading + 1
                        if (dayNum in 1..days) {
                            val date = month.atDay(dayNum)
                            val isDone = date in done
                            DayCell(
                                day = dayNum,
                                done = isDone,
                                isToday = date == today,
                                future = date.isAfter(today),
                                modifier = Modifier.weight(1f),
                                onClick = { if (!date.isAfter(today)) vm.toggle(selectedId, date) }
                            )
                        } else {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                }
                Spacer(Modifier.height(4.dp))
            }

            // summary
            Spacer(Modifier.height(10.dp))
            Box(Modifier.fillMaxWidth().height(1.dp).background(BorderCol))
            Spacer(Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Stat(currentStreak(done, today).toString(), "day streak \uD83D\uDD25")
                Spacer(Modifier.width(18.dp))
                Stat(monthDone.toString(), "done in ${month.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())}")
                Spacer(Modifier.weight(1f))
                Text(
                    "Delete",
                    fontSize = 12.5.sp,
                    color = InkFaint,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { vm.deleteHabit(selected) }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(Modifier.height(40.dp))
    }
}

@Composable
private fun ArrowBtn(icon: androidx.compose.ui.graphics.vector.ImageVector, enabled: Boolean = true, onClick: () -> Unit) {
    Box(
        Modifier
            .size(30.dp)
            .clip(RoundedCornerShape(7.dp))
            .clickable(enabled = enabled) { onClick() }
            .alpha(if (enabled) 1f else 0.25f),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = InkLight, modifier = Modifier.size(18.dp))
    }
}

@Composable
private fun DayCell(
    day: Int,
    done: Boolean,
    isToday: Boolean,
    future: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val base = modifier
        .aspectRatio(1f)
        .clip(RoundedCornerShape(8.dp))
        .clickable(enabled = !future) { onClick() }
    val boxMod = when {
        done -> base.background(Ink)
        isToday -> base.border(1.5.dp, BorderStrong, RoundedCornerShape(8.dp))
        else -> base
    }
    Box(boxMod, contentAlignment = Alignment.Center) {
        Text(
            day.toString(),
            fontSize = 13.sp,
            fontWeight = if (done) FontWeight.SemiBold else FontWeight.Normal,
            color = when {
                done -> Color.White
                future -> InkFaint
                else -> InkLight
            },
            modifier = if (future) Modifier.alpha(0.6f) else Modifier
        )
    }
}

@Composable
private fun Stat(number: String, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(number, fontSize = 17.sp, fontWeight = FontWeight.Bold, color = Ink)
        Spacer(Modifier.width(6.dp))
        Text(label, fontSize = 12.5.sp, color = InkLight)
    }
}
