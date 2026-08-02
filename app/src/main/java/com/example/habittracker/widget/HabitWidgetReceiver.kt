package com.example.habittracker.widget

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionParametersOf
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.habittracker.Graph
import com.example.habittracker.data.Habit

private val Ink = Color(0xFF37352F)
private val InkFaint = Color(0x7337352F)
private val Track = Color(0x1F37352F)

val habitIdKey = ActionParameters.Key<Long>("habitId")

class HabitWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val repo = Graph.repository(context)
        val habits = repo.habitsOnce()
        val doneIds = repo.doneTodayIds()
        provideContent {
            WidgetContent(habits, doneIds)
        }
    }
}

@androidx.compose.runtime.Composable
private fun WidgetContent(habits: List<Habit>, doneIds: Set<Long>) {
    val doneCount = habits.count { it.id in doneIds }
    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(Color.White)
            .cornerRadius(22.dp)
            .padding(16.dp)
    ) {
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Today",
                style = TextStyle(color = ColorProvider(Ink), fontSize = 13.sp, fontWeight = FontWeight.Bold)
            )
            Spacer(GlanceModifier.width(8.dp))
            Text(
                "$doneCount/${habits.size}",
                style = TextStyle(color = ColorProvider(InkFaint), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            )
        }
        Spacer(GlanceModifier.height(10.dp))

        habits.take(6).forEach { habit ->
            val done = habit.id in doneIds
            Row(
                modifier = GlanceModifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clickable(actionRunCallback<ToggleHabitAction>(actionParametersOf(habitIdKey to habit.id))),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = GlanceModifier
                        .size(18.dp)
                        .cornerRadius(5.dp)
                        .background(if (done) Ink else Track),
                    contentAlignment = Alignment.Center
                ) {
                    if (done) {
                        Text(
                            "\u2713",
                            style = TextStyle(color = ColorProvider(Color.White), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                }
                Spacer(GlanceModifier.width(10.dp))
                Text(
                    "${habit.emoji}  ${habit.name}",
                    maxLines = 1,
                    style = TextStyle(
                        color = ColorProvider(if (done) InkFaint else Ink),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

class ToggleHabitAction : ActionCallback {
    override suspend fun onAction(context: Context, glanceId: GlanceId, parameters: ActionParameters) {
        val id = parameters[habitIdKey] ?: return
        // toggle() updates the DB and refreshes all widget instances
        Graph.repository(context).toggle(id)
    }
}
