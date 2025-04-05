package com.example.photoviewer.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.photoviewer.data.database.ImageDatabase
import com.example.photoviewer.data.entities.Image
import com.example.photoviewer.repository.ImageRepository
import com.example.photoviewer.utils.ImageUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PhotoViewModel(application: Application) : AndroidViewModel(application) {
    private val _photos: MutableStateFlow<List<Image>> = MutableStateFlow(emptyList())
    val photos: StateFlow<List<Image>> = _photos.asStateFlow()

    private val repository: ImageRepository

    init {
        val imageDao = ImageDatabase.getDatabase(application).imageDao()
        repository = ImageRepository(imageDao)
        loadPhotos()
    }

    private fun loadPhotos() {
        viewModelScope.launch {
            val images = repository.getImages()
            _photos.update { images }
        }
    }
    
    // Function specifically for photos taken with the camera
    fun addPhotoFromCamera(tempCameraUri: Uri) {
        viewModelScope.launch {
            processAndSaveImage(tempCameraUri, true) // Pass true to indicate deletion needed
        }
    }
    
    // Function specifically for photos selected from storage
    fun addPhotoFromStorage(storageUri: Uri) {
        viewModelScope.launch {
            processAndSaveImage(storageUri, false) // Pass false, source should not be deleted
        }
    }
    
    // *** Private helper function for the common logic ***
    private suspend fun processAndSaveImage(sourceUri: Uri, deleteSourceAfterProcessing: Boolean) {
        var bitmap: Bitmap?
        try {
            bitmap = ImageUtils.getBitmapFromUri(getApplication(), sourceUri)
        } catch (e: Exception) {
            Log.e("PhotoViewModel", "Error getting bitmap from source URI: $sourceUri", e)
            if (deleteSourceAfterProcessing) {
                ImageUtils.deleteCacheFile(getApplication(), sourceUri)
            }
            return
        }
        
        if (bitmap != null) {
            // Create Image entity using the internal storage URI
            val newImage = Image()
            
            var newInternalImageUriString: String?
            try {
                newInternalImageUriString = ImageUtils.saveBitmapToInternalStorage(getApplication(), bitmap, newImage.title)
                Log.d("PhotoViewModel", "Image saved successfully to internal storage: $newInternalImageUriString")
                
                newImage.uri = newInternalImageUriString
                repository.insertImage(newImage)
                loadPhotos() // Refresh UI
                
                // Conditionally delete the source file using the flag
                if (deleteSourceAfterProcessing) {
                    ImageUtils.deleteCacheFile(getApplication(), sourceUri)
                }
                
            } catch (e: Exception) {
                Log.e("PhotoViewModel", "Error saving bitmap/inserting/deleting cache.", e)
                if (deleteSourceAfterProcessing) {
                    ImageUtils.deleteCacheFile(getApplication(), sourceUri)
                }
            } finally {
                // bitmap.recycle() // Optional
                bitmap.recycle()
            }
        } else {
            Log.w("PhotoViewModel", "Bitmap was null for source URI: $sourceUri. Cannot save.")
            if (deleteSourceAfterProcessing) {
                ImageUtils.deleteCacheFile(getApplication(), sourceUri)
            }
        }
    }

    fun deletePhoto(imageId: Int) {
        viewModelScope.launch {
            val photoToDelete = _photos.value.find { it.id == imageId }
            val filename = photoToDelete?.title
            
            repository.deleteImageById(imageId)
            
            if (filename != null) {
                ImageUtils.deleteImageFromInternalStorage(getApplication(), filename)
            } else {
                Log.w("PhotoViewModel", "Could not find filename for image ID $imageId to delete internal file.")
            }
            loadPhotos()
        }
    }

    fun favoritePhoto(imageId: Int) {
        viewModelScope.launch {
            val photo = _photos.value.find { it.id == imageId }
            if (photo != null) {
                val newFavoriteStatus = !photo.isFavorite
                repository.updateImageFavoriteStatus(imageId, newFavoriteStatus)
                loadPhotos()
            } else {
                Log.w("PhotoViewModel", "Attempted to favorite non-existent image ID: $imageId")
            }
        }
    }
}