package com.amro.feature.trending.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.amro.core.designsystem.theme.Dimens
import com.amro.domain.movie.model.Movie
import com.amro.feature.trending.R

private val PosterWidth = 110.dp
private const val OverviewMaxLines = 4

@Composable
internal fun MovieCard(
    movie: Movie,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Row(Modifier.height(IntrinsicSize.Min)) {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = stringResource(
                    R.string.trending_movie_poster_content_description,
                    movie.title,
                ),
                modifier = Modifier
                    .width(PosterWidth)
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop,
            )
            Column(Modifier.padding(Dimens.spaceLarge)) {
                Text(movie.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    movie.releaseDate?.year?.toString()
                        ?: stringResource(R.string.trending_release_unknown),
                    style = MaterialTheme.typography.labelMedium,
                )
                Spacer(Modifier.height(Dimens.spaceSmall))
                Text(movie.overview, maxLines = OverviewMaxLines, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}
