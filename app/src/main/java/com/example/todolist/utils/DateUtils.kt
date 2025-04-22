package com.example.todolist.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
	private val readableDateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
	
	fun formatTimestamp(timestamp: Long?, defaultString: String = "N/A"): String {
		return timestamp?.let {
			try {
				readableDateFormat.format(Date(it))
			} catch (e: Exception) {
				// Handle potential formatting errors
				defaultString
			}
		} ?: defaultString // Return default if timestamp is null
	}
	
	fun formatTimestampWithTime(timestamp: Long?, defaultString: String = "N/A"): String {
		val format = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
		return timestamp?.let {
			try {
				format.format(Date(it))
			} catch (e: Exception) {
				defaultString
			}
		} ?: defaultString
	}
}