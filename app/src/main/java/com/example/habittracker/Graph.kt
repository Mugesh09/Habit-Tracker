package com.example.habittracker

import android.content.Context
import com.example.habittracker.data.AppDatabase
import com.example.habittracker.data.HabitRepository

object Graph {
    @Volatile private var repo: HabitRepository? = null

    fun repository(context: Context): HabitRepository =
        repo ?: synchronized(this) {
            repo ?: run {
                val db = AppDatabase.get(context.applicationContext)
                HabitRepository(db.habitDao(), context.applicationContext).also { repo = it }
            }
        }
}
