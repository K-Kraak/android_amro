package com.amro.application.movie.usecase

import com.amro.application.movie.repository.MovieRepository
import com.amro.domain.movie.model.MovieDetails
import com.amro.domain.movie.model.MovieIdentifier
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveMovieDetailsUseCase @Inject constructor(
    private val repository: MovieRepository,
) {
    operator fun invoke(id: MovieIdentifier, language: String): Flow<MovieDetails?> =
        repository.observeMovieDetails(id, language)
}
