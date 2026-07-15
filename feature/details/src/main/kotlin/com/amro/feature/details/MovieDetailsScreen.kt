package com.amro.feature.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.amro.core.designsystem.component.AmroCenteredMessage
import com.amro.core.designsystem.component.AmroLoadingIndicator
import com.amro.core.designsystem.theme.Dimens
import com.amro.domain.movie.model.MovieDetails

private val BACKDROP_HEIGHT = 220.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    state: MovieDetailsUiState,
    onBackClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(state.movie?.title ?: stringResource(R.string.movie_details_title)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.movie_details_back))
                    }
                },
                actions = {
                    IconButton(onClick = onRefreshClick, enabled = !state.refreshing) {
                        if (state.refreshing) CircularProgressIndicator()
                        else Icon(Icons.Default.Refresh, stringResource(R.string.movie_details_refresh))
                    }
                },
            )
        },
    ) { padding ->
        when {
            state.loading -> AmroLoadingIndicator(Modifier.padding(padding))
            state.movie != null -> MovieDetailsContent(state.movie, Modifier.padding(padding))
            else -> AmroCenteredMessage(
                message = stringResource(R.string.movie_details_error),
                actionLabel = stringResource(R.string.movie_details_retry),
                onActionClick = onRetryClick,
                modifier = Modifier.padding(padding),
            )
        }
    }
}

@Composable
private fun MovieDetailsContent(movie: MovieDetails, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        AsyncImage(
            model = movie.backdropUrl ?: movie.posterUrl,
            contentDescription = stringResource(R.string.movie_details_backdrop_description, movie.title),
            modifier = Modifier
                .fillMaxWidth()
                .height(BACKDROP_HEIGHT),
            contentScale = ContentScale.Crop,
        )
        Column(
            modifier = Modifier.padding(Dimens.spaceLarge),
            verticalArrangement = Arrangement.spacedBy(Dimens.spaceMedium),
        ) {
            Text(movie.title, style = MaterialTheme.typography.headlineMedium)
            movie.tagline?.let { Text(it, style = MaterialTheme.typography.titleMedium) }
            Row(horizontalArrangement = Arrangement.spacedBy(Dimens.spaceLarge)) {
                Text(movie.releaseDate?.year?.toString() ?: stringResource(R.string.movie_details_release_unknown))
                movie.runtimeMinutes?.let {
                    Text(stringResource(R.string.movie_details_runtime_minutes, it))
                }
                Text(stringResource(R.string.movie_details_rating, movie.voteAverage))
            }
            Text(stringResource(R.string.movie_details_genres), style = MaterialTheme.typography.titleMedium)
            Text(movie.genres.joinToString { it.name })
            Text(stringResource(R.string.movie_details_overview), style = MaterialTheme.typography.titleLarge)
            Text(
                movie.overview.ifBlank {
                    stringResource(R.string.movie_details_overview_missing)
                }
            )
        }
    }
}
