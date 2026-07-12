package com.amro.data.movie.provider

import com.amro.domain.movie.model.MovieProviderType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class MovieProviderRegistry @Inject constructor(
    providers: Set<@JvmSuppressWildcards MovieProvider>,
) {
    private val providersByType: Map<MovieProviderType, MovieProvider> =
        providers.associateBy(MovieProvider::type)

    init {
        check(providersByType.size == providers.size) {
            "Multiple MovieProvider implementations registered for the same provider type."
        }
    }

    fun require(
        type: MovieProviderType,
    ): MovieProvider =
        requireNotNull(providersByType[type]) {
            "No MovieProvider registered for provider type: $type"
        }
}