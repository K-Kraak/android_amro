package com.amro.feature.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amro.domain.movie.model.MovieProviderType

@Composable
fun MovieDetailsRoute(
    provider: MovieProviderType,
    movieId: String,
    onBackClick: () -> Unit,
    viewModel: MovieDetailsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(provider, movieId) {
        viewModel.initialize(provider, movieId)
    }
    MovieDetailsScreen(
        state = state,
        onBackClick = onBackClick,
        onRefreshClick = viewModel::onRefreshRequested,
        onRetryClick = viewModel::onRetryRequested,
    )
}
