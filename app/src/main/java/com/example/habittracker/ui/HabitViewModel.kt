package com.example.habittracker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.habittracker.Graph
import com.example.habittracker.data.Habit
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class HabitViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = Graph.repository(app)

    val habits: StateFlow<List<Habit>> =
        repo.habits.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    /** habitId -> set of dates completed. */
    val completions: StateFlow<Map<Long, Set<LocalDate>>> =
        repo.completions
            .map { list ->
                list.groupBy { it.habitId }
                    .mapValues { (_, rows) -> rows.mapNotNull { runCatching { LocalDate.parse(it.date) }.getOrNull() }.toSet() }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyMap())

    fun toggle(habitId: Long, date: LocalDate = LocalDate.now()) =
        viewModelScope.launch { repo.toggle(habitId, date) }

    fun addHabit(name: String, emoji: String) =
        viewModelScope.launch { repo.addHabit(name, emoji) }

    fun deleteHabit(habit: Habit) =
        viewModelScope.launch { repo.deleteHabit(habit) }
}

/** Consecutive-day streak ending today (or yesterday if today isn't done yet). */
fun currentStreak(done: Set<LocalDate>, today: LocalDate = LocalDate.now()): Int {
    var day = if (today in done) today else today.minusDays(1)
    var n = 0
    while (day in done) {
        n++
        day = day.minusDays(1)
    }
    return n
}
