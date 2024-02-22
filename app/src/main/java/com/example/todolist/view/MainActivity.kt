package com.example.todolist.view

import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.example.todolist.R
import com.example.todolist.databinding.MainBinding
import com.example.todolist.model.Todo
import com.example.todolist.model.TodoStates
import com.example.todolist.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private lateinit var mainBinding: MainBinding
    private var enteredText: String = ""
    val mainViewModel by viewModels<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.storeTodo(this, Todo(1, "Hey there", TodoStates.ACTIVE, System.currentTimeMillis()))
        mainViewModel.retrieveTodos(this)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        mainBinding = MainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        setUpUI()
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
            mainViewModel.storeTodo(it.context, Todo(
                1,
                enteredText,
                TodoStates.ACTIVE,
                System.currentTimeMillis()
            ))
        }
    }
}