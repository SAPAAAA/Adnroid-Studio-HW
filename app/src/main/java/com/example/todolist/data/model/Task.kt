package com.example.todolist.data.model

data class Task(
	val id: Int,
	val title: String,
	val description: String?,
	val isCompleted: Boolean,
	val createdAt: Long,
	val updatedAt: Long,
	val dueDate: Long?
)
