package com.amro.data.movie.provider.tmdb.mapper

import com.amro.data.movie.provider.model.ProviderGenre
import com.amro.data.movie.provider.model.ProviderMovieDetails
import com.amro.data.movie.provider.tmdb.dto.TmdbMovieDetailsDto
import java.time.LocalDate
import java.time.format.DateTimeParseException

// TODO: @Koen respect configuration for mapping images
private const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"
private const val POSTER_SIZE = "w500"
private const val BACKDROP_SIZE = "w1280"

internal fun TmdbMovieDetailsDto.toProviderMovieDetails() = ProviderMovieDetails(
    id = id.toString(),
    title = title,
    originalTitle = originalTitle,
    overview = overview,
    tagline = tagline?.takeIf(String::isNotBlank),
    releaseDate = releaseDate.toLocalDateOrNull(),
    runtimeMinutes = runtime?.takeIf { it > 0 },
    genres = genres.map { ProviderGenre(it.id.toString(), it.name) },
    posterUrl = posterPath.toImageUrlOrNull(POSTER_SIZE),
    backdropUrl = backdropPath.toImageUrlOrNull(BACKDROP_SIZE),
    voteAverage = voteAverage,
    voteCount = voteCount,
    status = status?.takeIf(String::isNotBlank),
    homepage = homepage?.takeIf(String::isNotBlank),
)

private fun String?.toImageUrlOrNull(size: String): String? {
    val path = this?.trim(  )?.takeIf(String::isNotEmpty) ?: return null
    return "$IMAGE_BASE_URL$size/${path.removePrefix("/")}"
}

private fun String?.toLocalDateOrNull(): LocalDate? {
    val value = this?.takeIf(String::isNotBlank) ?: return null
    return try {
        LocalDate.parse(value)
    } catch (_: DateTimeParseException) {
        null
    }
}
