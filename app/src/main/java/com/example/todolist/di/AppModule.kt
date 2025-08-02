package com.example.todolist.di

import android.app.Application
import androidx.room.Room
import com.example.todolist.data.local.AppDatabase
import com.example.todolist.domain.repository.TodoRepositoryImpl
import com.example.todolist.domain.repository.TodoRepository
import com.example.todolist.domain.usecase.AddTodoUseCase
import com.example.todolist.domain.usecase.DeleteTodoUseCase
import com.example.todolist.domain.usecase.GetTodosUseCase
import com.example.todolist.domain.usecase.UpdateTodoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "todo_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(db: AppDatabase): TodoRepository {
        return TodoRepositoryImpl(db.todoDao())
    }

    // ✅ THÊM CÁC HÀM DƯỚI ĐÂY
    // Hilt sẽ thấy các hàm này và biết cách tạo ra các UseCase.
    // Nó sẽ tự động lấy TodoRepository từ hàm ở trên để truyền vào.

    @Provides
    @Singleton
    fun provideGetTodosUseCase(repository: TodoRepository): GetTodosUseCase {
        return GetTodosUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAddTodoUseCase(repository: TodoRepository): AddTodoUseCase {
        return AddTodoUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateTodoUseCase(repository: TodoRepository): UpdateTodoUseCase {
        return UpdateTodoUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteTodoUseCase(repository: TodoRepository): DeleteTodoUseCase {
        return DeleteTodoUseCase(repository)
    }
}