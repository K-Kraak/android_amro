package com.amro.application.movie.usecase

import com.amro.application.movie.model.TrendingQuery
import com.amro.application.movie.repository.MovieRepository
import javax.inject.Inject

class GetTrendingMoviesUseCase @Inject constructor(private val repository: MovieRepository) {
    operator fun invoke(query: TrendingQuery) = repository.trending(query)
}