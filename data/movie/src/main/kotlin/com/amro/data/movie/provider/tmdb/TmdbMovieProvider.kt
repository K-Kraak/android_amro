package com.amro.data.movie.provider.tmdb

import com.amro.data.movie.provider.MovieProvider
import com.amro.data.movie.provider.model.ProviderGenre
import com.amro.data.movie.provider.model.ProviderMoviePage
import com.amro.data.movie.provider.tmdb.api.TmdbApi
import com.amro.data.movie.provider.tmdb.mapper.toProviderGenres
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
}