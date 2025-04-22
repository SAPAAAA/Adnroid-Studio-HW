package com.example.imageloader.loaders

import androidx.loader.content.AsyncTaskLoader
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL

@Suppress("DEPRECATION")
class ImageLoader(context: Context, private val imageUrl: String?) : AsyncTaskLoader<Bitmap?>(context) {
	// Cache for the loaded bitmap to handle configuration changes
	private var cachedBitmap: Bitmap? = null
	private val tag: String = "ImageLoader"
	
	@Deprecated("Deprecated in Java")
	override fun onStartLoading() {
		super.onStartLoading()
		if (cachedBitmap != null) {
			Log.d(tag, "Delivering cached bitmap")
			deliverResult(cachedBitmap)
		} else {
			Log.d(tag, "No cached bitmap, forcing load")
			forceLoad()
		}
	}
	
	@Deprecated("Deprecated in Java")
	override fun loadInBackground(): Bitmap? {
		Log.d(tag, "loadInBackground started for URL: $imageUrl")
		if (imageUrl.isNullOrEmpty()) return null // Basic validation
		
		var connection: HttpURLConnection? = null
		var bitmap: Bitmap? = null
		
		try {
			val url = URL(imageUrl)
			connection = (url.openConnection() as? HttpURLConnection)?.apply {
				connectTimeout = 15000
				readTimeout = 10000
				requestMethod = "GET"
				
				if (responseCode == HttpURLConnection.HTTP_OK) {
					inputStream.use {
						val bufferedStream = BufferedInputStream(it)
						bitmap = BitmapFactory.decodeStream(bufferedStream)
					}
					Log.d(tag, "Bitmap loaded successfully in background")
				} else {
					Log.e(tag, "Server error in background: $responseCode")
				}
			}
		} catch (e: Exception) {
			Log.e(tag, "Error loading image in background: ${e.message}", e)
			bitmap = null
		} finally {
			connection?.disconnect()
		}
		return bitmap
	}
	
	@Deprecated("Deprecated in Java")
	override fun deliverResult(data: Bitmap?) {
		cachedBitmap = data
		if (isStarted) {
			super.deliverResult(data)
			Log.d(tag, "Bitmap delivered to UI")
		} else {
			Log.d(tag, "Loader not started, not delivering result")
		}
	}
	
	@Deprecated("Deprecated in Java")
	override fun onStopLoading() {
		super.onStopLoading()
		
		cancelLoad()
		Log.d(tag, "onStopLoading called, load cancelled.")
	}
	
	@Deprecated("Deprecated in Java")
	override fun onReset() {
		super.onReset()
		
		onStopLoading()
		cachedBitmap = null
		Log.d(tag, "onReset called, clearing cached bitmap.")
	}
}