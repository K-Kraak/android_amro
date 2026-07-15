package com.amro.application.movie.repository

import androidx.paging.PagingData
import com.amro.application.movie.model.CatalogMetadata
import com.amro.application.movie.model.CatalogType
import com.amro.application.movie.model.TrendingQuery
import com.amro.domain.movie.model.Genre
import com.amro.domain.movie.model.Movie
import com.amro.domain.movie.model.MovieDetails
import com.amro.domain.movie.model.MovieIdentifier
import com.amro.domain.movie.model.MovieProviderType
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun observeMovieDetails(id: MovieIdentifier, language: String): Flow<MovieDetails?>
    suspend fun refreshMovieDetails(id: MovieIdentifier, language: String)
    fun trending(query: TrendingQuery): Flow<PagingData<Movie>>
    fun genres(provider: MovieProviderType, language: String): Flow<List<Genre>>
    suspend fun refreshTrending(query: TrendingQuery)
    suspend fun metadata(
        provider: MovieProviderType,
        catalog: CatalogType,
        language: String,
    ): CatalogMetadata?
}