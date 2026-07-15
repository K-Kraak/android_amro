package com.amro.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.amro.domain.movie.model.MovieProviderType
import com.amro.feature.details.navigation.movieDetailsScreen
import com.amro.feature.details.navigation.navigateToMovieDetails
import com.amro.feature.trending.navigation.TrendingDestination
import com.amro.feature.trending.navigation.trendingScreen

@Composable
fun AmroNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = TrendingDestination,
        modifier = modifier,
    ) {
        trendingScreen(
            onMovieClick = { movieId ->
                navController.navigateToMovieDetails(movieId)
            },
        )
        movieDetailsScreen(
            onBackClick = navController::navigateUp
        )
    }
}
