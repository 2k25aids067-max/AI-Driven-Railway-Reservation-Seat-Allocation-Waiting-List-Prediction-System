package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class RiskLevel(val label: String, val description: String) {
    HIGH_CHANCE("High Chance", "Strong likelihood of confirmation prior to chart preparation"),
    MEDIUM_CHANCE("Medium Chance", "Moderate probability; track position & keep alternative plan"),
    LOW_CHANCE("Low Chance", "Low probability of confirmation; consider booking alternative train")
}

enum class WaitlistQuota(val code: String, val fullName: String, val priorityIndex: Int, val description: String) {
    GNWL("GNWL", "General Waiting List", 1, "Originates from starting station. Highest confirmation chance; gets first priority during chart preparation."),
    RLWL("RLWL", "Remote Location WL", 2, "Issued for intermediate major stations. Dedicated smaller quota, moderate confirmation chance."),
    PQWL("PQWL", "Pooled Quota WL", 3, "Shared between several intermediate stations. Low confirmation priority."),
    TQWL("TQWL", "Tatkal Waiting List", 4, "Direct tatkal waitlist. Very low movement as Tatkal tickets carry high cancellation penalty."),
    RAC("RAC", "Reservation Against Cancellation", 0, "Guaranteed travel berth; split seat with another passenger. 90%+ chance of full berth upgrade.")
}

data class InfluencingFactor(
    val factor: String,
    val isPositive: Boolean,
    val description: String,
    val weightPct: Int
)

data class AlternativeTrain(
    val trainNumber: String,
    val trainName: String,
    val departureTime: String,
    val arrivalTime: String,
    val travelClass: String,
    val currentStatus: String, // e.g. "AVAILABLE-42", "RAC-3", "WL-5"
    val confirmationProbability: Float,
    val fare: Double
)

data class SplitJourneyOption(
    val title: String,
    val trainNumber: String,
    val trainName: String,
    val splitStation: String,
    val leg1Origin: String,
    val leg1Dest: String,
    val leg1Status: String,
    val leg1Class: String,
    val leg2Origin: String,
    val leg2Dest: String,
    val leg2Status: String,
    val leg2Class: String,
    val totalFare: Double,
    val confirmationChancePct: Int,
    val reason: String
)

data class ChartingStatus(
    val isChartPrepared: Boolean,
    val hoursUntilDeparture: Double,
    val chartingRound: String, // "Open Booking", "Chart-1 Finalized (T-4h)", "Final Chart-2 (T-30m)"
    val seatsReleasedFromQuota: Int,
    val wlPromoted: Int,
    val racPromoted: Int
)

data class CancellationPolicyQuote(
    val baseFare: Double,
    val currentStatus: String,
    val refundAmount: Double,
    val clerkageDeduction: Double,
    val ruleDescription: String
)

data class ModelBenchmark(
    val modelName: String,
    val rocAuc: Float,
    val accuracy: Float,
    val precision: Float,
    val recall: Float,
    val f1: Float,
    val brierScore: Float,
    val isRecommended: Boolean
)

data class PredictionRequest(
    val pnr: String = "",
    val trainId: String,
    val origin: String,
    val destination: String,
    val travelDate: String,
    val travelClass: String, // "1A", "2A", "3A", "SL", "CC"
    val quotaCode: String = "GNWL", // GNWL, RLWL, PQWL, TQWL, RAC
    val quota: String, // "General", "Tatkal", "Ladies", "Senior Citizen"
    val currentStatusType: String, // "WL", "RAC"
    val currentPosition: Int,
    val daysRemaining: Int,
    val selectedModelType: String = "Gradient Boosting (Recommended)"
)

data class PredictionResult(
    val pnr: String,
    val trainNumber: String,
    val trainName: String,
    val origin: String,
    val destination: String,
    val travelDate: String,
    val travelClass: String,
    val quotaCode: String,
    val quota: String,
    val initialPosition: Int,
    val daysRemaining: Int,
    val confirmationProbability: Float, // 0.0 - 1.0 (e.g. 0.78 = 78%)
    val confidenceLower: Float,
    val confidenceUpper: Float,
    val riskLevel: RiskLevel,
    val estimatedPositionMovement: Int,
    val estimatedFinalPosition: String, // e.g. "CONFIRMED (CNF)", "RAC-4", "WL-8"
    val recommendation: String,
    val factors: List<InfluencingFactor>,
    val alternativeTrains: List<AlternativeTrain> = emptyList(),
    val splitJourneyOptions: List<SplitJourneyOption> = emptyList(),
    val chartingStatus: ChartingStatus,
    val cancellationPolicy: CancellationPolicyQuote,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "prediction_history")
data class PredictionHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pnr: String,
    val trainNumber: String,
    val trainName: String,
    val origin: String,
    val destination: String,
    val travelDate: String,
    val travelClass: String,
    val quota: String,
    val initialPosition: Int,
    val daysRemaining: Int,
    val probability: Float,
    val riskLevel: String,
    val estimatedMovement: Int,
    val recommendation: String,
    val timestamp: Long = System.currentTimeMillis()
)
