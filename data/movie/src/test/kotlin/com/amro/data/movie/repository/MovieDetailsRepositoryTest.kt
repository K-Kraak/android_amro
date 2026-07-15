package com.amro.data.movie.repository

import com.amro.core.common.time.Clock
import com.amro.core.database.AmroDatabase
import com.amro.core.dispatchers.DispatcherProvider
import com.amro.data.movie.provider.MovieProvider
import com.amro.data.movie.provider.MovieProviderRegistry
import com.amro.data.movie.provider.model.ProviderMovieDetails
import com.amro.domain.movie.model.MovieIdentifier
import com.amro.domain.movie.model.MovieProviderType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.Instant

class MovieDetailsRepositoryTest {
    private val dispatcher = StandardTestDispatcher()
    private val db = mockk<AmroDatabase>(relaxed = true)
    private val provider = mockk<MovieProvider>()
    private val clock = Clock { Instant.parse("2026-07-14T09:00:00Z") }

    @Test
    fun `refresh details persists provider and language scoped entity`() = runTest(dispatcher) {
        val providerType = MovieProviderType.TMDB
        every { provider.type } returns providerType
        coEvery { provider.details("550", "nl-NL") } returns ProviderMovieDetails(
            id = "550",
            title = "Fight Club",
            originalTitle = "Fight Club",
            overview = "Overview",
            tagline = null,
            releaseDate = null,
            runtimeMinutes = 139,
            genres = emptyList(),
            posterUrl = null,
            backdropUrl = null,
            voteAverage = 8.4,
            voteCount = 30_000,
            status = "Released",
            homepage = null,
        )
        val repository = createRepository(MovieProviderRegistry(setOf(provider)))

        repository.refreshMovieDetails(
            MovieIdentifier(providerType, "550"),
            "nl-NL",
        )

        coVerify {
            db.movieDetailsDao().upsert(match {
                it.providerId == providerType.id &&
                        it.movieId == "550" &&
                        it.language == "nl-NL" &&
                        it.updatedAtEpochMillis == clock.now().toEpochMilli()
            })
        }
    }

    private fun createRepository(registry: MovieProviderRegistry) = MovieRepositoryImpl(
        db = db,
        providerRegistry = registry,
        clock = clock,
        policy = mockk(),
        // TODO: @Koen move to common:testing for re-use across tests with an extension function to map it to this one
        dispatchers = object : DispatcherProvider {
            override val io: CoroutineDispatcher = dispatcher
            override val default: CoroutineDispatcher = dispatcher
            override val main: CoroutineDispatcher = dispatcher
        },
    )
}
