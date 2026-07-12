package com.amro.data.movie.provider.tmdb.dto

import kotlinx.serialization.Serializable

@Serializable
internal data class TmdbGenreDto(
    val id: Int,
    val name: String,
)
