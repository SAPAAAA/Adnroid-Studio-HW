package com.example.todolist.ui.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.data.model.Task
import com.example.todolist.data.repository.TaskRepository
import com.example.todolist.utils.SortOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
	private val taskRepository: TaskRepository
) : ViewModel() {
	private val _sortOrder = MutableStateFlow(SortOrder.BY_DATE)
	
	@OptIn(ExperimentalCoroutinesApi::class)
	private val _tasksFlow = _sortOrder.flatMapLatest { sortOrder ->
		when (sortOrder) {
			SortOrder.BY_NAME -> taskRepository.getTasksSortedByName()
			SortOrder.BY_DATE -> taskRepository.getTasksSortedByDueDate()
		}
	}.catch { throwable -> // Catch errors from the repository flow
		// In a real app, emit an error state here
		_userMessage.value = throwable.message ?: "An error occurred"
		emit(emptyList()) // Emit empty list on error
	}
	
	private val _isLoading = MutableStateFlow(false)
	private val _userMessage = MutableStateFlow<String?>(null) // For errors/messages
	
	val uiState: StateFlow<TaskListUiState> = combine(
		_tasksFlow,
		_isLoading,
		_userMessage,
		_sortOrder
	) { tasks, isLoading, message, sortOrder ->
		TaskListUiState(
			tasks = tasks,
			isLoading = isLoading,
			userMessage = message,
			sortOrder = sortOrder
		)
	}.stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5000), // Keep flow active 5s after last observer
		initialValue = TaskListUiState(isLoading = true) // Start in loading state
	)
	
	fun addTask(title: String, description: String?, dueDate: Long?) {
		if (title.isBlank()) {
			_userMessage.value = "Task title cannot be empty."
			return
		}
		viewModelScope.launch {
			_isLoading.value = true
			try {
				val newTask = Task(
					id = 0,
					title = title,
					description = description,
					isCompleted = false, // Default for new task
					createdAt = System.currentTimeMillis(), // Set on creation
					updatedAt = System.currentTimeMillis(),
					dueDate = dueDate
				)
				taskRepository.addTask(newTask)
			} catch (e: Exception) {
				_userMessage.value = e.message ?: "Failed to add task."
			} finally {
				_isLoading.value = false
			}
		}
	}
	
	fun deleteTask(task: Task) {
		viewModelScope.launch {
			_isLoading.value = true
			try {
				taskRepository.deleteTaskById(task.id)
				_userMessage.value = "Task deleted."
			} catch (e: Exception) {
				_userMessage.value = e.message ?: "Failed to delete task."
			} finally {
				_isLoading.value = false
			}
		}
	}
	
	fun toggleTaskCompletion(task: Task) {
		viewModelScope.launch {
			_isLoading.value = true
			try {
				val updatedTask = task.copy(isCompleted = !task.isCompleted)
				taskRepository.updateTask(updatedTask)
				_userMessage.value = "Task updated."
			} catch (e: Exception) {
				_userMessage.value = e.message ?: "Failed to update task."
			} finally {
				_isLoading.value = false
			}
		}
	}
	
	fun changeSortOrder(newSortOrder: SortOrder) {
		_sortOrder.value = newSortOrder
	}
	
	fun userMessageShown() {
		_userMessage.value = null // Clear message after it's shown
	}
}