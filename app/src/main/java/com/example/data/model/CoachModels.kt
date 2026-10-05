package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class BerthStatus(val label: String) {
    CONFIRMED("Confirmed"),
    RAC("RAC (Sitting)"),
    WAITLISTED("Waitlisted"),
    NO_SHOW("No-Show Marked"),
    REALLOCATED("AI Reallocated")
}

data class BerthInfo(
    val berthNumber: Int,
    val coach: String,
    val berthType: String, // "Lower", "Middle", "Upper", "Side Lower", "Side Upper"
    val travelClass: String,
    var passengerName: String,
    var pnr: String,
    var originSegment: String,
    var destinationSegment: String,
    var status: BerthStatus,
    var reassignedPassengerName: String? = null,
    var reassignedPnr: String? = null
)

data class WaitingPassenger(
    val id: String,
    val pnr: String,
    val name: String,
    val initialStatus: String, // "RAC-1", "WL-1", "WL-2"
    val origin: String,
    val destination: String,
    val travelClass: String,
    val seniorityRank: Int,
    val bookingTimeDaysAgo: Int
)

@Entity(tableName = "reallocation_audit")
data class ReallocationAuditEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val trainNumber: String,
    val coach: String,
    val seatNumber: Int,
    val segment: String,
    val noShowPassenger: String,
    val noShowPnr: String,
    val reallocatedPassenger: String,
    val reallocatedPnr: String,
    val originalWaitingStatus: String,
    val timestamp: Long = System.currentTimeMillis(),
    val recoveredSeatKm: Int,
    val revenueProtected: Double
)
