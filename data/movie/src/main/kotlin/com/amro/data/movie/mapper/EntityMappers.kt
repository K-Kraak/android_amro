package com.amro.data.movie.mapper

import com.amro.core.database.entity.GenreEntity
import com.amro.core.database.entity.MovieEntity
import com.amro.data.movie.provider.model.ProviderGenre
import com.amro.data.movie.provider.model.ProviderMovie
import com.amro.domain.movie.model.Genre
import com.amro.domain.movie.model.GenreIdentifier
import com.amro.domain.movie.model.Movie
import com.amro.domain.movie.model.MovieIdentifier
import com.amro.domain.movie.model.MovieProviderType
import java.time.LocalDate


internal fun ProviderGenre.toEntity(
    providerType: MovieProviderType,
    language: String,
): GenreEntity =
    GenreEntity(
        providerId = providerType.id,
        genreId = id,
        language = language,
        name = name,
    )

internal fun ProviderMovie.toEntity(
    provider: MovieProviderType,
    language: String,
): MovieEntity =
    MovieEntity(
        providerId = provider.id,
        movieId = id,
        language = language,
        title = title,
        overview = overview,
        genreIds = genreIds.sorted().joinToString(","),
        popularity = popularity,
        releaseDate = releaseDate?.toString(),
        posterUrl = posterPath,
    )

fun MovieEntity.toDomain() = Movie(
    MovieIdentifier(
        provider = MovieProviderType.fromId(providerId),
        value = movieId,
    ),
    title,
    overview,
    posterUrl,
    releaseDate?.let { runCatching { LocalDate.parse(it) }.getOrNull() },
    popularity,
    genreIds.split(',').mapNotNull { it.toIntOrNull() }.toSet()
)

fun GenreEntity.toDomain() = Genre(
    id = GenreIdentifier(
        provider = MovieProviderType.fromId(providerId),
        value = genreId
    ),
    name,
)