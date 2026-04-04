package com.example.focusapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey val id: String,
    val label: String,
    val time: String,
    val remoteId: String? = null
)

