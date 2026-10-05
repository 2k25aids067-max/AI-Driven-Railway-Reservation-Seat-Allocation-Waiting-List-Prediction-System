package com.example.engine

import com.example.data.model.BerthInfo
import com.example.data.model.BerthStatus
import com.example.data.model.ReallocationAuditEntity
import com.example.data.model.WaitingPassenger

object NoShowManager {

    fun generateInitialCoachBerths(coach: String = "B2"): List<BerthInfo> {
        val berthTypes = listOf("Lower", "Middle", "Upper", "Lower", "Middle", "Upper", "Side Lower", "Side Upper")
        val names = listOf(
            "Rajesh Sharma", "Pooja Verma", "Vikram Patel", "Sneha Rao",
            "Anil Mehta", "Sunita Nair", "Arjun Das", "Deepak Gupta",
            "Kavita Reddy", "Rohan Joshi", "Meera Sen", "Manoj Singh",
            "Aditi Iyer", "Sanjay Kumar", "Bhavna Jain", "Girish Menon"
        )

        return (1..16).map { berthNum ->
            val type = berthTypes[(berthNum - 1) % berthTypes.size]
            val name = names[(berthNum - 1) % names.size]
            val isRac = (berthNum == 7 || berthNum == 8)
            val pnr = "2847" + (100000 + berthNum * 123)

            BerthInfo(
                berthNumber = berthNum,
                coach = coach,
                berthType = type,
                travelClass = "3A",
                passengerName = name,
                pnr = pnr,
                originSegment = "NDLS",
                destinationSegment = if (berthNum % 3 == 0) "CNB" else "HWH",
                status = if (isRac) BerthStatus.RAC else BerthStatus.CONFIRMED,
                reassignedPassengerName = null,
                reassignedPnr = null
            )
        }
    }

    fun getEligibleWaitingPassengers(): List<WaitingPassenger> = listOf(
        WaitingPassenger(
            id = "WP-1",
            pnr = "9283710294",
            name = "Amitabh Sengupta",
            initialStatus = "RAC 1",
            origin = "CNB",
            destination = "HWH",
            travelClass = "3A",
            seniorityRank = 1,
            bookingTimeDaysAgo = 18
        ),
        WaitingPassenger(
            id = "WP-2",
            pnr = "7192830192",
            name = "Priya Deshmukh",
            initialStatus = "WL 1",
            origin = "CNB",
            destination = "DDU",
            travelClass = "3A",
            seniorityRank = 2,
            bookingTimeDaysAgo = 15
        ),
        WaitingPassenger(
            id = "WP-3",
            pnr = "5820194821",
            name = "Karthik Raman",
            initialStatus = "WL 2",
            origin = "PRYJ",
            destination = "HWH",
            travelClass = "3A",
            seniorityRank = 3,
            bookingTimeDaysAgo = 12
        ),
        WaitingPassenger(
            id = "WP-4",
            pnr = "3910294821",
            name = "Shalini Bannerjee",
            initialStatus = "WL 3",
            origin = "CNB",
            destination = "HWH",
            travelClass = "3A",
            seniorityRank = 4,
            bookingTimeDaysAgo = 9
        )
    )

    fun reassignNoShow(
        berth: BerthInfo,
        waitingQueue: List<WaitingPassenger>,
        currentSegment: String = "CNB"
    ): Pair<BerthInfo, ReallocationAuditEntity?> {
        val bestCandidate = waitingQueue.firstOrNull() ?: return Pair(berth, null)

        val updatedBerth = berth.copy(
            status = BerthStatus.REALLOCATED,
            reassignedPassengerName = bestCandidate.name,
            reassignedPnr = bestCandidate.pnr
        )

        val audit = ReallocationAuditEntity(
            trainNumber = "12302",
            coach = berth.coach,
            seatNumber = berth.berthNumber,
            segment = "$currentSegment -> ${berth.destinationSegment}",
            noShowPassenger = berth.passengerName,
            noShowPnr = berth.pnr,
            reallocatedPassenger = bestCandidate.name,
            reallocatedPnr = bestCandidate.pnr,
            originalWaitingStatus = bestCandidate.initialStatus,
            timestamp = System.currentTimeMillis(),
            recoveredSeatKm = 680,
            revenueProtected = 1420.0
        )

        return Pair(updatedBerth, audit)
    }
}
