package com.example.todolist.domain.usecase

import com.example.todolist.domain.model.Todo
import com.example.todolist.domain.repository.TodoRepository

class AddTodoUseCase(private val repository: TodoRepository) {
    suspend operator fun invoke(todo: Todo) = repository.addTodo(todo)
}