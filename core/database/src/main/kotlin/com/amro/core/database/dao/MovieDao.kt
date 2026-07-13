package com.amro.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Upsert
import androidx.sqlite.db.SupportSQLiteQuery
import com.amro.core.database.entity.CatalogMetadataEntity
import com.amro.core.database.entity.GenreEntity
import com.amro.core.database.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @RawQuery(observedEntities = [MovieEntity::class])
    fun pagingSource(query: SupportSQLiteQuery): PagingSource<Int, MovieEntity>

    @Upsert
    suspend fun upsert(
        movies: List<MovieEntity>,
    )

    @Query(
        """
        DELETE FROM movies
        WHERE providerId = :providerId
          AND language = :language
        """
    )
    suspend fun deleteByProviderAndLanguage(
        providerId: Int,
        language: String,
    )
}
