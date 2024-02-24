package com.example.todolist.view

import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.core.view.isVisible
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
        mainViewModel.retrieveTodos(this)
        mainBinding.todoTextInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                mainBinding.todoRadioInput.isChecked = false
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                enteredText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        mainBinding.todoRadioInput.setOnClickListener {
            if(enteredText.isEmpty())
                return@setOnClickListener
            mainViewModel.storeTodo(it.context, Todo(
                1,
                enteredText,
                TodoStates.ACTIVE,
                System.currentTimeMillis()
            ))
            mainBinding.todoTextInput.setText("")
        }

        mainBinding.clearCompleteBtn.setOnClickListener {
            mainViewModel.clearTodos(it.context)
        }

        mainBinding.todoList.adapter = adapter
    }

    private fun setupStateFlowListeners() {
        mainViewModel.viewModelScope.launch {
            mainViewModel.todoState.collect {
                if(it.allTodos.isNullOrEmpty()) {
                    mainBinding.emptyTodoSection.isVisible = true
                    mainBinding.todoItemsCard.isVisible = false
                    mainBinding.detailedSection.isVisible = false
                    mainBinding.filterSection.isVisible = false
                    mainBinding.instruction.isVisible = false
                } else {
                    mainBinding.emptyTodoSection.isVisible = false
                    mainBinding.todoItemsCard.isVisible = true
                    mainBinding.detailedSection.isVisible = true
                    mainBinding.filterSection.isVisible = true
                    mainBinding.instruction.isVisible = true
                }
                adapter.submitList(it.allTodos)
            }
        }
    }
}