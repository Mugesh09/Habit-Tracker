package com.example.habittracker.data

import android.content.Context
import androidx.glance.appwidget.updateAll
import com.example.habittracker.widget.HabitWidget
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class HabitRepository(
    private val dao: HabitDao,
    private val appContext: Context
) {
    val habits: Flow<List<Habit>> = dao.observeHabits()
    val completions: Flow<List<Completion>> = dao.observeCompletions()

    suspend fun addHabit(name: String, emoji: String) {
        dao.insertHabit(Habit(name = name.trim(), emoji = emoji))
        refreshWidget()
    }

    suspend fun deleteHabit(habit: Habit) {
        dao.deleteCompletionsFor(habit.id)
        dao.deleteHabit(habit.id)
        refreshWidget()
    }

    /** Flip a habit's completion for [date] (default today). */
    suspend fun toggle(habitId: Long, date: LocalDate = LocalDate.now()) {
        val key = date.toString() // ISO yyyy-MM-dd
        if (dao.isDone(habitId, key) > 0) {
            dao.deleteCompletion(habitId, key)
        } else {
            dao.insertCompletion(Completion(habitId, key))
        }
        refreshWidget()
    }

    suspend fun habitsOnce(): List<Habit> = dao.habitsOnce()

    suspend fun doneTodayIds(): Set<Long> =
        dao.completionsOn(LocalDate.now().toString()).map { it.habitId }.toSet()

    private suspend fun refreshWidget() {
        // Keep the home-screen widget in sync with any change made in the app.
        HabitWidget().updateAll(appContext)
    }
}
