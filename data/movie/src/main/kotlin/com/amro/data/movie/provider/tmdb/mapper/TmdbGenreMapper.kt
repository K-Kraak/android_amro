package com.amro.data.movie.provider.tmdb.mapper

import com.amro.data.movie.provider.model.ProviderGenre
import com.amro.data.movie.provider.tmdb.dto.TmdbGenreDto
import com.amro.data.movie.provider.tmdb.dto.TmdbGenreResponseDto

internal fun TmdbGenreResponseDto.toProviderGenres(): List<ProviderGenre> =
    genres.map(TmdbGenreDto::toProviderGenre)

private fun TmdbGenreDto.toProviderGenre(): ProviderGenre =
    ProviderGenre(
        id = id.toString(),
        name = name,
    )
