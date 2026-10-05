package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.PredictionHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PredictionDao {
    @Query("SELECT * FROM prediction_history ORDER BY timestamp DESC")
    fun getAllPredictions(): Flow<List<PredictionHistoryEntity>>

    @Query("SELECT * FROM prediction_history WHERE pnr = :pnr LIMIT 1")
    suspend fun getPredictionByPnr(pnr: String): PredictionHistoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrediction(prediction: PredictionHistoryEntity): Long

    @Query("DELETE FROM prediction_history WHERE id = :id")
    suspend fun deletePrediction(id: Long)

    @Query("DELETE FROM prediction_history")
    suspend fun clearAll()
}
