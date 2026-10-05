package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ODPairDemand(
    val origin: String,
    val destination: String,
    val distanceKm: Int,
    val totalRequests: Int,
    val fcfsAllocated: Int,
    val aiAllocated: Int,
    val capacityCap: Int,
    val farePerSeat: Double
)

data class SimulationMetrics(
    val method: String, // "FCFS Baseline" or "AI-Assisted"
    val totalCapacity: Int,
    val totalBookingsRequested: Int,
    val confirmedSeats: Int,
    val racSeats: Int,
    val wlSeats: Int,
    val cancellations: Int,
    val noShows: Int,
    val unusedSeats: Int,
    val occupancyRate: Float, // e.g. 0.82 = 82%
    val wlConversionRate: Float, // e.g. 0.31 = 31%
    val totalRevenue: Double,
    val runtimeMs: Long
)

data class SimulationComparison(
    val trainId: String,
    val trainName: String,
    val travelDate: String,
    val totalCapacity: Int,
    val fcfs: SimulationMetrics,
    val ai: SimulationMetrics,
    val odDemands: List<ODPairDemand>,
    val timestamp: Long = System.currentTimeMillis()
) {
    val occupancyDelta: Float get() = (ai.occupancyRate - fcfs.occupancyRate) * 100f
    val wlConversionDelta: Float get() = (ai.wlConversionRate - fcfs.wlConversionRate) * 100f
    val unusedSeatsReduction: Float get() = if (fcfs.unusedSeats > 0) {
        ((fcfs.unusedSeats - ai.unusedSeats).toFloat() / fcfs.unusedSeats.toFloat()) * 100f
    } else 0f
    val revenueUpliftPct: Float get() = if (fcfs.totalRevenue > 0) {
        ((ai.totalRevenue - fcfs.totalRevenue) / fcfs.totalRevenue * 100).toFloat()
    } else 0f
}

data class TimelineEvent(
    val timeStep: String,
    val eventType: String, // "BOOKING_SURGE", "CANCELLATION", "NO_SHOW_DETECTED", "AI_REALLOCATION"
    val description: String,
    val impact: String
)

@Entity(tableName = "simulation_history")
data class SimulationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val trainId: String,
    val trainName: String,
    val travelDate: String,
    val totalCapacity: Int,
    val fcfsOccupancy: Float,
    val aiOccupancy: Float,
    val fcfsWlConversion: Float,
    val aiWlConversion: Float,
    val fcfsUnusedSeats: Int,
    val aiUnusedSeats: Int,
    val fcfsRevenue: Double,
    val aiRevenue: Double,
    val timestamp: Long = System.currentTimeMillis()
)
