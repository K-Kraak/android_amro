package com.amro.data.movie.provider.tmdb

import com.amro.data.movie.provider.MovieProvider
import com.amro.data.movie.provider.model.ProviderGenre
import com.amro.data.movie.provider.model.ProviderMovieDetails
import com.amro.data.movie.provider.model.ProviderMoviePage
import com.amro.data.movie.provider.tmdb.api.TmdbApi
import com.amro.data.movie.provider.tmdb.mapper.toProviderGenres
import com.amro.data.movie.provider.tmdb.mapper.toProviderMovieDetails
import com.amro.data.movie.provider.tmdb.mapper.toProviderMoviePage
import com.amro.domain.movie.model.MovieProviderType
import javax.inject.Inject

internal class TmdbMovieProvider @Inject constructor(
    private val api: TmdbApi,
) : MovieProvider {

    override val type = MovieProviderType.TMDB

    override suspend fun genres(language: String): List<ProviderGenre> =
        api.genres(language).toProviderGenres()

    override suspend fun trending(
        page: Int,
        language: String,
    ): ProviderMoviePage =
        api.trending(
            page = page,
            language = language,
        ).toProviderMoviePage()

    override suspend fun details(
        movieId: String,
        language: String,
    ): ProviderMovieDetails =
        api.movieDetails(
            movieId = movieId,
            language = language,
        ).toProviderMovieDetails()
}