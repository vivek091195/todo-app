package com.example.todolist.model

enum class TodoStates {
    COMPLETED,
    ACTIVE
}

data class Todo(
    val id: Int,
    val title: String,
    val status: TodoStates,
    val timestamp: Long
)