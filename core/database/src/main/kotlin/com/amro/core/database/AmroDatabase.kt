package com.amro.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Transaction
import com.amro.core.database.dao.CatalogMetadataDao
import com.amro.core.database.dao.GenreDao
import com.amro.core.database.dao.MovieDao
import com.amro.core.database.entity.CatalogMetadataEntity
import com.amro.core.database.entity.GenreEntity
import com.amro.core.database.entity.MovieEntity

@Database(
    entities = [
        MovieEntity::class,
        GenreEntity::class,
        CatalogMetadataEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
abstract class AmroDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun genreDao(): GenreDao
    abstract fun catalogMetadataDao(): CatalogMetadataDao

    @Transaction
    open suspend fun replaceTrendingCatalog(
        providerId: Int,
        language: String,
        genres: List<GenreEntity>,
        movies: List<MovieEntity>,
        metadata: CatalogMetadataEntity,
    ) {
        genreDao().deleteByProviderAndLanguage(
            providerId = providerId,
            language = language,
        )
        genreDao().upsert(genres)

        movieDao().deleteByProviderAndLanguage(
            providerId = providerId,
            language = language,
        )
        movieDao().upsert(movies)

        catalogMetadataDao().upsert(metadata)
    }
}