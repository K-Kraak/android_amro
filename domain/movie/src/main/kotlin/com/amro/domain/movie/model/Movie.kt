package com.amro.domain.movie.model

import java.time.LocalDate

data class Movie(
    val identifier: MovieIdentifier,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val releaseDate: LocalDate?,
    val popularity: Double,
    val genreIds: Set<Int>
)