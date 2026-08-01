package com.example.habittracker.data

import androidx.room.Entity
import androidx.room.Index

// One row = one habit marked done on one date. "date" is an ISO string (yyyy-MM-dd).
@Entity(
    tableName = "completions",
    primaryKeys = ["habitId", "date"],
    indices = [Index("habitId")]
)
data class Completion(
    val habitId: Long,
    val date: String
)
