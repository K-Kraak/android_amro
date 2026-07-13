package com.amro.feature.trending

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.compose.LazyPagingItems
import com.amro.application.movie.model.MovieSortField
import com.amro.domain.movie.model.Movie
import com.amro.domain.movie.model.MovieIdentifier
import com.amro.feature.trending.component.TrendingContent
import com.amro.feature.trending.component.TrendingFilters
import com.amro.feature.trending.component.TrendingTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendingScreen(
    state: TrendingUiState,
    movies: LazyPagingItems<Movie>,
    onSearchChanged: (String) -> Unit,
    onGenreToggled: (String) -> Unit,
    onSortSelected: (MovieSortField) -> Unit,
    onRefreshClick: () -> Unit,
    onRetryClick: () -> Unit,
    onMovieClick: (MovieIdentifier) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TrendingTopBar(state.refreshing, onRefreshClick) },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            TrendingFilters(
                query = state.query,
                genres = state.genres,
                onSearchChanged = onSearchChanged,
                onGenreToggled = onGenreToggled,
                onSortSelected = onSortSelected,
            )
            TrendingContent(
                movies = movies,
                uiError = state.error,
                onRetryClick = onRetryClick,
                onMovieClick = onMovieClick,
                modifier = Modifier.weight(1f),
            )
        }
    }
}
