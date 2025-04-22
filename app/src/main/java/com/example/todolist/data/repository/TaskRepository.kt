package com.example.todolist.data.repository

import com.example.todolist.data.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
	fun getTasksSortedByName(): Flow<List<Task>>
	fun getTasksSortedByDueDate(): Flow<List<Task>>
	fun getTaskById(taskId: Int): Flow<Task?>
	suspend fun addTask(task: Task)
	suspend fun updateTask(task: Task)
	suspend fun deleteTaskById(taskId: Int)
	suspend fun deleteAllTasks()
}