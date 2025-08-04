package com.example.todolist.presentation.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ItemTodoBinding
import com.example.todolist.domain.model.Todo

class TodoAdapter(
    private val onCheckChanged: (Todo) -> Unit,
    private val onDelete: (Todo) -> Unit
) : ListAdapter<Todo, TodoAdapter.TodoViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TodoViewHolder(private val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Todo) {
            binding.cbDone.isChecked = todo.isDone
            binding.tvTitle.text = todo.title
            binding.cbDone.setOnCheckedChangeListener { _, _ -> onCheckChanged(todo) }
            binding.btnDelete.setOnClickListener { onDelete(todo) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Todo, newItem: Todo) = oldItem == newItem
    }
}