package com.example.todolist.model

enum class TodoStates {
    COMPLETED,
    ACTIVE
}

data class Todo(
    val id: Int,
    val title: String,
    var status: TodoStates,
    val timestamp: Long
)

enum class ActiveTodoSection {
    ALL,
    COMPLETED,
    ACTIVE
}