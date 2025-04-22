package com.example.imageloader.tasks

import ImageLoadListener
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import java.io.BufferedInputStream
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

@Suppress("DEPRECATION")
class ImageLoadAsyncTask(listener: ImageLoadListener) : AsyncTask<String, Unit, Bitmap?>() {
	private val listenerRef: WeakReference<ImageLoadListener?> = WeakReference(listener)
	private val tag = "ImageLoadAsyncTask"
	
	@Deprecated("Deprecated in Java")
	override fun onPreExecute() {
		super.onPreExecute()
		// Notify the listener (if available) that loading is starting using safe call ?.
		listenerRef.get()
			?.onImageLoadStart()
	}
	
	@Deprecated("Deprecated in Java")
	override fun doInBackground(vararg params: String?): Bitmap? {
		// Safely get the URL from parameters, return null if invalid
		val urlString = params.getOrNull(0) ?: return null
		if (urlString.isEmpty()) return null
		
		var connection: HttpURLConnection? = null
		var bitmap: Bitmap? = null
		
		try {
			Log.d(tag, "Attempting to download image from: $urlString")
			var url = URL(urlString)
			connection = ((url.openConnection() as? HttpURLConnection)?.apply {
				connectTimeout = 15000
				readTimeout = 15000
				requestMethod = "GET"
				
				if (responseCode == HttpURLConnection.HTTP_OK) {
					inputStream.use {
						val bufferedStream = BufferedInputStream(it)
						bitmap = BitmapFactory.decodeStream(bufferedStream)
					}
					Log.d(tag, "Image downloaded successfully")
				} else {
					Log.e(tag, "Failed to download image. Response code: $responseCode")
				}
			} ?: Log.e(tag, "Could not open connection or cast to HttpURLConnection")) as HttpURLConnection?
		} catch (e: Exception) {
			Log.e(tag, "Error downloading image: ${e.message}")
			bitmap = null
		} finally {
			connection?.disconnect()
		}
		return bitmap
	}
}