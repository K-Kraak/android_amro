package com.amro.feature.trending.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.amro.domain.movie.model.MovieIdentifier
import com.amro.feature.trending.TrendingRoute
import kotlinx.serialization.Serializable

@Serializable
data object TrendingDestination

fun NavGraphBuilder.trendingScreen(
    onMovieClick: (movieId: MovieIdentifier) -> Unit,
) {
    composable<TrendingDestination> {
        TrendingRoute(
            onMovieClick = onMovieClick,
        )
    }
}
