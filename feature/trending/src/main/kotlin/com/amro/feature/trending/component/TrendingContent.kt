package com.amro.feature.trending.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.amro.core.designsystem.component.AmroCenteredMessage
import com.amro.core.designsystem.component.AmroLoadingIndicator
import com.amro.core.designsystem.theme.Dimens
import com.amro.domain.movie.model.Movie
import com.amro.domain.movie.model.MovieIdentifier
import com.amro.feature.trending.R
import com.amro.feature.trending.TrendingUiError

@Composable
internal fun TrendingContent(
    movies: LazyPagingItems<Movie>,
    uiError: TrendingUiError?,
    onRetryClick: () -> Unit,
    onMovieClick: (MovieIdentifier) -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        movies.loadState.refresh is LoadState.Loading -> AmroLoadingIndicator(modifier)
        movies.loadState.refresh is LoadState.Error || uiError != null -> AmroCenteredMessage(
            message = stringResource(R.string.trending_error_refresh),
            actionLabel = stringResource(R.string.trending_retry),
            onActionClick = onRetryClick,
            modifier = modifier,
        )
        movies.itemCount == 0 -> AmroCenteredMessage(
            message = stringResource(R.string.trending_empty),
            modifier = modifier,
        )
        else -> LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(Dimens.spaceLarge),
            verticalArrangement = Arrangement.spacedBy(Dimens.spaceMedium),
        ) {
            items(
                count = movies.itemCount,
                key = movies.itemKey { it.id.value },
            ) { index ->
                movies[index]?.let { movie ->
                    MovieCard(movie = movie, onClick = { onMovieClick(movie.id) })
                }
            }
            if (movies.loadState.append is LoadState.Loading) {
                item { AmroLoadingIndicator() }
            }
            if (movies.loadState.append is LoadState.Error) {
                item {
                    AmroCenteredMessage(
                        message = stringResource(R.string.trending_error_loading_more),
                        actionLabel = stringResource(R.string.trending_retry),
                        onActionClick = movies::retry,
                    )
                }
            }
        }
    }
}
