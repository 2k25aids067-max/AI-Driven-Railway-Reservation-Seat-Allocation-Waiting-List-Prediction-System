package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.Train
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainDao {
    @Query("SELECT * FROM trains ORDER BY popularityScore DESC")
    fun getAllTrains(): Flow<List<Train>>

    @Query("SELECT * FROM trains WHERE trainId = :trainId LIMIT 1")
    suspend fun getTrainById(trainId: String): Train?

    @Query("SELECT * FROM trains WHERE trainNumber = :trainNumber LIMIT 1")
    suspend fun getTrainByNumber(trainNumber: String): Train?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrains(trains: List<Train>)

    @Query("SELECT COUNT(*) FROM trains")
    suspend fun getTrainCount(): Int
}
