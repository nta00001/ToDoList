package com.example.todolist.presentation.todolist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.domain.model.Todo
import com.example.todolist.domain.usecase.AddTodoUseCase
import com.example.todolist.domain.usecase.DeleteTodoUseCase
import com.example.todolist.domain.usecase.GetTodosUseCase
import com.example.todolist.domain.usecase.UpdateTodoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // Thêm annotation này
class TodoViewModel @Inject constructor( // Thêm annotation này
    getTodos: GetTodosUseCase,
    private val addTodo: AddTodoUseCase,
    private val updateTodo: UpdateTodoUseCase,
    private val deleteTodo: DeleteTodoUseCase
) : ViewModel() {

    val todos = getTodos().stateIn(viewModelScope, SharingStarted.Lazily, emptyList<Todo>())

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