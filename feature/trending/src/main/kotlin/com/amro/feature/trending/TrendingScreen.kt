package com.amro.feature.trending

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.amro.application.movie.model.MovieSortField
import com.amro.domain.movie.model.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// TODO: @Koen localization, use constants for sizes, split up re-usable composables, etc.
fun TrendingScreen(
    state: TrendingUiState,
    movies: LazyPagingItems<Movie>,
    refresh: () -> Unit,
    toggleGenre: (genreId: String) -> Unit,
    searchQueryChange: (query: String) -> Unit,
    sort: (field: MovieSortField) -> Unit,
    onMovieClicked: (movie: Movie) -> Unit,
) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Trending movies") },
            actions = {
                IconButton(onClick = refresh)
                {
                    Icon(Icons.Default.Refresh, "Refresh")
                }
            }
        )
    }) { padding ->
        Column(Modifier.padding(padding)) {
            // Search
            OutlinedTextField(
                state.query.search,
                searchQueryChange,
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                label = { Text("Search top 100") },
                singleLine = true
            )

            // Genres
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    state.genres,
                    key = { it.id.value }) { genre ->
                    FilterChip(
                        selected = genre.id.value in state.query.genreIds,
                        onClick = { toggleGenre(genre.id.value) },
                        label = { Text(genre.name) }
                    )
                }
            }

            // Sort options
            LazyRow(
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(MovieSortField.entries) { field ->
                    AssistChip(
                        { sort(field) },
                        { Text(field.displayName()) }
                    )
                }
            }

            // Movies
            when {
                movies.loadState.refresh is LoadState.Loading -> Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(Modifier.padding(32.dp))
                }
                movies.itemCount == 0 -> Box(
                    Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    Text(
                        state.error ?: "No movies match the current filters."
                    )
                }
                else -> androidx.compose.foundation.lazy.LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(movies.itemCount) { index ->
                        movies[index]?.let {
                            MovieCard(
                                it,
                                onMovieClicked
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun MovieSortField.displayName() = when (this) {
    MovieSortField.POPULARITY -> "Popularity"
    MovieSortField.TITLE -> "Title"
    MovieSortField.RELEASE_DATE -> "Release date"
}