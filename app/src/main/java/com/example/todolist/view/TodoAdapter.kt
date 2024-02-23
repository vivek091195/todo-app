package com.example.todolist.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.todolist.databinding.TodoListItemBinding
import com.example.todolist.viewmodel.MainViewModel

class TodoAdapter :
    ListAdapter<MainViewModel.TodoState.Todo, TodoAdapter.TodoViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val todoListBinding =
            TodoListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(todoListBinding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TodoViewHolder(private val binding: TodoListItemBinding) :
        ViewHolder(binding.root) {
        fun bind(item: MainViewModel.TodoState.Todo) {
            with(binding) {
                todoItemTitle.text = item.title
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MainViewModel.TodoState.Todo>() {
            override fun areItemsTheSame(
                oldItem: MainViewModel.TodoState.Todo,
                newItem: MainViewModel.TodoState.Todo
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: MainViewModel.TodoState.Todo,
                newItem: MainViewModel.TodoState.Todo
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}