package com.example.todolist.domain.usecase

import com.example.todolist.domain.repository.TodoRepository
import javax.inject.Inject

class GetTodosUseCase @Inject constructor(
    private val todoRepository: TodoRepository
){
    operator fun invoke() = todoRepository.getTodos()
}