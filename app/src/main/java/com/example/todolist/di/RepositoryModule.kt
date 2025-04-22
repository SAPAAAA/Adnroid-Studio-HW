package com.example.todolist.di

import com.example.todolist.data.repository.TaskRepository
import com.example.todolist.data.repository.TaskRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Lives as long as the application
abstract class RepositoryModule {
	@Binds
	@Singleton
	abstract fun bindTaskRepository(
		taskRepositoryImpl: TaskRepositoryImpl
	): TaskRepository
}