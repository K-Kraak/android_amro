package com.amro.core.database.entity

import androidx.room.Entity

@Entity(
    tableName = "movie_details",
    primaryKeys = ["providerId", "movieId", "language"],
)
data class MovieDetailsEntity(
    val providerId: Int,
    val movieId: String,
    val language: String,
    val title: String,
    val originalTitle: String,
    val overview: String,
    val tagline: String?,
    val releaseDate: String?,
    val runtimeMinutes: Int?,
    val genreIds: String,
    val genreNames: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val status: String?,
    val homepage: String?,
    val updatedAtEpochMillis: Long,
)
