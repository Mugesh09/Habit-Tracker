package com.example.habittracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {

    @Query("SELECT * FROM habits ORDER BY position ASC, id ASC")
    fun observeHabits(): Flow<List<Habit>>

    @Query("SELECT * FROM completions")
    fun observeCompletions(): Flow<List<Completion>>

    @Query("SELECT * FROM habits ORDER BY position ASC, id ASC")
    suspend fun habitsOnce(): List<Habit>

    @Query("SELECT * FROM completions WHERE date = :date")
    suspend fun completionsOn(date: String): List<Completion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: Habit): Long

    @Query("DELETE FROM habits WHERE id = :habitId")
    suspend fun deleteHabit(habitId: Long)

    @Query("DELETE FROM completions WHERE habitId = :habitId")
    suspend fun deleteCompletionsFor(habitId: Long)

    @Query("SELECT COUNT(*) FROM completions WHERE habitId = :habitId AND date = :date")
    suspend fun isDone(habitId: Long, date: String): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCompletion(completion: Completion)

    @Query("DELETE FROM completions WHERE habitId = :habitId AND date = :date")
    suspend fun deleteCompletion(habitId: Long, date: String)
}
