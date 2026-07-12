package com.amro.application.movie.policy

import com.amro.application.movie.model.CatalogMetadata
import com.amro.core.common.time.Clock
import java.time.Duration

class TrendingMoviesPolicy(
    private val clock: Clock,
    val maximumMovies: Int = 100,
    val freshness: Duration = Duration.ofDays(1)
) {
    fun requiresRefresh(metadata: CatalogMetadata?): Boolean =
        metadata == null || !clock.now().isBefore(metadata.updatedAt.plus(freshness))
}