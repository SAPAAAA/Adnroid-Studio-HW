package com.example.todolist.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks") // Specifies the table name in the database
data class TaskEntity(
	@PrimaryKey(autoGenerate = true)
	val id: Int = 0, // Default value needed for autoGenerate
	
	@ColumnInfo(name = "title")
	val title: String,
	
	@ColumnInfo(name = "description")
	val description: String?,
	
	@ColumnInfo(name = "is_completed")
	val isCompleted: Boolean = false,
	
	@ColumnInfo(name = "created_at")
	val createdAt: Long = System.currentTimeMillis(),
	
	@ColumnInfo(name = "updated_at")
	val updatedAt: Long = System.currentTimeMillis(),
	
	@ColumnInfo(name = "due_date")
	val dueDate: Long?
)