package com.amro.feature.trending

import com.amro.application.movie.model.TrendingQuery
import com.amro.domain.movie.model.Genre

data class TrendingUiState(
    val query: TrendingQuery,
    val genres: List<Genre> = emptyList(),
    val refreshing: Boolean = false,
    val error: TrendingUiError? = null,
)
