package com.example.todolist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.domain.model.Todo
import com.example.todolist.domain.usecase.AddTodoUseCase
import com.example.todolist.domain.usecase.DeleteTodoUseCase
import com.example.todolist.domain.usecase.GetTodosUseCase
import com.example.todolist.domain.usecase.UpdateTodoUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TodoViewModel(
    getTodos: GetTodosUseCase,
    private val addTodo: AddTodoUseCase,
    private val updateTodo: UpdateTodoUseCase,
    private val deleteTodo: DeleteTodoUseCase
) : ViewModel() {

    val todos = getTodos().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun add(title: String) {
        viewModelScope.launch {
            addTodo(Todo(title = title))
        }
    }

    fun toggle(todo: Todo) {
        viewModelScope.launch {
            updateTodo(todo.copy(isDone = !todo.isDone))
        }
    }

    fun delete(todo: Todo) {
        viewModelScope.launch {
            deleteTodo(todo)
        }
    }
}
