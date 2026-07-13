package com.amro.application.movie.usecase

import com.amro.application.movie.model.CatalogType
import com.amro.application.movie.model.TrendingQuery
import com.amro.application.movie.policy.TrendingMoviesPolicy
import com.amro.application.movie.repository.MovieRepository
import javax.inject.Inject

class RefreshTrendingMoviesUseCase @Inject constructor(
    private val repository: MovieRepository,
    private val policy: TrendingMoviesPolicy,
) {
    suspend operator fun invoke(query: TrendingQuery, force: Boolean = false) = runCatching {
        if (force || isRefreshRequired(query)) {
            repository.refreshTrending(query)
        }
    }

    suspend fun isRefreshRequired(query: TrendingQuery) =
        policy.requiresRefresh(repository.metadata(query.provider, CatalogType.Trending, query.language))
}