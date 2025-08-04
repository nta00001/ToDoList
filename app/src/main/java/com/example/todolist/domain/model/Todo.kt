package com.example.todolist.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity // Thêm annotation này
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val isDone: Boolean = false
)