package com.amro.data.movie.provider.model

internal data class ProviderMoviePage(
    val movies: List<ProviderMovie>,
    val page: Int,
    val totalPages: Int,
)