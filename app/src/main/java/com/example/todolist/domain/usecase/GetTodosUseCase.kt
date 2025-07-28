package com.example.todolist.domain.usecase

import com.example.todolist.domain.repository.TodoRepository

class GetTodosUseCase(private val repository: TodoRepository) {
    operator fun invoke() = repository.getTodos()
}