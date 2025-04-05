package com.example.photoviewer.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.photoviewer.data.entities.Image

/**
 * Data Access Object for handling Image entity operations.
 * Provides methods to interact with the images table in the database.
 */
@Dao
interface ImageDao {
    @Insert
    suspend fun insertImage(image: Image): Long

    @Query("DELETE FROM image_table WHERE id = :imageId")
    suspend fun deleteImageById(imageId: Int): Int

    @Query("SELECT * FROM image_table")
    suspend fun getAllImages(): List<Image>

    @Query("SELECT * FROM image_table WHERE id = :imageId")
    suspend fun getImageById(imageId: Int): Image?

    @Query("UPDATE image_table SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateImageFavoriteStatus(id: Int, isFavorite: Boolean)
}
