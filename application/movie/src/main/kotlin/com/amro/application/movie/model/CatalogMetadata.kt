package com.amro.application.movie.model

import com.amro.domain.movie.model.MovieProviderType
import java.time.Instant

data class CatalogMetadata(
    val provider: MovieProviderType,
    val language: String,
    val catalog: CatalogType,
    val updatedAt: Instant,
    val pageCount: Int,
    val itemCount: Int
)