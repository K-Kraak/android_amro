package com.amro.data.movie.provider.tmdb.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TmdbMovieDetailsDto(
    val id: Int,
    val title: String,
    @SerialName("original_title")
    val originalTitle: String,
    val overview: String = "",
    val tagline: String? = null,
    @SerialName("release_date")
    val releaseDate: String? = null,
    val runtime: Int? = null,
    val genres: List<TmdbGenreDto> = emptyList(),
    @SerialName("poster_path")
    val posterPath: String? = null,
    @SerialName("backdrop_path")
    val backdropPath: String? = null,
    @SerialName("vote_average")
    val voteAverage: Double = 0.0,
    @SerialName("vote_count")
    val voteCount: Int = 0,
    val status: String? = null,
    val homepage: String? = null,
)
