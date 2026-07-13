package com.amro.feature.trending

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.amro.domain.movie.model.Movie

// TODO: @Koen fix deprecated hiltViewModel usage
@Composable
fun TrendingRoute(
    onMovieClicked: (movie: Movie) -> Unit,
    vm: TrendingViewModel = hiltViewModel(),
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val movies = vm.movies.collectAsLazyPagingItems()
    TrendingScreen(
        state = state,
        movies = movies,
        refresh = vm::refresh,
        toggleGenre = vm::toggleGenre,
        searchQueryChange = vm::search,
        sort = vm::sort,
        onMovieClicked = onMovieClicked,
    )
}