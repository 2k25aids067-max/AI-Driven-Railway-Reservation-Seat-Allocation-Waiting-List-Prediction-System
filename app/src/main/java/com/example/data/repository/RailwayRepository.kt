package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.InitialData
import com.example.data.model.PredictionHistoryEntity
import com.example.data.model.ReallocationAuditEntity
import com.example.data.model.SimulationEntity
import com.example.data.model.Train
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class RailwayRepository(private val database: AppDatabase) {

    val trains: Flow<List<Train>> = database.trainDao().getAllTrains().flowOn(Dispatchers.IO)
    val predictionHistory: Flow<List<PredictionHistoryEntity>> = database.predictionDao().getAllPredictions().flowOn(Dispatchers.IO)
    val simulationHistory: Flow<List<SimulationEntity>> = database.simulationDao().getAllSimulations().flowOn(Dispatchers.IO)
    val auditHistory: Flow<List<ReallocationAuditEntity>> = database.auditDao().getAllAudits().flowOn(Dispatchers.IO)

    suspend fun checkAndSeedInitialData() = withContext(Dispatchers.IO) {
        val count = database.trainDao().getTrainCount()
        if (count == 0) {
            database.trainDao().insertTrains(InitialData.TRAINS)
        }
    }

    suspend fun getTrainById(trainId: String): Train? = withContext(Dispatchers.IO) {
        database.trainDao().getTrainById(trainId) ?: InitialData.TRAINS.find { it.trainId == trainId }
    }

    suspend fun savePrediction(prediction: PredictionHistoryEntity): Long = withContext(Dispatchers.IO) {
        database.predictionDao().insertPrediction(prediction)
    }

    suspend fun deletePrediction(id: Long) = withContext(Dispatchers.IO) {
        database.predictionDao().deletePrediction(id)
    }

    suspend fun saveSimulation(simulation: SimulationEntity): Long = withContext(Dispatchers.IO) {
        database.simulationDao().insertSimulation(simulation)
    }

    suspend fun saveAudit(audit: ReallocationAuditEntity): Long = withContext(Dispatchers.IO) {
        database.auditDao().insertAudit(audit)
    }
}
