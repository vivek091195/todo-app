package com.example.todolist.model

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonParseException

object SharedPreferencesUtility {
    private const val PREFS_NAME = "todo_prefs"
    private const val TODO_KEY = "todos"
    private val gson = Gson()

    fun writeToPreferences(context: Context, todo: Todo) {
        val sharedPrefs: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val storedTodo = readFromPreferences(context).toMutableList()
        val existingTodo = storedTodo.filter { stored -> stored.id == todo.id }
        if (existingTodo.isEmpty()) {
            storedTodo.add(todo)
        } else {
            existingTodo[0].status = todo.status
        }

        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        editor.putString(TODO_KEY, gson.toJson(storedTodo))
        editor.apply()
    }

    fun readFromPreferences(context: Context): List<Todo> {
        val sharedPrefs: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val todoJsonList = sharedPrefs.getString(TODO_KEY, null) ?: return emptyList()

        return try {
            gson.fromJson(todoJsonList, Array<Todo>::class.java).toList()
        } catch (e: JsonParseException) {
            Log.e("TodoJsonList", "Error parsing JSON", e)
            emptyList()
        }
    }

    fun clearCompletedPreferences(context: Context) {
        val storedTodo = readFromPreferences(context).toMutableList()
        clearPreferences(context)
        storedTodo.filter { it.status == TodoStates.COMPLETED }
            .forEach { storedTodo.remove(it) }
        for (todo in storedTodo) {
            writeToPreferences(context, todo)
        }
    }

    fun clearPreferences(context: Context) {
        val sharedPrefs: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        editor.clear()
        editor.apply()
    }
}