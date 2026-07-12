package com.amro.application.movie.policy

import com.amro.application.movie.model.CatalogMetadata
import com.amro.application.movie.model.CatalogType
import com.amro.domain.movie.model.MovieProviderType
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant
import kotlin.time.Duration.Companion.days

class TrendingMoviesPolicyTest {
    private val now = Instant.parse("2026-07-10T10:00:00Z")
    private val policy = TrendingMoviesPolicy({ now });

    @Test
    fun `missing trending catalog refreshes`() = assertTrue(policy.requiresRefresh(null))

    @Test
    fun `trending catalog becomes stale after one day`() {
        val catalogMetadata = CatalogMetadata(
            MovieProviderType.TMDB,
            "en-US",
            CatalogType.Trending,
            now.minusSeconds(1.days.inWholeSeconds),
            5,
            100
        )
        assertTrue(policy.requiresRefresh(catalogMetadata))
    }
}