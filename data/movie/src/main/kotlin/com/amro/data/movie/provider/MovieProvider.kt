package com.amro.data.movie.provider

import com.amro.data.movie.provider.model.ProviderGenre
import com.amro.data.movie.provider.model.ProviderMovieDetails
import com.amro.data.movie.provider.model.ProviderMoviePage
import com.amro.domain.movie.model.MovieProviderType

internal interface MovieProvider {
    val type: MovieProviderType

    suspend fun genres(
        language: String,
    ): List<ProviderGenre>

    suspend fun trending(
        page: Int,
        language: String,
    ): ProviderMoviePage

    suspend fun details(
        movieId: String,
        language: String,
    ): ProviderMovieDetails
}