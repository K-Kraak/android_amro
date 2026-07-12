package com.amro.data.movie.provider.model

import java.time.LocalDate

internal data class ProviderMovie(
    val id: String,
    val title: String,
    val overview: String,
    val genreIds: Set<String>,
    val popularity: Double,
    val releaseDate: LocalDate?,
    val posterPath: String?,
)