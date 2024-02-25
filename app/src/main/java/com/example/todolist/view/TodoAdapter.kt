package com.example.todolist.view

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.todolist.databinding.TodoListItemBinding
import com.example.todolist.model.Todo
import com.example.todolist.model.TodoStates
import com.example.todolist.viewmodel.MainViewModel

var selectedItemPosition: Int = RecyclerView.NO_POSITION

class TodoAdapter(private val completeTodoClickHandler: (Todo) -> Unit) :
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
                if (item.status == TodoStates.COMPLETED) {
                    todoItemTitle.setTextColor(android.graphics.Color.parseColor("#d1d2da"))
                    strikethrough.isVisible = true
                } else {
                    todoItemTitle.setTextColor(android.graphics.Color.parseColor("#494c6b"))
                    strikethrough.isVisible = false
                }
                doneRadio.setOnClickListener {
                    itemSelectionHandler(
                        bindingAdapterPosition, Todo(
                            item.id,
                            item.title,
                            TodoStates.COMPLETED,
                            System.currentTimeMillis()
                        )
                    )
                }
            }
        }
    }

    private fun itemSelectionHandler(adapterPosition: Int, todo: Todo) {
        Log.d(TAG, "Item clicked $adapterPosition")
        if (adapterPosition == RecyclerView.NO_POSITION) return
        notifyItemChanged(selectedItemPosition)
        selectedItemPosition = adapterPosition
        notifyItemChanged(selectedItemPosition)
        completeTodoClickHandler(todo)
    }

    companion object {
        private const val TAG = "TodoAdapter"
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