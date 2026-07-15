package com.amro.domain.movie.model

import java.time.LocalDate

data class MovieDetails(
    val id: MovieIdentifier,
    val title: String,
    val originalTitle: String,
    val overview: String,
    val tagline: String?,
    val releaseDate: LocalDate?,
    val runtimeMinutes: Int?,
    val genres: List<Genre>,
    val posterUrl: String?,
    val backdropUrl: String?,
    val voteAverage: Double,
    val voteCount: Int,
    val status: String?,
    val homepage: String?,
)
