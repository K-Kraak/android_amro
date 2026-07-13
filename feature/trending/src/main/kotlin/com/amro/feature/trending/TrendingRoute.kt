package com.amro.feature.trending

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.amro.domain.movie.model.MovieIdentifier

@Composable
fun TrendingRoute(
    viewModel: TrendingViewModel = hiltViewModel(),
    onMovieClick: (movieId: MovieIdentifier) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val movies = viewModel.movies.collectAsLazyPagingItems()

    TrendingScreen(
        state = state,
        movies = movies,
        onSearchChanged = viewModel::onSearchChanged,
        onGenreToggled = viewModel::onGenreToggled,
        onSortSelected = viewModel::onSortSelected,
        onRefreshClick = viewModel::onRefreshRequested,
        onRetryClick = viewModel::onRetryRequested,
        onMovieClick = onMovieClick,
    )
}
