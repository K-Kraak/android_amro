package com.amro.core.database.entity

import androidx.room.Entity

@Entity(tableName = "catalog_metadata", primaryKeys = ["providerId", "catalogId", "language"])
data class CatalogMetadataEntity(
    val providerId: Int,
    val catalogId: Int,
    val language: String,
    val updatedAtEpochMillis: Long,
    val pageCount: Int,
    val itemCount: Int
)