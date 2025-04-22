package com.example.todolist

import android.app.Application // Import Application class
import dagger.hilt.android.HiltAndroidApp // Import the Hilt annotation

/**
 * Custom Application class required by Hilt.
 * Hilt's code generation starts from here.
 */
@HiltAndroidApp
class TaskApplication : Application() {
	
	override fun onCreate() {
		super.onCreate()
		// You can add other application-wide initialization here if needed
	}
}