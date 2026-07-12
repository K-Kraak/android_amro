package com.amro.data.movie.di

import android.content.Context
import androidx.room.Room
import com.amro.application.movie.policy.TrendingMoviesPolicy
import com.amro.application.movie.repository.MovieRepository
import com.amro.core.common.time.Clock
import com.amro.core.database.AmroDatabase
import com.amro.data.movie.provider.tmdb.TmdbMovieProvider
import com.amro.data.movie.provider.tmdb.api.TmdbApi
import com.amro.data.movie.provider.tmdb.network.TmdbAuthorizationInterceptor
import com.amro.data.movie.repository.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MovieDataModule {

    @Provides
    fun policy(clock: Clock) = TrendingMoviesPolicy(clock)

    @Provides
    @Singleton
    fun database(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            AmroDatabase::class.java,
            "amro.db"
        )
            .fallbackToDestructiveMigration(true)
            .build()


    @Provides
    @Singleton
    internal fun api(
        client: OkHttpClient,
    ): TmdbApi {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/")
            .client(client)
            .addConverterFactory(
                json.asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(TmdbApi::class.java)
    }

    @Provides
    @Singleton
    internal fun provideTmdbOkHttpClient(
        authorizationInterceptor: TmdbAuthorizationInterceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(authorizationInterceptor)
            .build()


    @Provides
    @Singleton
    internal fun provider(api: TmdbApi) = TmdbMovieProvider(api)

    @Module
    @InstallIn(SingletonComponent::class)
    internal abstract class Bindings {
        @Binds
        @Singleton
        abstract fun bindMovieRepository(
            implementation: MovieRepositoryImpl,
        ): MovieRepository
    }
}