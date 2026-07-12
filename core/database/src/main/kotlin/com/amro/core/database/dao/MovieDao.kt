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

@Dao
interface GenreDao {

    @Query(
        """
        SELECT *
        FROM genres
        WHERE providerId = :providerId
          AND language = :language
        ORDER BY name
        """
    )
    fun observe(
        providerId: Int,
        language: String,
    ): Flow<List<GenreEntity>>

    @Upsert
    suspend fun upsert(items: List<GenreEntity>)

    @Query(
        """
        DELETE FROM genres
        WHERE providerId = :providerId
          AND language = :language
        """
    )
    suspend fun deleteByProviderAndLanguage(
        providerId: Int,
        language: String,
    )
}

@Dao
interface CatalogMetadataDao {
    @Query("SELECT * FROM catalog_metadata WHERE providerId=:providerId AND catalogId=:catalogId AND language=:language")
    suspend fun get(providerId: Int, catalogId: Int, language: String): CatalogMetadataEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: CatalogMetadataEntity)
}
