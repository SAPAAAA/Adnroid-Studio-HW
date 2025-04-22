package com.example.todolist.data.repository

import com.example.todolist.data.local.dao.TaskDao
import com.example.todolist.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
	private val taskDao: TaskDao
) : TaskRepository {
	override fun getTasksSortedByName(): Flow<List<Task>> {
		return taskDao.getAllTasksSortedByName().map { entityList ->
			entityList.toModelList() // Use mapper
		}
	}
	
	override fun getTasksSortedByDueDate(): Flow<List<Task>> {
		return taskDao.getAllTasksSortedByDueDate().map { entityList ->
			entityList.toModelList() // Use mapper
		}
	}
	
	override fun getTaskById(taskId: Int): Flow<Task?> {
		return taskDao.getTaskById(taskId).map { entity ->
			entity?.toModel() // Use mapper, handle null
		}
	}
	
	override suspend fun addTask(task: Task) {
		// Map to a new entity for insertion
		val newEntity = task.toNewEntity()
		taskDao.insertTask(newEntity)
	}
	
	override suspend fun updateTask(task: Task) {
		// Map to an entity for update (preserves ID, updates timestamp)
		val entityToUpdate = task.toEntity()
		taskDao.updateTask(entityToUpdate)
	}
	
	override suspend fun deleteTaskById(taskId: Int) {
		taskDao.deleteTaskById(taskId)
	}
	
	override suspend fun deleteAllTasks() {
		taskDao.deleteAllTasks()
	}

}