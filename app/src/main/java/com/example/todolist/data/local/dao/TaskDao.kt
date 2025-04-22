package com.example.todolist.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todolist.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
	
	// Get all tasks, potentially sorted (using Flow for reactive updates)
	@Query("SELECT * FROM tasks ORDER BY title ASC")
	fun getAllTasksSortedByName(): Flow<List<TaskEntity>> // Returns Flow
	
	@Query("SELECT * FROM tasks ORDER BY due_date ASC")
	fun getAllTasksSortedByDueDate(): Flow<List<TaskEntity>> // Returns Flow
	
	// Get a single task by ID (useful for detail views or updates)
	@Query("SELECT * FROM tasks WHERE id = :taskId")
	fun getTaskById(taskId: Int): Flow<TaskEntity?> // Returns Flow, Task might not exist
	
	// Insert a single task. If conflict (e.g., same ID), replace it.
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertTask(task: TaskEntity) // Use suspend for coroutines
	
	// Insert multiple tasks
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertTasks(tasks: List<TaskEntity>)
	
	// Update an existing task
	@Update
	suspend fun updateTask(task: TaskEntity)
	
	// Update the 'updatedAt' timestamp for a specific task
	@Query("UPDATE tasks SET updated_at = :timestamp WHERE id = :taskId")
	suspend fun updateTimestamp(taskId: Int, timestamp: Long)
	
	// Delete a specific task
	@Delete
	suspend fun deleteTask(task: TaskEntity)
	
	// Delete a task by its ID
	@Query("DELETE FROM tasks WHERE id = :taskId")
	suspend fun deleteTaskById(taskId: Int): Int // Returns number of rows deleted
	
	// Delete all tasks (useful for debugging or specific features)
	@Query("DELETE FROM tasks")
	suspend fun deleteAllTasks()
}