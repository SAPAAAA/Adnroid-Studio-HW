package com.example.photoviewer.repository

import com.example.photoviewer.data.dao.ImageDao
import com.example.photoviewer.data.entities.Image

class ImageRepository(private val imageDao: ImageDao) {

    suspend fun getImages(): List<Image> = imageDao.getAllImages()

    suspend fun insertImage(image: Image) {
        imageDao.insertImage(image)
        // Log that code is reached
        android.util.Log.d("ImageRepository", "Image inserted successfully")
    }

    suspend fun deleteImageById(imageId: Int) {
        imageDao.deleteImageById(imageId)
    }

    suspend fun updateImageFavoriteStatus(imageId: Int, isFavorite: Boolean) {
        imageDao.updateImageFavoriteStatus(imageId, isFavorite)
    }
}