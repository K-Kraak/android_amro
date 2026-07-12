package com.amro.application.movie.usecase

import com.amro.application.movie.model.TrendingQuery
import com.amro.application.movie.repository.MovieRepository

class GetTrendingMoviesUseCase(private val repository: MovieRepository) {
    operator fun invoke(query: TrendingQuery) = repository.trending(query)
}