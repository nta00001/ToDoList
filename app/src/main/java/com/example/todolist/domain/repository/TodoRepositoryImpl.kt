package com.example.todolist.domain.repository

import com.example.todolist.data.local.TodoDao
import com.example.todolist.domain.model.Todo
import com.example.todolist.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Hilt sẽ inject TodoDao vào đây
class TodoRepositoryImpl @Inject constructor(
    private val dao: TodoDao
) : TodoRepository {

    override fun getTodos(): Flow<List<Todo>> = dao.getTodos()

    override suspend fun addTodo(todo: Todo) {
        dao.addTodo(todo)
    }

    override suspend fun updateTodo(todo: Todo) {
        dao.updateTodo(todo)
    }

    override suspend fun deleteTodo(todo: Todo) {
        dao.deleteTodo(todo)
    }
}