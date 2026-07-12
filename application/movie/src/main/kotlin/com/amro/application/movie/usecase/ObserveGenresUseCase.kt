package com.amro.application.movie.usecase

import com.amro.application.movie.repository.MovieRepository
import com.amro.domain.movie.model.MovieProviderType

class ObserveGenresUseCase(private val repository: MovieRepository) {
    operator fun invoke(provider: MovieProviderType, language: String) =
        repository.genres(provider, language)
}

