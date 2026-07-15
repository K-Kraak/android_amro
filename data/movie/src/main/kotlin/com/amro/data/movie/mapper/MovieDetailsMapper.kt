package com.amro.data.movie.mapper

import com.amro.core.database.entity.MovieDetailsEntity
import com.amro.data.movie.provider.model.ProviderMovieDetails
import com.amro.domain.movie.model.Genre
import com.amro.domain.movie.model.GenreIdentifier
import com.amro.domain.movie.model.MovieDetails
import com.amro.domain.movie.model.MovieIdentifier
import com.amro.domain.movie.model.MovieProviderType
import java.time.Instant
import java.time.LocalDate

private const val ID_SEPARATOR = ","
private const val NAME_SEPARATOR = "|"

internal fun ProviderMovieDetails.toEntity(
    provider: MovieProviderType,
    language: String,
    updatedAt: Instant,
) = MovieDetailsEntity(
    providerId = provider.id,
    movieId = id,
    language = language,
    title = title,
    originalTitle = originalTitle,
    overview = overview,
    tagline = tagline,
    releaseDate = releaseDate?.toString(),
    runtimeMinutes = runtimeMinutes,
    genreIds = genres.joinToString(ID_SEPARATOR) { it.id },
    genreNames = genres.joinToString(ID_SEPARATOR) { it.name },
    posterUrl = posterUrl,
    backdropUrl = backdropUrl,
    voteAverage = voteAverage,
    voteCount = voteCount,
    status = status,
    homepage = homepage,
    updatedAtEpochMillis = updatedAt.toEpochMilli(),
)

internal fun MovieDetailsEntity.toDomain(): MovieDetails {
    val providerType = MovieProviderType.fromId(providerId)
    val ids = genreIds.splitValues(ID_SEPARATOR)
    val names = genreNames.splitValues(NAME_SEPARATOR)
    return MovieDetails(
        id = MovieIdentifier(providerType, movieId),
        title = title,
        originalTitle = originalTitle,
        overview = overview,
        tagline = tagline,
        releaseDate = releaseDate?.let(LocalDate::parse),
        runtimeMinutes = runtimeMinutes,
        genres = ids.zip(names).map { (id, name) ->
            Genre(GenreIdentifier(providerType, id), name)
        },
        posterUrl = posterUrl,
        backdropUrl = backdropUrl,
        voteAverage = voteAverage,
        voteCount = voteCount,
        status = status,
        homepage = homepage,
    )
}

private fun String.splitValues(separator: String) =
    takeIf(String::isNotEmpty)?.split(separator).orEmpty()
