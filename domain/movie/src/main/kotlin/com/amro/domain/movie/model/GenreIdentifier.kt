package com.amro.domain.movie.model

data class GenreIdentifier(
    val provider: MovieProviderType,
    val value: String,
)