package com.amro.data.movie.provider.tmdb.di

import com.amro.data.movie.provider.MovieProvider
import com.amro.data.movie.provider.tmdb.TmdbMovieProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
internal abstract class TmdbProviderModule {

    @Binds
    @IntoSet
    abstract fun bindTmdbProvider(
        implementation: TmdbMovieProvider,
    ): MovieProvider
}