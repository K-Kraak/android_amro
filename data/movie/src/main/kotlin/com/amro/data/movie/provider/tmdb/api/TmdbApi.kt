package com.amro.data.movie.provider.tmdb.api

import com.amro.data.movie.provider.tmdb.dto.*
import retrofit2.http.GET
import retrofit2.http.Query

internal interface TmdbApi {
    @GET("3/trending/movie/day")
    suspend fun trending(
        @Query("page") page: Int,
        @Query("language") language: String,
    ): TmdbMoviePageDto

    @GET("3/genre/movie/list")
    suspend fun genres(@Query("language") language: String): TmdbGenreResponseDto
}