package com.amro.domain.movie.model

data class MovieIdentifier(
    val provider: MovieProviderType,
    val value: String,
)