package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.PredictionHistoryEntity
import com.example.data.model.ReallocationAuditEntity
import com.example.data.model.SimulationEntity
import com.example.data.model.Train
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Train::class,
        PredictionHistoryEntity::class,
        SimulationEntity::class,
        ReallocationAuditEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun trainDao(): TrainDao
    abstract fun predictionDao(): PredictionDao
    abstract fun simulationDao(): SimulationDao
    abstract fun auditDao(): AuditDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "rail_reserve_ai.db"
                ).addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            getInstance(context).trainDao().insertTrains(InitialData.TRAINS)
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
