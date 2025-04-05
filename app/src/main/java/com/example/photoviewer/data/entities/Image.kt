package com.example.photoviewer.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

@Entity(tableName = "image_table")
data class Image(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo var uri: String = "",
    @ColumnInfo val thumbnailUri: String = uri,
    @ColumnInfo var title: String = "${UUID.randomUUID().toString()}_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())}",
    @ColumnInfo val isFavorite: Boolean = false
) {
    override fun toString(): String {
        return "Image(id=$id, uri='$uri', thumbnailUri='$thumbnailUri', title='$title', isFavorite=$isFavorite)"
    }
}