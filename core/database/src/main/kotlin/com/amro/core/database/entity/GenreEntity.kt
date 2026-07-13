package com.amro.core.database.entity

import androidx.room.Entity

@Entity(tableName = "genres", primaryKeys = ["providerId", "genreId", "language"])
data class GenreEntity(
    val providerId: Int,
    val genreId: String,
    val language: String,
    val name: String,
)