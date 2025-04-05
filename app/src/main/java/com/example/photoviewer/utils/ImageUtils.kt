package com.example.photoviewer.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects

object ImageUtils {
    fun saveBitmapToInternalStorage(context: Context, bitmap: Bitmap, filename: String): String {
        // Create (or get) the 'images' folder inside internal storage
        val imagesDir = File(context.filesDir, "images")
        if (!imagesDir.exists()) {
            val dirCreated = imagesDir.mkdir() // create the directory if it doesn't exist
            if (!dirCreated) {
                Log.e("ImageUtils", "Failed to create images directory")
                return ""
            }
        }

        // Create the file in that directory
        val file = File(imagesDir, filename)
        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        // Log out the image path
        Log.d("ImageUtils", "Image saved to: ${file.absolutePath}")
        return file.absolutePath
    }
    
    fun deleteImageFromInternalStorage(context: Context, filename: String?): Boolean {
        // Get the 'images' folder inside internal storage
        val imagesDir = File(context.filesDir, "images")
        if (imagesDir.exists()) {
            // Get the file in that directory
            val file = File(imagesDir, filename ?: return false)
            if (file.exists()) {
                return file.delete()
            }
        }
        return false
    }

    fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { stream ->
                BitmapFactory.decodeStream(stream)
            }
        } catch (e: Exception) {
            Log.e("ImageUtils", "Failed to get bitmap from uri", e)
            null
        }
    }
    
    fun createImageUri(context: Context): Uri? {
        try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val imageFileName = "TEMP_IMAGE_${timeStamp}"
            val storageDir = context.cacheDir // Or context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val imageFile = File.createTempFile(
                imageFileName,
                ".png",
                storageDir
            )
            // Important: Use your app's authority string defined in AndroidManifest.xml
            val authority = "com.example.photoviewer.fileprovider"
            return FileProvider.getUriForFile(
                Objects.requireNonNull(context),
                authority,
                imageFile
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
    }
    
    fun deleteCacheFile(context: Context, uri: Uri): Boolean {
        var deleted = false
        try {
            // Use the passed context to get the contentResolver
            val deletedRows = context.contentResolver.delete(uri, null, null)
            if (deletedRows > 0) {
                Log.d("ImageUtils", "Successfully deleted temp cache file via URI: $uri")
                deleted = true
            } else {
                // This might happen if the file was already gone or the URI was invalid
                Log.w("ImageUtils", "Could not delete temp cache file via URI (or it didn't exist): $uri")
            }
        } catch (e: SecurityException) {
            // This can happen if the URI is invalid or permissions are wrong (e.g., incorrect FileProvider authority)
            Log.e("ImageUtils", "SecurityException deleting temp cache file: $uri", e)
        }
        catch (e: Exception) {
            Log.e("ImageUtils", "Error deleting temp cache file: $uri", e)
        }
        return deleted
    }
}
