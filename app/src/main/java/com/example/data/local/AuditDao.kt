package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.ReallocationAuditEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AuditDao {
    @Query("SELECT * FROM reallocation_audit ORDER BY timestamp DESC")
    fun getAllAudits(): Flow<List<ReallocationAuditEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAudit(audit: ReallocationAuditEntity): Long

    @Query("DELETE FROM reallocation_audit")
    suspend fun clearAudits()
}
