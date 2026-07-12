package com.amro.data.movie.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.amro.application.movie.model.CatalogMetadata
import com.amro.application.movie.model.CatalogType
import com.amro.application.movie.model.TrendingQuery
import com.amro.application.movie.policy.TrendingMoviesPolicy
import com.amro.application.movie.repository.MovieRepository
import com.amro.core.common.time.Clock
import com.amro.core.database.AmroDatabase
import com.amro.core.database.entity.CatalogMetadataEntity
import com.amro.core.database.entity.GenreEntity
import com.amro.core.database.entity.MovieEntity
import com.amro.core.dispatchers.DispatcherProvider
import com.amro.data.movie.mapper.toDomain
import com.amro.data.movie.mapper.toEntity
import com.amro.data.movie.mapper.toSQLiteQuery
import com.amro.data.movie.provider.MovieProvider
import com.amro.data.movie.provider.MovieProviderRegistry
import com.amro.domain.movie.model.Movie
import com.amro.domain.movie.model.MovieProviderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class MovieRepositoryImpl @Inject constructor(
    private val db: AmroDatabase,
    private val providerRegistry: MovieProviderRegistry,
    private val clock: Clock,
    private val policy: TrendingMoviesPolicy,
    private val dispatchers: DispatcherProvider,
) : MovieRepository {


    override fun trending(query: TrendingQuery): Flow<PagingData<Movie>> {
        val paging = Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                db.movieDao().pagingSource(query.toSQLiteQuery())
            }
        )
        return paging.flow.map { pagingData -> pagingData.map { entity -> entity.toDomain() } }
    }

    override fun genres(provider: MovieProviderType, language: String) =
        db.genreDao().observe(provider.id, language).map { items ->
            items.map { it.toDomain() }
        }

    override suspend fun refreshTrending(
        query: TrendingQuery,
    ): Unit = withContext(dispatchers.io) {
        val provider = providerRegistry.require(query.provider)
        val catalog = fetchTrendingCatalog(
            provider,
            query.language,
        )
        replaceTrendingCatalog(
            provider.type,
            query.language,
            catalog,
        )
    }


    override suspend fun metadata(
        provider: MovieProviderType,
        catalog: CatalogType,
        language: String,
    ) =
        db.catalogMetadataDao().get(
            provider.id,
            catalog.id,
            language,
        )?.let {
            CatalogMetadata(
                provider,
                language,
                catalog,
                java.time.Instant.ofEpochMilli(it.updatedAtEpochMillis),
                it.pageCount,
                it.itemCount,
            )
        }

    private suspend fun replaceTrendingCatalog(
        provider: MovieProviderType,
        language: String,
        catalog: RefreshedTrendingCatalog,
    ) {
        val metadata = CatalogMetadataEntity(
            providerId = provider.id,
            catalogId = CatalogType.Trending.id,
            language = language,
            updatedAtEpochMillis = clock.now().toEpochMilli(),
            pageCount = catalog.fetchedPages,
            itemCount = catalog.movies.size,
        )

        db.replaceTrendingCatalog(
            providerId = provider.id,
            language = language,
            genres = catalog.genres,
            movies = catalog.movies,
            metadata = metadata,
        )
    }

    private suspend fun fetchTrendingCatalog(
        provider: MovieProvider,
        language: String,
    ): RefreshedTrendingCatalog {
        val genres = provider
            .genres(language)
            .map { genre ->
                genre.toEntity(
                    providerType = provider.type,
                    language = language,
                )
            }

        val trendingMovies = fetchTrendingMovies(
            provider = provider,
            language = language,
        )

        return RefreshedTrendingCatalog(
            genres = genres,
            movies = trendingMovies.movies,
            fetchedPages = trendingMovies.fetchedPages,
        )
    }

    private suspend fun fetchTrendingMovies(
        provider: MovieProvider,
        language: String,
    ): FetchedTrendingMovies {
        val movies = mutableListOf<MovieEntity>()

        var page = 1
        var fetchedPages = 0

        while (movies.size < policy.maximumMovies) {
            val response = provider.trending(
                page = page,
                language = language,
            )

            fetchedPages++

            val remainingCapacity =
                policy.maximumMovies - movies.size

            movies += response.movies
                .take(remainingCapacity)
                .map { movie ->
                    movie.toEntity(
                        provider = provider.type,
                        language = language,
                    )
                }

            val hasReachedLastPage =
                response.movies.isEmpty() ||
                        page >= response.totalPages

            if (hasReachedLastPage) {
                break
            }

            page++
        }

        return FetchedTrendingMovies(
            movies = movies,
            fetchedPages = fetchedPages,
        )
    }

    private data class FetchedTrendingMovies(
        val movies: List<MovieEntity>,
        val fetchedPages: Int,
    )

    private data class RefreshedTrendingCatalog(
        val genres: List<GenreEntity>,
        val movies: List<MovieEntity>,
        val fetchedPages: Int,
    )
}