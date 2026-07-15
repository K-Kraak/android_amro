package com.amro.data.movie.repository

import com.amro.application.movie.model.TrendingQuery
import com.amro.application.movie.policy.TrendingMoviesPolicy
import com.amro.core.common.time.Clock
import com.amro.core.database.AmroDatabase
import com.amro.core.dispatchers.DispatcherProvider
import com.amro.data.movie.provider.MovieProvider
import com.amro.data.movie.provider.MovieProviderRegistry
import com.amro.data.movie.provider.model.ProviderGenre
import com.amro.data.movie.provider.model.ProviderMovie
import com.amro.data.movie.provider.model.ProviderMovieDetails
import com.amro.data.movie.provider.model.ProviderMoviePage
import com.amro.domain.movie.model.MovieProviderType
import io.mockk.coVerify
import io.mockk.mockk
import java.time.Instant
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieRepositoryImplTest {
    private val dispatcher = StandardTestDispatcher()
    private val database = mockk<AmroDatabase>(relaxed = true)
    private val clock = FixedClock(Instant.parse("2026-07-13T10:15:30Z"))
    private val policy = TrendingMoviesPolicy(clock)
    private val provider = FakeMovieProvider()
    private val registry = MovieProviderRegistry(setOf(provider))
    private val repository = MovieRepositoryImpl(
        db = database,
        providerRegistry = registry,
        clock = clock,
        policy = policy,
        dispatchers = TestDispatcherProvider(dispatcher),
    )

    @Test
    fun `refresh resolves provider and replaces complete catalog`() = runTest(dispatcher) {
        provider.genresResult = listOf(ProviderGenre("28", "Action"))
        provider.pages = mapOf(
            1 to page(1, 2, listOf("1", "2")),
            2 to page(2, 2, listOf("3")),
        )

        repository.refreshTrending(TrendingQuery(provider = MovieProviderType.TMDB, language = "en-US"))

        assertEquals(listOf(1, 2), provider.requestedPages)
        coVerify(exactly = 1) {
            database.replaceTrendingCatalog(
                providerId = MovieProviderType.TMDB.id,
                language = "en-US",
                genres = match { it.size == 1 },
                movies = match { it.map { movie -> movie.movieId } == listOf("1", "2", "3") },
                metadata = match {
                    it.itemCount == 3 && it.pageCount == 2 &&
                        it.updatedAtEpochMillis == clock.now().toEpochMilli()
                },
            )
        }
    }

    @Test
    fun `refresh stops at maximum movie count`() = runTest(dispatcher) {
        provider.pages = mapOf(
            1 to page(1, 10, (1..75).map(Int::toString)),
            2 to page(2, 10, (76..150).map(Int::toString)),
        )
        repository.refreshTrending(TrendingQuery(provider = MovieProviderType.TMDB, language = "en-US"))
        assertEquals(listOf(1, 2), provider.requestedPages)
        coVerify {
            database.replaceTrendingCatalog(
                providerId = any(), language = any(), genres = any(),
                movies = match { it.size == policy.maximumMovies },
                metadata = match { it.itemCount == policy.maximumMovies },
            )
        }
    }

    @Test
    fun `failed provider request does not replace catalog`() {
        provider.pages = mapOf(1 to page(1, 2, listOf("1")))
        provider.failurePage = 2
        assertThrows(IllegalStateException::class.java) {
            runTest(dispatcher) {
                repository.refreshTrending(TrendingQuery(provider = MovieProviderType.TMDB, language = "en-US"))
            }
        }
        coVerify(exactly = 0) {
            database.replaceTrendingCatalog(any(), any(), any(), any(), any())
        }
    }

    private fun page(page: Int, totalPages: Int, ids: List<String>) = ProviderMoviePage(
        page = page,
        totalPages = totalPages,
        movies = ids.map { id ->
            ProviderMovie(
                id = id,
                title = "Movie $id",
                overview = "",
                popularity = id.toDoubleOrNull() ?: 0.0,
                releaseDate = null,
                posterUrl = null,
                genreIds = emptySet(),
            )
        },
    )

    private class FakeMovieProvider : MovieProvider {
        override val type = MovieProviderType.TMDB
        var genresResult: List<ProviderGenre> = emptyList()
        var pages: Map<Int, ProviderMoviePage> = emptyMap()
        var failurePage: Int? = null
        val requestedPages = mutableListOf<Int>()
        override suspend fun genres(language: String) = genresResult
        override suspend fun trending(page: Int, language: String): ProviderMoviePage {
            requestedPages += page
            check(page != failurePage) { "Provider failure for page $page" }
            return requireNotNull(pages[page])
        }

        override suspend fun details(movieId: String, language: String): ProviderMovieDetails {
            return mockk()
        }
    }

    private class FixedClock(private val instant: Instant) : Clock {
        override fun now(): Instant = instant
    }

    private class TestDispatcherProvider(dispatcher: TestDispatcher) : DispatcherProvider {
        override val io: CoroutineDispatcher = dispatcher
        override val default: CoroutineDispatcher = dispatcher
        override val main: CoroutineDispatcher = dispatcher
    }
}
