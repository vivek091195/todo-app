package com.example.todolist.view

import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.viewModelScope
import com.example.todolist.R
import com.example.todolist.databinding.MainBinding
import com.example.todolist.model.Todo
import com.example.todolist.model.TodoStates
import com.example.todolist.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var mainBinding: MainBinding
    private var enteredText: String = ""
    val mainViewModel by viewModels<MainViewModel>()
    val adapter = TodoAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = MainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        setUpUI()
        setupStateFlowListeners()
    }

    private fun setUpUI() {
        mainBinding.todoTextInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                enteredText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        mainBinding.todoRadioInput.setOnClickListener {
            Log.d("MainActivity", "I am being called")
            mainViewModel.storeTodo(it.context, Todo(
                1,
                enteredText,
                TodoStates.ACTIVE,
                System.currentTimeMillis()
            ))
            mainViewModel.retrieveTodos(it.context)
        }

        mainBinding.todoList.adapter = adapter
    }

    private fun setupStateFlowListeners() {
        mainViewModel.viewModelScope.launch {
            mainViewModel.todoState.collect {
                adapter.submitList(it.allTodos)
            }
        }
    }
}