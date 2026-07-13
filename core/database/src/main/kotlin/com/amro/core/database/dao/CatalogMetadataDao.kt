package com.amro.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.amro.core.database.entity.CatalogMetadataEntity

@Dao
interface CatalogMetadataDao {
    @Query(
        """
        SELECT * 
        FROM catalog_metadata
        WHERE providerId=:providerId 
            AND catalogId=:catalogId 
            AND language=:language
        """
    )
    suspend fun get(providerId: Int, catalogId: Int, language: String): CatalogMetadataEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: CatalogMetadataEntity)
}