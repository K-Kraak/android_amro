package com.amro.feature.trending

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil3.compose.AsyncImage
import com.amro.application.movie.model.MovieSortField
import com.amro.domain.movie.model.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// TODO: @Koen localization, this route should only pass the data objects to the composable Screen.
// TODO: @Koen use constants for sizes etc.
// TODO: @Koen fix deprecated hiltViewModel usage
fun TrendingRoute(vm: TrendingViewModel = hiltViewModel()) {
    val state by vm.state.collectAsStateWithLifecycle();
    val movies = vm.movies.collectAsLazyPagingItems(); Scaffold(topBar = {
        TopAppBar(
            title = { Text("Trending movies") },
            actions = {
                IconButton(onClick = { vm.refresh() })
                {
                    Icon(Icons.Default.Refresh, "Refresh")
                }
            }
        )
    }) { padding ->
        Column(Modifier.padding(padding)) {
            OutlinedTextField(
                state.query.search,
                vm::search,
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                label = { Text("Search top 100") },
                singleLine = true
            ); LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(
                state.genres,
                key = { it.id.value }) { genre ->
                FilterChip(
                    selected = genre.id.value in state.query.genreIds,
                    onClick = { vm.toggleGenre(genre.id.value) },
                    label = { Text(genre.name) }
                )
            }
        }; LazyRow(
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(MovieSortField.entries) { field ->
                AssistChip(
                    { vm.sort(field) },
                    { Text(field.name.lowercase().replaceFirstChar(Char::uppercase)) })
            }
        }; when {
            movies.loadState.refresh is LoadState.Loading -> Box(Modifier.fillMaxSize()) { CircularProgressIndicator(Modifier.padding(32.dp)) }; movies.itemCount == 0 -> Box(
                Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                Text(
                    state.error ?: "No movies match the current filters."
                )
            }; else -> androidx.compose.foundation.lazy.LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) { items(movies.itemCount) { index -> movies[index]?.let { MovieCard(it) } } }
        }
        }
    }
}

@Composable
private fun MovieCard(movie: Movie) {
    ElevatedCard(Modifier.fillMaxWidth()) {
        Row(Modifier.height(IntrinsicSize.Min)) {
            AsyncImage(
                movie.posterUrl,
                movie.title,
                Modifier
                    .width(110.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop
            )
            Column(Modifier.padding(16.dp)) {
                Text(movie.title, style = MaterialTheme.typography.titleMedium);
                Text(
                    movie.releaseDate?.year?.toString() ?: "Release unknown",
                    style = MaterialTheme.typography.labelMedium
                )
                Spacer(Modifier.height(8.dp))
                Text(movie.overview, maxLines = 4, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}