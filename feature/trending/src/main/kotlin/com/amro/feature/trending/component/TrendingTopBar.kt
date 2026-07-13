package com.amro.feature.trending.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.amro.feature.trending.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TrendingTopBar(refreshing: Boolean, onRefreshClick: () -> Unit) {
    TopAppBar(
        title = { Text(stringResource(R.string.trending_title)) },
        actions = {
            IconButton(onClick = onRefreshClick, enabled = !refreshing) {
                if (refreshing) CircularProgressIndicator() else Icon(
                    Icons.Default.Refresh,
                    contentDescription = stringResource(R.string.trending_refresh_content_description),
                )
            }
        },
    )
}
