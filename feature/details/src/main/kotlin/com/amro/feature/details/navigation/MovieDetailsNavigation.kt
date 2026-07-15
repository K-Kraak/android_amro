package com.amro.feature.details.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.amro.domain.movie.model.MovieIdentifier
import com.amro.domain.movie.model.MovieProviderType
import com.amro.feature.details.MovieDetailsRoute
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsDestination(val providerId: Int, val movieId: String)

fun NavHostController.navigateToMovieDetails(id: MovieIdentifier) {
    navigate(MovieDetailsDestination(id.provider.id, id.value))
}

fun NavGraphBuilder.movieDetailsScreen(onBackClick: () -> Unit) {
    composable<MovieDetailsDestination> { entry ->
        val destination = entry.toRoute<MovieDetailsDestination>()
        MovieDetailsRoute(
            provider = MovieProviderType.fromId(destination.providerId),
            movieId = destination.movieId,
            onBackClick = onBackClick,
        )
    }
}
