package com.example.imageloader.services

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.imageloader.utils.NotificationUtils

class NotificationService : Service() {
	private val tag = "NotificationService"
	
	companion object {
		private const val INTERVAL_MS = 5 * 60 * 1000L
	}
	
	private lateinit var handler: Handler
	private lateinit var runnable: Runnable
	
	@RequiresApi(Build.VERSION_CODES.O)
	override fun onCreate() {
		super.onCreate()
		Log.d(tag, "Service created")
		handler = Handler(Looper.getMainLooper()) // Get handler for the main thread
		
		// Create the notification channel
		NotificationUtils.createNotificationChannel(this)
		
		runnable = Runnable {
			Log.d(tag, "Notification Runnable executing...")
			
			val notification = NotificationUtils.buildNotification(this@NotificationService)
			
			NotificationUtils.showNotification(this@NotificationService, notification)
			
			scheduleNextNotification()
		}
	}
	
	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		Log.d(tag, "Service onStartCommand")
		// Remove any existing callbacks to prevent duplicates if service restarts
		handler.removeCallbacks(runnable)
		// Schedule the first execution of the runnable
		scheduleNextNotification()
		
		return START_STICKY
	}
	
	private fun scheduleNextNotification() {
		Log.d(tag, "Scheduling next notification in $INTERVAL_MS ms")
		handler.postDelayed(runnable, INTERVAL_MS)
	}
	
	override fun onDestroy() {
		super.onDestroy()
		Log.d(tag, "Service onDestroy")
		if (::handler.isInitialized) {
			handler.removeCallbacks(runnable)
		}
	}
	
	// Required method for bound services, return null as this is a started service
	override fun onBind(intent: Intent?): IBinder? {
		return null
	}
}