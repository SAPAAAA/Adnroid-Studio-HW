package com.example.todolist.data.repository

import com.example.todolist.data.local.entity.TaskEntity
import com.example.todolist.data.model.Task

// Maps TaskEntity (from database) to Task (domain model)
fun TaskEntity.toModel(): Task {
	return Task(
		id = this.id,
		title = this.title,
		description = this.description,
		isCompleted = this.isCompleted,
		createdAt = this.createdAt,
		updatedAt = this.updatedAt,
		dueDate = this.dueDate
	)
}

// Maps a list of TaskEntity to a list of Task
fun List<TaskEntity>.toModelList(): List<Task> {
	return this.map { it.toModel() }
}

// Maps Task (domain model) to TaskEntity (for database insertion/update)
fun Task.toEntity(): TaskEntity {
	return TaskEntity(
		id = this.id,
		title = this.title,
		description = this.description,
		isCompleted = this.isCompleted,
		createdAt = this.createdAt, // Keep original creation time
		updatedAt = System.currentTimeMillis(), // Update modification time
		dueDate = this.dueDate
	)
}

// Overload for creating a new entity where ID is not yet known (auto-generated)
// Or when adding a task, we might only have title/dueDate initially
fun Task.toNewEntity(): TaskEntity {
	return TaskEntity(
		// id = 0, // Let Room autoGenerate handle this
		title = this.title,
		description = this.description,
		isCompleted = this.isCompleted,
		createdAt = System.currentTimeMillis(), // Set creation time now
		updatedAt = System.currentTimeMillis(), // Set modification time now
		dueDate = this.dueDate
	)
}