package com.amro.feature.details

import com.amro.domain.movie.model.MovieDetails

data class MovieDetailsUiState(
    val movie: MovieDetails? = null,
    val loading: Boolean = true,
    val refreshing: Boolean = false,
    val error: MovieDetailsUiError? = null,
)

sealed interface MovieDetailsUiError {
    data object LoadingFailed : MovieDetailsUiError
}
