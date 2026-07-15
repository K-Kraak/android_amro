package com.amro.application.movie.usecase

import com.amro.application.movie.repository.MovieRepository
import com.amro.domain.movie.model.MovieIdentifier
import javax.inject.Inject

class RefreshMovieDetailsUseCase @Inject constructor(
    private val repository: MovieRepository,
) {
    suspend operator fun invoke(id: MovieIdentifier, language: String): Result<Unit> =
        runCatching { repository.refreshMovieDetails(id, language) }
}
