package com.amro.domain.movie.model

enum class MovieProviderType(val id: Int) {
    TMDB(1);

    companion object {
        fun fromId(providerId: Int): MovieProviderType =
            entries.first { it.id == providerId }
    }
}