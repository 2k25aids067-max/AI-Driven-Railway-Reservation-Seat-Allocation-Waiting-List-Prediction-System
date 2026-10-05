package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trains")
data class Train(
    @PrimaryKey val trainId: String,
    val trainNumber: String,
    val trainName: String,
    val sourceStation: String,
    val sourceCode: String,
    val destinationStation: String,
    val destinationCode: String,
    val intermediateStations: String, // Comma separated, e.g. "NDLS,CNB,PRYJ,DDU,HWH"
    val trainType: String, // "Rajdhani Express", "Vande Bharat", "Shatabdi", "Superfast"
    val departureTime: String,
    val arrivalTime: String,
    val availableClasses: String, // "1A,2A,3A,SL"
    val totalSeats: Int,
    val baseFare: Double,
    val historicalConfirmationRate: Float, // 0.0 - 1.0
    val avgCancellationRate: Float, // 0.0 - 1.0
    val popularityScore: Float // 0.0 - 1.0
) {
    fun getStationList(): List<String> = intermediateStations.split(",").map { it.trim() }
    fun getClassList(): List<String> = availableClasses.split(",").map { it.trim() }
}
