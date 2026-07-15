package com.amro.data.movie.provider.model

import java.time.LocalDate

internal data class ProviderMovieDetails(
    val id: String,
    val title: String,
    val originalTitle: String,
    val overview: String,
    val tagline: String?,
    val releaseDate: LocalDate?,
    val runtimeMinutes: Int?,
    val genres: List<ProviderGenre>,
    val posterUrl: String?,
    val backdropUrl: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val status: String?,
    val homepage: String?,
)
