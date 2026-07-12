package com.amro.data.movie.provider.tmdb.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TmdbMovieDto(
    val id: Int,
    val title: String,
    val overview: String = "",
    val popularity: Double = 0.0,
    @SerialName("release_date")
    val releaseDate: String? = null,
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("genre_ids")
    val genreIds: List<Int> = emptyList(),
)
