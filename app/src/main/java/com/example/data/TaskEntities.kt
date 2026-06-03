package com.example.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    indices = [Index(value = ["date"])]
)
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val category: String, // "CASA" or "ESTUDOS"
    val difficulty: String, // "DOCE_LEVE" (Fácil), "MARSHMALLOW" (Médio), "CHOCOLATE_AMARGO" (Difícil)
    val date: String, // "YYYY-MM-DD" style
    val progressPercentage: Int = 0, // Calculated percentage 0-100
    val xpReward: Int = 50,
    val isCompleted: Boolean = false
)

@Entity(
    tableName = "subtasks",
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["taskId"])]
)
data class SubTask(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val taskId: Long,
    val title: String,
    val isCompleted: Boolean = false
)

@Entity(tableName = "custom_presets")
data class CustomPreset(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val category: String, // "CASA" or "ESTUDOS"
    val difficulty: String, // "CANDY_EASY", "CANDY_MEDIUM", "CANDY_HARD"
    val emoji: String,
    val subtasksRaw: String // Comma or newline separated list of subtasks
)

