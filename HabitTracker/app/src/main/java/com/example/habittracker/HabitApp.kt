package com.example.habittracker

import android.app.Application
import com.example.habittracker.data.Habit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HabitApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val repo = Graph.repository(this)
        // Seed a few starter habits on very first launch only.
        CoroutineScope(Dispatchers.IO).launch {
            if (repo.habitsOnce().isEmpty()) {
                listOf(
                    "Drink water" to "\uD83D\uDCA7",
                    "Read for 20 minutes" to "\uD83D\uDCDA",
                    "Morning workout" to "\uD83C\uDFC3",
                    "Meditate" to "\uD83E\uDDD8"
                ).forEach { (name, emoji) -> repo.addHabit(name, emoji) }
            }
        }
    }
}
