package com.amro.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.amro.app.navigation.destinations.TrendingMoviesDestination
import com.amro.feature.trending.TrendingRoute

@Composable
fun AppNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = TrendingMoviesDestination
    ) {
        composable<TrendingMoviesDestination> {
            TrendingRoute(
                onMovieClicked = { movie ->
                    // TODO: @Koen Handle navigation to detail page once present
                    // navController.navigate(MovieDestination(movie.id))
                },
            )
        }
    }
}