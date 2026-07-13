package com.amro.data.movie.provider.tmdb.mapper

import com.amro.data.movie.provider.model.ProviderMovie
import com.amro.data.movie.provider.model.ProviderMoviePage
import com.amro.data.movie.provider.tmdb.dto.TmdbMovieDto
import com.amro.data.movie.provider.tmdb.dto.TmdbMoviePageDto
import java.time.LocalDate
import java.time.format.DateTimeParseException

internal fun TmdbMoviePageDto.toProviderMoviePage(): ProviderMoviePage =
    ProviderMoviePage(
        page = page,
        totalPages = totalPages,
        movies = results.map(TmdbMovieDto::toProviderMovie),
    )

private fun TmdbMovieDto.toProviderMovie(): ProviderMovie =
    ProviderMovie(
        id = id.toString(),
        title = title,
        overview = overview,
        popularity = popularity,
        releaseDate = releaseDate.toLocalDateOrNull(),
        // TODO: @Koen should not be hardcoded but instead be based on image sizes
        //  in the configuration object
        posterUrl = "https://image.tmdb.org/t/p/w500$posterPath",
        genreIds = genreIds.mapTo(mutableSetOf()) { it.toString() },
    )

private fun String?.toLocalDateOrNull(): LocalDate? {
    if (this.isNullOrBlank()) return null
    return try {
        LocalDate.parse(this)
    } catch (_: DateTimeParseException) {
        null
    }
}
