package com.example.todolist.data.repository

import com.example.todolist.domain.model.Todo
import com.example.todolist.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TodoRepositoryImpl : TodoRepository {
    private val todos = MutableStateFlow<List<Todo>>(emptyList())
    private var nextId = 1

    override fun getTodos(): Flow<List<Todo>> = todos.asStateFlow()

    override suspend fun addTodo(todo: Todo) {
        todos.value = todos.value + todo.copy(id = nextId++)
    }

    override suspend fun updateTodo(todo: Todo) {
        todos.value = todos.value.map { if (it.id == todo.id) todo else it }
    }

    override suspend fun deleteTodo(todo: Todo) {
        todos.value = todos.value.filter { it.id != todo.id }
    }
}