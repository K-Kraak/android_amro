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

@Entity(tableName = "genres", primaryKeys = ["providerId", "genreId", "language"])
data class GenreEntity(val providerId: Int, val genreId: String, val language: String, val name: String)

@Entity(tableName = "catalog_metadata", primaryKeys = ["providerId", "catalogId", "language"])
data class CatalogMetadataEntity(
    val providerId: Int,
    val catalogId: Int,
    val language: String,
    val updatedAtEpochMillis: Long,
    val pageCount: Int,
    val itemCount: Int
)