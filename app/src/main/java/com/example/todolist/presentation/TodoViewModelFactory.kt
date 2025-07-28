package com.example.todolist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolist.data.repository.TodoRepositoryImpl
import com.example.todolist.domain.usecase.*

class TodoViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val repo = TodoRepositoryImpl()
        return TodoViewModel(
            GetTodosUseCase(repo),
            AddTodoUseCase(repo),
            UpdateTodoUseCase(repo),
            DeleteTodoUseCase(repo)
        ) as T
    }
}