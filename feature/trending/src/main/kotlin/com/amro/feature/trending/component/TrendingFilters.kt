package com.amro.feature.trending.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.amro.application.movie.model.MovieSortField
import com.amro.application.movie.model.TrendingQuery
import com.amro.core.designsystem.theme.Dimens
import com.amro.domain.movie.model.Genre
import com.amro.feature.trending.R

@Composable
internal fun TrendingFilters(
    query: TrendingQuery,
    genres: List<Genre>,
    onSearchChanged: (String) -> Unit,
    onGenreToggled: (String) -> Unit,
    onSortSelected: (MovieSortField) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Dimens.spaceSmall),
    ) {
        OutlinedTextField(
            value = query.search,
            onValueChange = onSearchChanged,
            modifier = Modifier.fillMaxWidth().padding(horizontal = Dimens.spaceLarge),
            label = { Text(stringResource(R.string.trending_search_label)) },
            singleLine = true,
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = Dimens.spaceLarge),
            horizontalArrangement = Arrangement.spacedBy(Dimens.spaceSmall),
        ) {
            items(genres, key = { it.id.value }) { genre ->
                FilterChip(
                    selected = genre.id.value in query.genreIds,
                    onClick = { onGenreToggled(genre.id.value) },
                    label = { Text(genre.name) },
                )
            }
        }
        LazyRow(
            contentPadding = PaddingValues(
                horizontal = Dimens.spaceLarge,
                vertical = Dimens.spaceSmall,
            ),
            horizontalArrangement = Arrangement.spacedBy(Dimens.spaceSmall),
        ) {
            items(MovieSortField.entries, key = MovieSortField::name) { field ->
                AssistChip(
                    onClick = { onSortSelected(field) },
                    label = { Text(field.localizedLabel()) },
                )
            }
        }
    }
}

@Composable
private fun MovieSortField.localizedLabel() = when (this) {
    MovieSortField.POPULARITY -> stringResource(R.string.trending_sort_popularity)
    MovieSortField.TITLE -> stringResource(R.string.trending_sort_title)
    MovieSortField.RELEASE_DATE -> stringResource(R.string.trending_sort_release_date)
}
