package com.amro.core.database.entity

import androidx.room.Entity

@Entity(tableName = "movies", primaryKeys = ["providerId", "movieId", "language"])
data class MovieEntity(
    val providerId: Int,
    val movieId: String,
    val language: String,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val releaseDate: String?,
    val popularity: Double,
    val genreIds: String
)