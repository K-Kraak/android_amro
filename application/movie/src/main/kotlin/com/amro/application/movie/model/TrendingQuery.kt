package com.amro.application.movie.model

import com.amro.domain.movie.model.MovieProviderType

enum class MovieSortField(val defaultSortDirection: SortDirection) {
    POPULARITY(defaultSortDirection = SortDirection.DESCENDING),
    TITLE(defaultSortDirection = SortDirection.ASCENDING),
    RELEASE_DATE(defaultSortDirection = SortDirection.DESCENDING);
}

enum class SortDirection { ASCENDING, DESCENDING }

data class TrendingQuery(
    val provider: MovieProviderType = MovieProviderType.TMDB,
    val language: String = "en-US",
    val search: String = "",
    val genreIds: Set<String> = emptySet(),
    val sortField: MovieSortField = MovieSortField.POPULARITY,
    val direction: SortDirection = SortDirection.DESCENDING
)