package com.amro.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.amro.core.database.entity.GenreEntity
import kotlinx.coroutines.flow.Flow

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