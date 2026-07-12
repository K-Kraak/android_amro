package com.amro.data.movie.provider.tmdb.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class TmdbGenreResponseDto(
    val genres: List<TmdbGenreDto>,
)
