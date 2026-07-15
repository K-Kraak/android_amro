package com.amro.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.amro.core.database.entity.MovieDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDetailsDao {
    @Query("""
        SELECT * FROM movie_details
        WHERE providerId = :providerId
          AND movieId = :movieId
          AND language = :language
        LIMIT 1
    """)
    fun observe(providerId: Int, movieId: String, language: String): Flow<MovieDetailsEntity?>

    @Upsert
    suspend fun upsert(entity: MovieDetailsEntity)
}
