package com.amro.feature.trending

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.amro.domain.movie.model.Movie

@Composable
internal fun MovieCard(
    movie: Movie,
    onMovieClicked: (movie: Movie) -> Unit,
) {
    ElevatedCard(
        onClick = { onMovieClicked(movie) },
        modifier = Modifier.fillMaxWidth()
    ) {
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