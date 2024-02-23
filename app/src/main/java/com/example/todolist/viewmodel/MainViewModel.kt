package com.example.todolist.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.todolist.model.SharedPreferencesUtility
import com.example.todolist.model.Todo
import com.example.todolist.model.TodoStates
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    private val _todoState = MutableStateFlow(TodoState())
    val todoState = _todoState.asStateFlow()
    internal fun storeTodo(context: Context, todo: Todo) {
        SharedPreferencesUtility.writeToPreferences(context, todo)
    }

    internal fun retrieveTodos(context: Context) {
        val todos: List<Todo> = SharedPreferencesUtility.readFromPreferences(context)
        _todoState.update {
            it.copy(
                allTodos = todos.map {
                    TodoState.Todo(
                        id = it.id,
                        title = it.title,
                        status = it.status,
                        timestamp = it.timestamp
                    )
                }
            )
        }
    }

    data class TodoState(
        val allTodos: List<Todo>? = null
    ) {
        data class Todo(
            val id: Int,
            val title: String,
            val status: TodoStates,
            val timestamp: Long
        )
    }
}